package edu.illinois.cs425.mp2;

import java.io.IOException;

public class HeartBeatMessage extends Message {

	
	private static final long serialVersionUID = 1L;
	public HeartBeatMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
		
	}

	public HeartBeatMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage() {
		if (!ProcessorThread.toStartHeartBeating) {
			ProcessorThread.getServer().setTimer(new TimerThread(this));
			ProcessorThread.getServer().getTimer().start();
		    ProcessorThread.toStartHeartBeating = true;
		}
		//System.out.println("PORT::"+ getSourceNode().getHostPort());
		updateTimer();
	}

	public void updateTimer() {
        ProcessorThread.getServer().setLastReceivedHeartBeatTime(
				System.currentTimeMillis());
	}

	
}
