package edu.illinois.cs425.mp2;

import java.io.IOException;

public class HeartBeatMessage extends Message {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public HeartBeatMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage() {
		if (!ProcessorThread.toStartHeartBeating) {
			startTimerThread(this);
			ProcessorThread.toStartHeartBeating = true;
		}
		updateTimer();
	}

	public void updateTimer() {
        ProcessorThread.getServer().setLastReceivedHeartBeatTime(
				System.currentTimeMillis());
	}

	public void startTimerThread(Message message) {
		new ServiceThread(message) {
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis()
							- ProcessorThread.getServer()
									.getLastReceivedHeartBeatTime() > 5 * 1000) {

						System.out.println("Detected");
						
						processFailure(getMessage().getSourceNode());
						
						ProcessorThread.toStartHeartBeating = false;
						this.stop();
					}
				}

			}
		}.start();

	}

	public void processFailure(MemberNode node) {
		try {
		System.out.println("Processing Failure message");

		ProcessorThread.getMulticastServer().ensureRunning(ProcessorThread.getMulticastServer().getMulticastGroup(), ProcessorThread.getMulticastServer().getMulticastPort());

		MemberNode self = ProcessorThread.getServer().getNode();
		// TODO: in case of failure detection, altered is faulty node //Get sending nodel
		MulticastFailureMessage message = new MulticastFailureMessage(self, self,node);
        ProcessorThread.getMulticastServer().multicastUpdate(message);
		}
		catch(Exception e)
		{
			System.out.println("processing failure failed.");
		}
	}
}
