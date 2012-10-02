package edu.illinois.cs425.mp2;

public class LeaveMessage extends Message {

	public LeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					System.out.println("Processing Leave message");

					ProcessorThread.getMulticastServer().ensureRunning(getMessage().getMulticastGroup(), getMessage().getMulticastPort());

					MulticastLeaveMessage message = new MulticastLeaveMessage("MUTLICAST_LEAVE");
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
