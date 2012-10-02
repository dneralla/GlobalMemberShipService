package edu.illinois.cs425.mp2;


public class JoinMessage extends Message {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public JoinMessage(String messageType) {
		super(messageType);
	}

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					System.out.println("Servicing Join request");
					((JoinMessage) getMessage()).mergeIntoMemberList();

					// should change this step (TODO: master may be detected as
					// failure)
					ProcessorThread.getServer().setNeighborNode(
							getMessage().getNode());
					Message ackMessage = new JoinAckMessage("JOIN_ACK");
					ackMessage.setMulticastGroup(ProcessorThread
							.getMulticastServer().getMulticastGroup());
					ackMessage.setMulticastPort(ProcessorThread
							.getMulticastServer().getMulticastPort());

					ProcessorThread.getServer().sendMessage(ackMessage, getMessage().getNode());
					ProcessorThread.getServer().setSendHeartBeat(true);
					// ensure reliability in 5 seconds (TODO)
					// send back the multi-cast group, port number neighbour to
					// new node

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}
}
