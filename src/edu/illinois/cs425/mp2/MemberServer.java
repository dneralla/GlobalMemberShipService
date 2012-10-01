package edu.illinois.cs425.mp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * Class for starting server. For each new request, a separate thread is spawned
 * for processing it. For sending heartbeats periodically, a separate thread is
 * created and it sends messages if flag sendHeartBeat is set. All variables which
 * are accessed by multiple threads are declared as volatile.
 */
public class MemberServer {
	// node information
	private MemberNode node;
	private volatile MemberNode neighborNode;
	private volatile long lastReceivedHeartBeatTime;
	private volatile boolean sendHeartBeat = false;
	private volatile List<MemberNode> globalList;

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
		this.globalList = new LinkedList<MemberNode>(Arrays.asList(node));
		this.sendHeartBeat = false;
	}

	public long getLastReceivedHeartBeatTime() {
		return lastReceivedHeartBeatTime;
	}

	public void setLastReceivedHeartBeatTime(long lastReceivedHeartBeat) {
		this.lastReceivedHeartBeatTime = lastReceivedHeartBeat;
	}

	public boolean getSendHeartBeat() {
		return sendHeartBeat;
	}

	public void setSendHeartBeat(boolean sendHeartBeat) {
		this.sendHeartBeat = sendHeartBeat;
	}

	public static MemberServer start(String hostName, int hostPort)
			throws SocketException {
		MemberServer server = new MemberServer();
		server.setNode(MemberNode.start(hostName, hostPort));
		server.setNeighborNode(server.getNode());
		return server;
	}

	public void setNode(MemberNode node) {
		// TODO Auto-generated method stub
		this.node = node;
	}

	public MemberNode getNode() {
		return node;
	}

	public DatagramSocket getSocket() {
		return node.getSocket();
	}

	public synchronized boolean mergeMemberList(MemberNode node, String messageType) {

		boolean isLatest= false, isNewEntry = true;
		for (MemberNode member : globalList) {
			if (member.compareTo(node)) {
				isNewEntry = false;
				if (node.getTimeStamp().after(member.getTimeStamp())) {
					if (messageType.equals("JOIN")) {
						member.setTimeStamp(node.getTimeStamp());
					} else {
						globalList.remove(member);
						break;
					}
					isLatest = true;
				}
			}
		}
		if (isNewEntry) {
			globalList.add(node);
		}
		return isLatest;
	}


	public static void main(String[] args) throws Exception {

		// if(args.length < 2) {
		// System.out.println("Usage: java <portnumber>");
		// return;
		// }
		MemberServer server = null;
		MulticastServer multicastServer = null;
		boolean listening = true;
		try {
			server = MemberServer.start("localhost", Integer.parseInt(args[0]));
			multicastServer = new MulticastServer(server);
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
		// starting heartbeat thread
		new HeartBeatServiceThread(server).start();
		new ProcessorThread(server, multicastServer).start();

		try {
			DatagramSocket socket;
			socket = new DatagramSocket();
			String inputLine;
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("[Please Enter Command]$ ");
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.startsWith("join")) {
					byte[] buf = new byte[256];
					buf = new Message(MessageTypes.JOIN).toBytes();
					// InetAddress address = InetAddress.getByName(args[0]);
					DatagramPacket packet = new DatagramPacket(buf, buf.length,
							InetAddress.getByName("localhost"), 5090);
					server.getSocket().send(packet);
				} else if (inputLine.startsWith("leave")) {

				} else if (inputLine.equals("help")) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
