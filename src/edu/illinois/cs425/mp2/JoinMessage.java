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
					// Pre condition: source node must be populated with the node joined/left/failed
					((JoinMessage) getMessage()).mergeIntoMemberList();

					// should change this step (TODO: master may be detected as
					// failure)
					ProcessorThread.getServer().setNeighborNode(
							getMessage().getSourceNode());
					Message ackMessage = new JoinAckMessage(ProcessorThread.getServer().getNode(), null, null);

					((JoinAckMessage)ackMessage).setNeighbourNode(ProcessorThread.getServer().getNeighborNode());
					ackMessage.setMulticastGroup(ProcessorThread
							.getMulticastServer().getMulticastGroup());
					ackMessage.setMulticastPort(ProcessorThread
							.getMulticastServer().getMulticastPort());

					ProcessorThread.getServer().sendMessage(ackMessage, getMessage().getSourceNode());
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
