package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {

	private boolean isRunning;
	public int multicastPort;
	private MemberServer server;
	private MulticastSocket multicastSocket;
	private InetAddress multicastGroup;

	public MemberServer getServer() {
		return server;
	}

	public void setServer(MemberServer server) {
		this.server = server;
	}

	public MulticastServer(MemberServer server) {
		this.server = server;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public MulticastServer() {
		isRunning = false;
	}

	public MulticastSocket getMulticastSocket() {
		return multicastSocket;
	}

	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}

	public void start() {
		try {
			new MulticastServerThread(this).start();
		} catch (IOException e) {
			System.out.println("Unable to start Multicast Server");
		}
	}

	public synchronized void ensureRunning(InetAddress multicastGroup,
			int multicastPort) throws IOException {
		// senderAddressTODO Auto-generated method stub
		if (!isRunning) {
			this.multicastGroup = multicastGroup;
			this.multicastPort = multicastPort;
			this.multicastSocket = new MulticastSocket(multicastPort);
			start();
			isRunning = true;
		}
	}

	public synchronized void stop() {
		multicastSocket.close();
		isRunning = false;
	}

	void multicastUpdate(Message multicastMessage) throws Exception, IOException {
	    byte[] bytes = multicastMessage.toBytes();
		DatagramPacket packet =
				new DatagramPacket(bytes, bytes.length, getMulticastGroup(), getMulticastPort());

		ProcessorThread.getServer().getLogger().info(multicastMessage.getDescription());
		getMulticastSocket().send(packet);
	}
}