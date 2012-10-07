package edu.illinois.cs425.mp2;

public class MulticastLeaveMessage extends MulticastMessage {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MulticastLeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage() {
		System.out.println("Processing multicast leave message");
		new ServiceThread(this) {
			@Override
			public void run() {
				try {

					ProcessorThread.getServer().getLogger().info("Multicast leave message Processing  of node"+getMessage().getAlteredNode().getHostAddress());


					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage(ProcessorThread
								.getServer().getNode(), getMessage()
								.getSourceNode(), getMessage().getAlteredNode());
						ProcessorThread.getServer().sendMessage(message,
								ProcessorThread.getServer().getNeighborNode());
						if (getMessage().getAlteredNode().compareTo(
								ProcessorThread.getServer().getNeighborNode())) {
							ProcessorThread.getServer().setNeighborNode(
									getMessage().getAlteredNode());
						}

					}

				} catch (Exception e) {
					ProcessorThread.getServer().getLogger().info("Multicast leave message  Processing failed of node"+getMessage().getAlteredNode().getHostAddress());
					e.printStackTrace();
				}
			}
		}.start();

	}

	@Override
	public RelayLeaveMessage getNewRelayMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new RelayLeaveMessage(sourceNode, centralNode, alteredNode);
	}

	public MulticastLeaveMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

}
