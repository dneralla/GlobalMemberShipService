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
		System.out.println("Processing multicast join message");
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
