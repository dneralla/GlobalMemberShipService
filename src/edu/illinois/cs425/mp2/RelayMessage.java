package edu.illinois.cs425.mp2;

public abstract class RelayMessage extends Message {

	public RelayMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public abstract Message getNewMulticastMessage();

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {

					Message message = getNewMulticastMessage();
					message.setNode(ProcessorThread.getServer().getNode());
					ProcessorThread.getServer().sendMessage(message, message.getSourceNode());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
