package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartBeatServiceThread extends Thread {
	private final MemberServer server;

	public HeartBeatServiceThread(MemberServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		byte[] buf = HeartBeatMessage.getBytes();
        InetAddress address;
        int port;
        DatagramSocket socket = server.getSocket();
		while (true) {
			if (server.getSendHeartBeat()) {
				// send HeartBeat request
		        address = server.getNeighborNode().getHostAddress();
		        port = server.getNeighborNode().getHostPort();
		        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		        try {
					socket.send(packet);
				} catch (IOException e) {
					System.out.println("Error in sending hearbeat message");
					e.printStackTrace();
				}
			}
		}
	}
}
