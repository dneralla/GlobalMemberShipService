package edu.illinois.cs425.mp2;





public class JoinMessage extends Message {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public JoinMessage(String messageType) {
		super(messageType);
	}

	public JoinMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}



  

	
	@Override
	public void processMessage() {
		System.out.println("Processign message");
		new ServiceThread(this) {


			@Override
			public void run() {
				try {
					ProcessorThread.getServer().getLogger().info("Servicing Join request of node"+getMessage().getSourceNode().getHostAddress());
					// Pre condition: source node must be populated with the node joined/left/failed
					((JoinMessage) getMessage()).mergeIntoMemberList();

					MemberNode oldNeighbourNode = ProcessorThread.getServer().getNeighborNode();
					ProcessorThread.getServer().setNeighborNode(
							getMessage().getSourceNode());
					//ProcessorThread.toStartHeartBeating=false;
					//ProcessorThread.getServer().getTimer().stop();
					Message ackMessage = new JoinAckMessage(ProcessorThread.getServer().getNode(), null, null);

					((JoinAckMessage)ackMessage).setNeighbourNode(oldNeighbourNode);
					((JoinAckMessage)ackMessage).setGlobalList(ProcessorThread.getServer().getGlobalList());

					
                   ProcessorThread.getServer().sendMessage(ackMessage, getMessage().getSourceNode());
					

				} catch (Exception e) {

					ProcessorThread.getServer().getLogger().info("Processing join failed of node"+getMessage().getSourceNode().getHostAddress());
                    System.out.println("Processing join failed");
				}
			}
		}.start();

	}
}
