package edu.illinois.cs425.mp2;

public class MulticastFailureMessage extends MulticastMessage {
	private static final long serialVersionUID = 1L;

	@Override
	public void processMessage() {
		System.out.println("Processing multicast failure message");
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage(ProcessorThread
								.getServer().getNode(), getMessage()
								.getSourceNode(), getMessage().getAlteredNode());
						ProcessorThread.getServer().sendMessage(message,
								ProcessorThread.getServer().getNeighborNode());
						if (getMessage().getAlteredNode().compareTo(
								ProcessorThread.getServer().getNeighborNode())) {
							ProcessorThread.getServer().setNeighborNode(
									getMessage().getSourceNode());
						}
					}

				} catch (Exception e) {
					ProcessorThread
							.getServer()
							.getLogger()
							.info("Multicast failure receieve  Processing failed of node"
									+ getMessage().getAlteredNode()
											.getHostAddress());
					System.out
							.println("Multicast failure receieve  Processing failed of node"
									+ getMessage().getAlteredNode()
											.getHostAddress());
				}
			}
		}.start();

	}

	public MulticastFailureMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	@Override
	public RelayFailureMessage getNewRelayMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode) {

		return new RelayFailureMessage(sourceNode, centralNode, alteredNode);
	}

}
