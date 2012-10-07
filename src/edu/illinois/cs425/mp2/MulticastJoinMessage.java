package edu.illinois.cs425.mp2;

public class MulticastJoinMessage extends MulticastMessage {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MulticastJoinMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	@Override
	public void processMessage()
	{

		new ServiceThread(this) {

			@Override
			public void run() {
				try {
					ProcessorThread.getServer().getLogger().info("Multicast join message Processing of node"+getMessage().getAlteredNode().getHostAddress());
					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage(ProcessorThread.getServer().getNode(), getMessage().getSourceNode(), getMessage().getAlteredNode());
						ProcessorThread.getServer().sendMessage(message, ProcessorThread.getServer().getNeighborNode());
					}
				} catch (Exception e) {
					e.printStackTrace();
					ProcessorThread.getServer().getLogger().info("Multicast Join Message processing failed  of node"+getMessage().getAlteredNode().getHostAddress());
				}
			}
		}.start();
	}


	public MulticastJoinMessage(String messageType) {
		super(messageType);

		// TODO Auto-generated constructor stub
	}

	@Override
	public RelayMessage getNewRelayMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		// TODO Auto-generated method stub

		RelayJoinMessage message = new RelayJoinMessage(sourceNode, centralNode, alteredNode);
		return message;
	}

}
