package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.TimerTask;

public class ProcessorThread extends Thread {
	private static MemberServer server;
	private static MulticastServer multicastServer;

	public static MemberServer getServer() {
		return server;
	}

	public static MulticastServer getMulticastServer() {
		return multicastServer;
	}

	public static void setMulticastServer(MulticastServer multicastServer) {
		ProcessorThread.multicastServer = multicastServer;
	}

	
	static boolean toStartHeartBeating = false;

	public ProcessorThread(MemberServer server, MulticastServer multicastServer) {
		ProcessorThread.server = server;
		ProcessorThread.multicastServer = multicastServer;
	}

	@Override
	public void run() {
		DatagramPacket packet;
		Message message;
		InetAddress senderAddress;
		int port;
		
		while (true) {
			byte receiveMessage[] = new byte[Message.MAX_MESSAGE_LENGTH];

			try {
				packet = new DatagramPacket(receiveMessage,
						receiveMessage.length);
				server.getSocket().receive(packet);
				senderAddress = packet.getAddress();
				port = packet.getPort();
				ByteArrayInputStream bis = new ByteArrayInputStream(
						packet.getData());
				ObjectInputStream in = null;
				in = new ObjectInputStream(bis);
				message = (Message) in.readObject();
				message.processMessage();
				

			} catch (Exception e) {

			}

		}

	}

	

	private void stopMultiCastServer() throws IOException {
		multicastServer.stop();
	}

	

	

	

}
