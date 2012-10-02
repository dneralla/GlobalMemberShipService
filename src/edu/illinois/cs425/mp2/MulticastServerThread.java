package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServerThread extends Thread {
	private final MulticastServer multicastServer;

	public MulticastServerThread(MulticastServer multicastServer)
			throws IOException {
		super("MulticastServerThread");
		this.multicastServer = multicastServer;
	}

	@Override
	public void run() {
		DatagramPacket packet;
		Message message;
		InetAddress groupAddress = multicastServer.getMulticastGroup();
		MulticastSocket socket = multicastServer.getMulticastSocket();
		int port = multicastServer.getMulticastPort();
		boolean keepListening = true;
		while (keepListening) {
			byte receiveMessage[] = new byte[Message.MAX_MESSAGE_LENGTH];
			try {
				packet = new DatagramPacket(receiveMessage,
						receiveMessage.length);
				socket.receive(packet);
				ByteArrayInputStream bis = new ByteArrayInputStream(
						packet.getData());
				ObjectInputStream in = null;
				in = new ObjectInputStream(bis);
				message = (Message) in.readObject();
				multicastServer.getServer().getLogger().info(message.getDescription());
				if (message.getMessageType().startsWith("MULTICAST")) {
					multicastServer.getServer()
							.mergeMemberList(
									new MemberNode(message.getHost(),
											message.getPort()),
									message.getMessageType());
					// setting the source host and port, which will be used in case re multicast request is issued.
					message.setOriginalHost(packet.getAddress());
					message.setOriginalPort(packet.getPort());
					// relaying the server received by multi-cast to neighbor node
					multicastServer.getServer().getSocket().send(new DatagramPacket(message.toBytes(), Message.MAX_MESSAGE_LENGTH,
							multicastServer.getServer().getNeighborNode().getHostAddress(), multicastServer.getServer().getNeighborNode().getHostPort()));
				} else {
					if (message.getMessageType().equals(
							MessageTypes.REMULTICAST_JOIN)) {
						message.setMessageType("MULTICAST_JOIN");
					} else
						message.setMessageType("MULTICAST_LEAVE");

					System.out
							.println("Re-multicasting the earlier join/leave message requested");
					new DatagramPacket(message.toBytes(),
							Message.MAX_MESSAGE_LENGTH, groupAddress, port);
					multicastServer.getServer().getLogger().info(message.getDescription());
					socket.send(packet);
				}
			} catch (IOException e) {

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
