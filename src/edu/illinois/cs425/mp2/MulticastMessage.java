package edu.illinois.cs425.mp2;


public abstract class MulticastMessage extends Message {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MulticastMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public abstract RelayMessage getNewRelayMessage();

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage();
						message.setNode(ProcessorThread.getServer().getNode());
						message.setSourceNode(message.getNode());
						ProcessorThread.getServer().sendMessage(message, ProcessorThread.getServer().getNeighborNode());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
