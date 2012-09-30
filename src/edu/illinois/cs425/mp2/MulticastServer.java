package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {

	private boolean isRunning;
	public int multicastPort;


	public int getMulticastPort() {
		return multicastPort;
	}
	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	private MulticastSocket multicastSocket;
	private InetAddress multicastGroup;

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
		} catch(IOException e) {
			System.out.println("Unable to start Multicast Server");
		}
	}

	public synchronized void ensureRunning(InetAddress multicastGroup, int multicastPort) throws IOException {
		// TODO Auto-generated method stub
		if(!isRunning) {
			this.multicastGroup = multicastGroup;
			this.multicastPort = multicastPort;
			this.multicastSocket = new MulticastSocket(multicastPort);
			isRunning = true;
		}
	}

	public synchronized void stop() {
		multicastSocket.close();
		isRunning = false;
	}
}