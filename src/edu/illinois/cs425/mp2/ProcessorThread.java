package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.TimerTask;

public class ProcessorThread extends Thread {
	private static MemberServer server;
	private static MulticastServer multicastServer;
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
				server.getNode().getSocket().receive(packet);
				senderAddress = packet.getAddress();
				port = packet.getPort();
				ByteArrayInputStream bis = new ByteArrayInputStream(
						packet.getData());
				ObjectInputStream in = null;
				in = new ObjectInputStream(bis);
				message = (Message) in.readObject();
				if (message.getMessageType().equals(MessageTypes.JOIN)) {
					System.out.println("Join message receieved");
					new ServiceThread(senderAddress, port, message) {
						@Override
						public void run() {
							try {
								serviceJoinRequest(this.getAddr(),
										this.getPort(), this.getMessage());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();
				} else if (message.getMessageType().equals(
						MessageTypes.HEART_BEAT)) {
					// System.out.println("heartbeat receieved");
					if (!ProcessorThread.toStartHeartBeating) {
						startTimerThread();
						ProcessorThread.toStartHeartBeating = true;
					}
					updateTimer();
				} else if (message.getMessageType().equals(MessageTypes.LEAVE)) {
					new Thread() {
						@Override
						public void run() {
							serviceLeaveRequest();
						}
					}.start();
				} else if (message.getMessageType().equals(
						MessageTypes.JOIN_ACK)) {
					System.out.println("Join ack receieved, message has multicast info and " +
							"neighbor node");
					onJoinAck(/*senderAddress, port, */message);
				} else if (message.getMessageType().startsWith("MULTICAST")) {
					System.out.println("Need to check whether this (relayed) message has already been received");
					new ServiceThread(senderAddress, port, message) {
						@Override
						public void run() {
							try {
								requestMulticastOnceMore(this.getAddr(),
										this.getPort(), this.getMessage());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();
				}

			} catch (Exception e) {

			}

		}

	}



	protected void requestMulticastOnceMore(InetAddress addr, int port,
			Message message) {
		boolean isChangeMadetoMemberList;
//		if ( isChangeMadetoMemberList = server.mergeMemberList(server.getNode(), message.getMessageType())) {
//		message.setMessageType("RE" + message.getMessageType());
//		DatagramPacket packet = new DatagramPacket(message.toBytes(), Message.MAX_MESSAGE_LENGTH , this.getMulticastServer().getMulticastGroup(), this.getMulticastServer().getMulticastPort());
//		this.getMulticastServer().getMulticastSocket().send(packet);
//		}
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

	public void serviceJoinRequest(InetAddress addr, int port, Message message)
			throws Exception {
		System.out.println("Servicing Join request");
		server.setNeighborNode(new MemberNode(addr, port));

		// merge the list with the port
		server.mergeMemberList(new MemberNode(addr, port), MessageTypes.JOIN);

		Message ackMessage = new Message(MessageTypes.JOIN_ACK);
		ackMessage.setMulticastGroup(multicastServer.getMulticastGroup());
		ackMessage.setMulticastPort(multicastServer.getMulticastPort());
		ackMessage.setMessageType("JOIN_ACK");

		byte[] buf = ackMessage.toBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
		ProcessorThread.server.getSocket().send(packet);
		ProcessorThread.server.setSendHeartBeat(true);


		// ensure reliability in 5 seconds (TODO)
		// send back the multi-cast group, port number neighbour to new node
		ProcessorThread.server.getSocket().send(packet);

	}

	private void ensurReliableMultiCast() {
		// do multicast while there is still some non synchronized server
	}


	private void stopMultiCastServer() {
		multicastServer.stop();
	}

	public void onJoinAck(Message message) throws Exception {

		System.out.println("Join Acknowledging");
		ProcessorThread.server.setNeighborNode(new MemberNode(message.getHost(), message.getPort()));

		System.out.println("heartbeat setting true");
		ProcessorThread.server.setSendHeartBeat(true);
		ProcessorThread.multicastServer.ensureRunning(message.getMulticastGroup(), message.getMulticastPort());

        Message multicastMessage = new Message(MessageTypes.MULTICAST_JOIN);

		byte[] buf = multicastMessage.toBytes();
		DatagramPacket packet =
				new DatagramPacket(buf, buf.length, multicastServer.getMulticastGroup(), multicastServer.getMulticastPort());
		multicastServer.getMulticastSocket().send(packet);
	}

	public void serviceLeaveRequest() {

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
