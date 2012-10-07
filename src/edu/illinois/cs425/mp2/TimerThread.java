package edu.illinois.cs425.mp2;

import java.util.Date;

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

				System.out.println("failure Detected of node"+ProcessorThread.getServer().getHeartbeatSendingNode().getHostAddress());
				ProcessorThread.getServer().getLogger().info("failure Detected of node"+ProcessorThread.getServer().getHeartbeatSendingNode().getHostAddress());
				processFailure(ProcessorThread.getServer().getHeartbeatSendingNode());
				ProcessorThread.toStartHeartBeating = false;

			     this.stop();
			} }

	}

		public void processFailure(MemberNode node) {
			try {



			MemberNode self = ProcessorThread.getServer().getNode();
			// TODO: in case of failure detection, altered is faulty node //Get sending nodel
			node.setTimeStamp(new Date());
		
			MulticastFailureMessage message = new MulticastFailureMessage(self, self,node);
			message.mergeIntoMemberList();
	        
			ProcessorThread.getMulticastServer().multicastUpdate(message);
			}
			catch(Exception e)
			{
				System.out.println("processing failure failed of node"+node.getHostAddress());
				ProcessorThread.getServer().getLogger().info("processing failure failed of node"+node.getHostAddress());
			}
		}

}
