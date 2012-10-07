package edu.illinois.cs425.mp2;

public abstract class RelayMessage extends Message {

	public RelayMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	public abstract Message getNewMulticastMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode);

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				System.out.println("Relaying message");
				try {
					MemberNode self = ProcessorThread.getServer().getNode();
					if (mergeIntoMemberList()) {
						Message message = getNewMulticastMessage(self,
								getMessage().getCentralNode(), getMessage()
										.getAlteredNode());
						ProcessorThread.getServer().sendMessage(message,
								message.getSourceNode());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
