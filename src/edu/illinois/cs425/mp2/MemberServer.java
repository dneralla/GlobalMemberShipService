package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class MemberServer {
	// node information
	private final InetAddress hostAddress;

	private DatagramSocket memberSocket;
	private MulticastSocket multicastSocket;

	private final MemberServer neighbourServer;
	private Timer lastReceivedHeartBeat;
	private boolean sendHeartBeat;
	private final List<MemberServer> globalList;

	public DatagramSocket getMemberSocket() {
		return memberSocket;
	}

	public void setMemberSocket(DatagramSocket memberSocket) {
		this.memberSocket = memberSocket;
	}

	public MemberServer() throws UnknownHostException {
		this.neighbourServer = this;
		this.hostAddress = InetAddress.getByName("localhost");
		this.globalList = new LinkedList<MemberServer>(Arrays.asList(this));
		this.sendHeartBeat = false;
	}

    public Timer getLastReceivedHeartBeat() {
		return lastReceivedHeartBeat;
	}

	public synchronized void setLastReceivedHeartBeat(Timer lastReceivedHeartBeat) {
		this.lastReceivedHeartBeat = lastReceivedHeartBeat;
	}

	public synchronized boolean getSendHeartBeat() {
		return sendHeartBeat;
	}

	public void setSendHeartBeat(boolean sendHeartBeat) {
		this.sendHeartBeat = sendHeartBeat;
	}

	public static void main(String[] args) throws IOException {
		MemberServer member = new MemberServer();
		if(args.length < 1) {
			System.out.println("Usage: java <portnumber>");
            return;
		}
		boolean listening = true;
		try {
			member.setMemberSocket(new DatagramSocket(Integer.parseInt(args[0])));
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(-1);
		}
        //starting heartbeat thread

		while (listening)
		{
		//	new MemberServerThread(serverSocket.accept()).start();
		}

	}
}
