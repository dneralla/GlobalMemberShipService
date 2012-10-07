package edu.illinois.cs425.mp2;

import java.util.Random;

public class HeartBeatMessage extends Message {

	private static final long serialVersionUID = 1L;

	public HeartBeatMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	@Override
	public void processMessage() {
		ProcessorThread
				.getServer()
				.getLogger()
				.info("Hear tBeatReceieved from host"
						+ this.getSourceNode().getHostAddress().toString());

		if (!ProcessorThread.toStartHeartBeating) {
			ProcessorThread.getServer().setTimer(new TimerThread());

			ProcessorThread.getServer().getTimer().start();
			ProcessorThread.toStartHeartBeating = true;
		}
		updateTimer();
	}

	public void updateTimer() {
		if (!(ProcessorThread.getServer().getHeartbeatSendingNode()
				.compareTo(this.getSourceNode()))) {

			ProcessorThread.getServer().setHeartbeatSendingNode(
					this.getSourceNode());
			ProcessorThread.toStartHeartBeating = false;
			ProcessorThread.getServer().getTimer().stop();

		}
		ProcessorThread.getServer().setLastReceivedHeartBeatTime(
				System.currentTimeMillis());
	}

}
