package edu.illinois.cs425.mp2;

public class HeartBeatServiceThread extends Thread {

	public HeartBeatServiceThread() {

	}

	@Override
	public void run() {
		try {
			Message m = new HeartBeatMessage(ProcessorThread.getServer()
					.getNode(), null, null);
			while (true) {
               System.out.println(ProcessorThread.getServer().getNeighborNode().getHostAddress().toString());
				ProcessorThread.getServer().sendMessage(m,
						ProcessorThread.getServer().getNeighborNode());
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println("Error in sending hearbeat message");
			e.printStackTrace();
		}
	}
}
