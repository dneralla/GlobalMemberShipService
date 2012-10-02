package edu.illinois.cs425.mp2;

public class RelayMessage extends Message {

	public RelayMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {

					Message message = new RelayMessage("RELAYED_STRING");
					message.setNode(ProcessorThread.getServer().getNode());
					ProcessorThread.getMulticastServer().multicastUpdate(
							message);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
