package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

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
	private volatile Timer lastReceivedHeartBeat;
	private volatile boolean sendHeartBeat;
	private volatile List<MemberServer> globalList;

	public MemberNode getNeighborNode() {
		return neighborNode;
	}

	public void setNeighborNode(MemberNode neighborNode) {
		this.neighborNode = neighborNode;
	}

	private MemberServer() {
		// initially doesn't have any neighbors
		this.neighborNode = null;
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


	public static MemberServer start(String hostName, int hostPort) throws SocketException {
        MemberServer server = new MemberServer();
        server.setNode(MemberNode.start(hostName, hostPort));
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
	public static void main(String[] args) throws IOException {

//		if(args.length < 2) {
//			System.out.println("Usage: java <portnumber>");
//            return;
//		}
		MemberServer server = null;
		boolean listening = true;
		try {
			server = MemberServer.start("localhost", 4444);
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
			new MemberServerThread(server).start();
		}

	}
}
