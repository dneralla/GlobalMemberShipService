package edu.illinois.cs425.mp2;

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
			startTimerThread();
			ProcessorThread.toStartHeartBeating = true;
		}
		updateTimer();
	}

	public void updateTimer() {

		/*
		 * ProcessorThread.server.getLastReceivedHeartBeat().cancel();
		 * ProcessorThread
		 * .server.getLastReceivedHeartBeat().schedule(this.task,2*1000);
		 * System.out.println("timertask");
		 */
		ProcessorThread.getServer().setLastReceivedHeartBeatTime(
				System.currentTimeMillis());
	}

	public void startTimerThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis()
							- ProcessorThread.getServer()
									.getLastReceivedHeartBeatTime() > 5 * 1000) {

						System.out.println("Detected");
						processFailure();
						ProcessorThread.toStartHeartBeating = false;
						this.stop();
					}
				}

			}
		}.start();

	}

	public void processFailure() {

	}
}
