package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
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

	TimerTask task;
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
				server.getLogger().info(message.getDescription());
				 ProcessorThread.getServer().getLogger().info("receieved message");

				if (message instanceof HeartBeatMessage) {
					// System.out.println("heartbeat receieved");
					if (!ProcessorThread.toStartHeartBeating) {
						startTimerThread();
						ProcessorThread.toStartHeartBeating = true;
					}
					updateTimer();
				} else { 
					 System.out.println("processing message");
					 ProcessorThread.getServer().getLogger().info("processing message");
					message.processMessage();
				}

			} catch (Exception e) {

			}

		}

	}

	public void updateTimer() {

		/*
		 * ProcessorThread.server.getLastReceivedHeartBeat().cancel();
		 * ProcessorThread
		 * .server.getLastReceivedHeartBeat().schedule(this.task,2*1000);
		 * System.out.println("timertask");
		 */
		ProcessorThread.server.setLastReceivedHeartBeatTime(System
				.currentTimeMillis());
	}

	private void stopMultiCastServer() {
		multicastServer.stop();
	}

	public void onJoinAck(Message message) throws Exception {

		System.out.println("Join Acknowledging");
		ProcessorThread.server.setNeighborNode(new MemberNode(
				message.getHost(), message.getPort()));

		System.out.println("heartbeat setting true");
		ProcessorThread.server.setSendHeartBeat(true);
		ProcessorThread.multicastServer.ensureRunning(
				message.getMulticastGroup(), message.getMulticastPort());

		multicastServer.multicastUpdate(message);
	}

	public void processFailure() {
		System.out.println("failureDetected");
	}

	public void startTimerThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis()
							- ProcessorThread.server
									.getLastReceivedHeartBeatTime() > 5 * 1000) {

						System.out.println("Detected");
						processFailure();
						ProcessorThread.toStartHeartBeating = false;
						this.stop();
					}
				}

			}
		}.start();

	}

}
