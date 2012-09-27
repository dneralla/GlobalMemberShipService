package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

/*
 * Class for starting server. For each new request, a separate thread is spawned
 * for processing it. For sending heartbeats periodically, a separate thread is
 * created and it sends messages if flag sendHeartBeat is set. Synchronized methods
 * are being used for managing concurrent access to shared variables between threads.
 */
public class MemberServer {
	// node information
	private final MemberNode node;
	private DatagramSocket socket;
	private MulticastSocket multicastSocket;

	private volatile MemberNode neighborNode;

	private volatile Timer lastReceivedHeartBeat;
	private volatile boolean sendHeartBeat;
	private volatile List<MemberServer> globalList;

	public MemberNode getNeighborNode() {
		return neighborNode;
	}

	public void setNeighborNode(MemberNode neighborNode) {
		this.neighborNode = neighborNode;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setMemberSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public MemberServer(String hostName, int port) throws UnknownHostException, SocketException {
		// initially doesn't have any neighbors
		this.neighborNode = null;
		// open datagram socket
		setMemberSocket(new DatagramSocket(port));
		this.node = new MemberNode(InetAddress.getByName(hostName), port);
		this.globalList = new LinkedList<MemberServer>(Arrays.asList(this));
		this.sendHeartBeat = false;
	}

    public Timer getLastReceivedHeartBeat() {
		return lastReceivedHeartBeat;
	}

	public void setLastReceivedHeartBeat(Timer lastReceivedHeartBeat) {
		this.lastReceivedHeartBeat = lastReceivedHeartBeat;
	}

	public boolean getSendHeartBeat() {
		return sendHeartBeat;
	}

	public void setSendHeartBeat(boolean sendHeartBeat) {
		this.sendHeartBeat = sendHeartBeat;
	}


	public static void main(String[] args) throws IOException {

//		if(args.length < 2) {
//			System.out.println("Usage: java <portnumber>");
//            return;
//		}
		MemberServer server = null;
		boolean listening = true;
		try {
			server = new MemberServer("localhost", 4444);
		} catch (UnknownHostException e) {
			System.out.println("Error: Unknown host");
			System.exit(-1);
		} catch (SocketException e) {
			System.out.println("Error: Unable to open socket");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Could not listen on port.");
			System.exit(-1);
		}
        //starting heartbeat thread
        new HeartBeatServiceThread(server).start();

		while (listening)
		{
		//	new MemberServerThread(serverSocket.accept()).start();
		}

	}
}
