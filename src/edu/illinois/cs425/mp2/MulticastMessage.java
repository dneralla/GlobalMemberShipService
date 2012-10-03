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

	public MulticastMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	public abstract RelayMessage getNewRelayMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode);

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage(ProcessorThread.getServer().getNode(), getMessage().getSourceNode(), getMessage().getAlteredNode());
						ProcessorThread.getServer().sendMessage(message, ProcessorThread.getServer().getNeighborNode());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
