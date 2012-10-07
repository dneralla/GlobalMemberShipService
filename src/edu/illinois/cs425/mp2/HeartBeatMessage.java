package edu.illinois.cs425.mp2;
import java.util.Random;

public class HeartBeatMessage extends Message {


     

	private static final long serialVersionUID = 1L;


	public HeartBeatMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	  }

	public HeartBeatMessage(String messageType) {
		super(messageType);
		}

	@Override
	public void processMessage() {
		double loss_rate =0.5;
		Random r= new Random();
		System.out.println(r.nextDouble());
		
	    ProcessorThread.getServer().getLogger().info("Hear tBeatReceieved from host"+this.getSourceNode().getHostAddress().toString());

		if (!ProcessorThread.toStartHeartBeating) {
			ProcessorThread.getServer().setTimer(new TimerThread());
			
			ProcessorThread.getServer().getTimer().start();
		    ProcessorThread.toStartHeartBeating = true;
		}
		if(r.nextDouble()>loss_rate)
		{
	   updateTimer();
		}
	}

	public void updateTimer() {
		if(!(ProcessorThread.getServer().getHeartbeatSendingNode().compareTo(this.getSourceNode())))
		{

			ProcessorThread.getServer().setHeartbeatSendingNode(this.getSourceNode());
			ProcessorThread.toStartHeartBeating=false;
			ProcessorThread.getServer().getTimer().stop();


		}
        ProcessorThread.getServer().setLastReceivedHeartBeatTime(
				System.currentTimeMillis());
	}


}
