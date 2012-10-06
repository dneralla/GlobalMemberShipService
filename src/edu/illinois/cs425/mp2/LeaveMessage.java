package edu.illinois.cs425.mp2;

public class LeaveMessage extends Message {

	public LeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public LeaveMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					System.out.println("Processing Leave message");

					ProcessorThread.getMulticastServer().ensureRunning(getMessage().getMulticastGroup(), getMessage().getMulticastPort());

					MemberNode self = ProcessorThread.getServer().getNode();
					// TODO: in case of failure detection, altered is faulty node
					MulticastLeaveMessage message = new MulticastLeaveMessage(self, self, getMessage().getAlteredNode());
			        ProcessorThread.getMulticastServer().multicastUpdate(message);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
