package edu.illinois.cs425.mp2;

public class LeaveMessage extends Message {

	public LeaveMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {

					ProcessorThread
							.getServer()
							.getLogger()
							.info("Processing Leave message"
									+ getMessage().getAlteredNode()
											.getHostAddress());
					mergeIntoMemberList();

					MemberNode self = ProcessorThread.getServer().getNode();
					// TODO: in case of failure detection, altered is faulty
					// node
					MulticastLeaveMessage message = new MulticastLeaveMessage(
							self, self, getMessage().getAlteredNode());
					ProcessorThread.getServer().setRecentLeftNode(
							getAlteredNode());
					ProcessorThread.getMulticastServer().multicastUpdate(
							message);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					ProcessorThread
							.getServer()
							.getLogger()
							.info("Leave Message processing failed or multicast update failed of node"
									+ getMessage().getAlteredNode()
											.getHostAddress());
					e.printStackTrace();
				}
			}
		}.start();

	}

}
