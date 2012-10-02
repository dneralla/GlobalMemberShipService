package edu.illinois.cs425.mp2;

public class JoinAckMessage extends Message {

	private MemberNode neighbourNode;

	public MemberNode getNeighbourNode() {
		return neighbourNode;
	}

	public void setNeighbourNode(MemberNode neighbourNode) {
		this.neighbourNode = neighbourNode;
	}

	public JoinAckMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					System.out.println("Join Acknowledging");
					ProcessorThread.getServer().setNeighborNode(((JoinAckMessage)getMessage()).getNeighbourNode());

					System.out.println("heartbeat setting true");
					ProcessorThread.getServer().setSendHeartBeat(true);
					ProcessorThread.getMulticastServer().ensureRunning(getMessage().getMulticastGroup(), getMessage().getMulticastPort());

					MulticastJoinMessage message = new MulticastJoinMessage("MUTLICAST_JOIN");
					message.setNode(ProcessorThread.getServer().getNode());
					message.setSourceNode(ProcessorThread.getServer().getNode());
			        ProcessorThread.getMulticastServer().multicastUpdate(message);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
