package edu.illinois.cs425.mp2;

import java.util.Date;

public class TimerThread extends ServiceThread{
	
	public TimerThread(Message message)
	{
		super(message);
	}
	
	@Override
	public void run()
	{
		while (true) {
			if (System.currentTimeMillis()
					- ProcessorThread.getServer()
							.getLastReceivedHeartBeatTime() > 5 * 1000) {

				System.out.println("Detected");
				
				processFailure(getMessage().getSourceNode());
				ProcessorThread.toStartHeartBeating = false;
				
			     this.stop();
			} }
		
	}
		
		public void processFailure(MemberNode node) {
			try {
				
			System.out.println("Processing Failure message");

			MemberNode self = ProcessorThread.getServer().getNode();
			// TODO: in case of failure detection, altered is faulty node //Get sending nodel
			node.setTimeStamp(new Date());
		
			MulticastFailureMessage message = new MulticastFailureMessage(self, self,node);
			message.mergeIntoMemberList();
	        
			ProcessorThread.getMulticastServer().multicastUpdate(message);
			}
			catch(Exception e)
			{
				System.out.println("processing failure failed.");
			}
		}

}
