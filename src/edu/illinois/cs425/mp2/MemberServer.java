package edu.illinois.cs425.mp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * Class for starting server. For each new request, a separate thread is spawned
 * for processing it. For sending heartbeats periodically, a separate thread is
 * created and it sends messages if flag sendHeartBeat is set. All variables which
 * are accessed by multiple threads are declared as volatile.
 */
public class MemberServer{
	// node information
	private MemberNode node;
	private volatile MemberNode neighborNode;
	private volatile long lastReceivedHeartBeatTime;
	private volatile List<MemberNode> globalList;
    private volatile Logger logger;
    private DatagramSocket socket;
    private volatile MemberNode recentLeftNode;
    private TimerThread timer;
    private MemberNode heartbeatSendingNode;


	public TimerThread getTimer() {
		return timer;
	}

	public void setTimer(TimerThread timer) {
		this.timer = timer;
	}

	public MemberNode getRecentLeftNode() {
		return recentLeftNode;
	}

	public void setRecentLeftNode(MemberNode recentLeftNode) {
		this.recentLeftNode = recentLeftNode;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public List<MemberNode> getGlobalList() {
		return globalList;
	}

	public void setGlobalList(List<MemberNode> globalList) {
		this.globalList = globalList;
	}

	public MemberNode getNeighborNode() {
		return neighborNode;
	}

	public void setNeighborNode(MemberNode neighborNode) {
		this.neighborNode = neighborNode;
	}

	private MemberServer() {
		// initially doesn't have any neighbors
		this.neighborNode = null;
		this.recentLeftNode = null;
		this.heartbeatSendingNode = null;
		this.globalList = new ArrayList<MemberNode>();
	}

	public long getLastReceivedHeartBeatTime() {
		return lastReceivedHeartBeatTime;
	}

	public void setLastReceivedHeartBeatTime(long lastReceivedHeartBeat) {
		this.lastReceivedHeartBeatTime = lastReceivedHeartBeat;
	}

	public static MemberServer start(String hostName, int hostPort)
			throws SocketException, UnknownHostException {
		MemberServer server = new MemberServer();
		MemberNode node = new MemberNode(hostName, hostPort);
	    server.socket = new DatagramSocket(hostPort);
	    server.setNode(node);
		server.setNeighborNode(node);
		server.globalList.add(node);
		server.setHeartbeatSendingNode(node);
		return server;
	}

	private void setNode(MemberNode node) {
		// TODO Auto-generated method stub
		this.node = node;
	}

	public void stop() {
    	socket.close();
    }

	public MemberNode getNode() {
		return node;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

    public void sendMessage(Message message, MemberNode receiver) throws Exception {
    	if(!(message instanceof HeartBeatMessage))
        System.out.println("Sending message " + "sender: " + message.getSourceNode().getHostPort()+ "receiver: "+ receiver.getHostPort());
    	//getLogger().info(message.getDescription());
    	DatagramPacket packet = new DatagramPacket(message.toBytes(),
				message.toBytes().length, receiver.getHostAddress(), receiver.getHostPort());
		getSocket().send(packet);
    }

    public synchronized void printNodes() {
    	System.out.print("[");
		for (MemberNode node : getGlobalList())
			if(node!=null)
				System.out.print(node.getHostAddress()+" "+node.getHostPort() + ", ");
		System.out.println("]");
    }
    
	public static void main(String[] args) throws Exception {
		MemberServer server = null;
		MulticastServer multicastServer = null;
		FileHandler fileTxt = new FileHandler("Server_" + InetAddress.getLocalHost().getHostName() + "_" + args[0] + ".log");
		SimpleFormatter formatterTxt = new SimpleFormatter();

		//Assumption: Master server starts on 5090 port
		MemberNode master  = new MemberNode("linux5.ews.illinois.edu", 5095);

		
		// Create Logger
		LogManager lm = LogManager.getLogManager();
		lm.reset();
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);

		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		lm.addLogger(logger);

		
		try {
			
			server = MemberServer.start(InetAddress.getLocalHost().getHostName(),
					Integer.parseInt(args[0]));
			
			multicastServer = new MulticastServer(server);
			if(master.getHostAddress().equals(InetAddress.getLocalHost())) {
				System.out.println("I'm master node");
				multicastServer.ensureRunning();
			}
			server.setLogger(logger);

		} catch (SocketException e) {

			System.out.println("Error: Unable to open socket");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Could not	 listen on port.");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("Byte Construction failed");
			System.exit(-1);

		}

		logger.info("Staring logging");
		// starting heartbeat thread

		new ProcessorThread(server, multicastServer).start();
		new HeartBeatServiceThread().start();
		try {
			String inputLine;
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("[Please Enter Command]$ ");
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("join")) {
					server.getNode().setTimeStamp(new Date());
					Message message = new JoinMessage(server.getNode(), null,
							server.getNode());

					
					server.getLogger().info("Join message sending to"+master.getHostAddress());

					server.sendMessage(message, master);
					server.getLogger().info("Join message Sent");

				} else if (inputLine.equals("leave")) {
					server.getNode().setTimeStamp(new Date());
                    LeaveMessage leaveMessage = new LeaveMessage(server.getNode(), null, server.getNode());
					server.sendMessage(leaveMessage, server.getNeighborNode());
					server.getLogger().info("Leave Message sent");

				} else if (inputLine.startsWith("print")) {

					server.printNodes();

					
				} else if (inputLine.startsWith("set master")) {
					master.setHostAddress(InetAddress.getByName(inputLine.substring(12)));
				}
			 else if (inputLine.equals("next")) {
				System.out
						.println("Neighbour Port: " + server.getNeighborNode().getHostPort());
			}
				else if (inputLine.startsWith("set master")) {
					master.setHostAddress(InetAddress.getByName(inputLine.substring(12)));
				} 

				else if(inputLine.startsWith("dgrep"))
				{

				}
				else if (inputLine.equals("help")) {

					System.out
							.println("Usage: [join|leave] <hostname:hostport>");
				} else if (inputLine.equals("exit")) {
					System.exit(0);
				} else {
					System.out.println("use help for possible options");
				}
				System.out.print("[Please Enter Command]$ ");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public MemberNode getHeartbeatSendingNode() {
		return heartbeatSendingNode;
	}

	public void setHeartbeatSendingNode(MemberNode heartbeatSendingNode) {
		this.heartbeatSendingNode = heartbeatSendingNode;
	}
}
