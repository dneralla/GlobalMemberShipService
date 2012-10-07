package edu.illinois.cs425.mp2;

public abstract class RelayMessage extends Message {

	public RelayMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public RelayMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		// TODO Auto-generated constructor stub
		super(sourceNode, centralNode, alteredNode);
	}

	public abstract Message getNewMulticastMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode);

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				System.out.println("Relaying message");
				try {
					MemberNode self = ProcessorThread.getServer().getNode();
						if(mergeIntoMemberList()) {
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
