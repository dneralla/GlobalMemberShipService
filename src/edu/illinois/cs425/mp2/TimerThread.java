package edu.illinois.cs425.mp2;

import java.util.Date;

/*
 * Class spawns thread, which will be used for 
 * detecting node failures.
 */
public class TimerThread extends ServiceThread {

	public TimerThread(Message message) {
		super(message);
	}

	public TimerThread() {
		super();
	}

	@Override
	public void run() {
		while (true) {
			if (System.currentTimeMillis()
					- ProcessorThread.getServer()
							.getLastReceivedHeartBeatTime() > 3 * 1000) {

				System.out.println("failure Detected of node"
						+ ProcessorThread.getServer().getHeartbeatSendingNode()
								.getHostAddress());
				ProcessorThread
						.getServer()
						.getLogger()
						.info("failure Detected of node"
								+ ProcessorThread.getServer()
										.getHeartbeatSendingNode()
										.getHostAddress());
				processFailure(ProcessorThread.getServer()
						.getHeartbeatSendingNode());
				ProcessorThread.toStartHeartBeating = false;

				this.stop();
			}
		}

	}

	public void processFailure(MemberNode node) {
		try {

			MemberNode self = ProcessorThread.getServer().getNode();

			node.setTimeStamp(new Date());
			MulticastFailureMessage message = new MulticastFailureMessage(self,
					self, node);
			ProcessorThread.getMulticastServer().multicastUpdate(message);
			message.mergeIntoMemberList();
			ProcessorThread.getMulticastServer().multicastUpdate(message);
		} catch (Exception e) {
			System.out.println("processing failure failed of node"
					+ node.getHostAddress());
			ProcessorThread
					.getServer()
					.getLogger()
					.info("processing failure failed of node"
							+ node.getHostAddress());
		}
	}

}
