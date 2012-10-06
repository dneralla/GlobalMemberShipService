package edu.illinois.cs425.mp2;

public class TimerThread extends ServiceThread{

	public TimerThread(Message message)
	{
		super(message);
	}
	public TimerThread()
	{
		super();
	}
	@Override
	public void run()
	{
		while (true) {
			if (System.currentTimeMillis()
					- ProcessorThread.getServer()
							.getLastReceivedHeartBeatTime() > 5 * 1000) {

				System.out.println("Detected");

				processFailure(ProcessorThread.getServer().getHeartbeatSendingNode());
				ProcessorThread.toStartHeartBeating = false;

			     this.stop();
			} }

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
