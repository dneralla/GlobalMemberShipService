package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastServer {

	private static final int MULTICAST_LISTENER_PORT = 4446;
    private static final String MULTICAST_GROUP = "230.0.0.1";
	private boolean isRunning;
	private MemberServer server;
	private MulticastSocket multicastListenerSocket;
	private MulticastSocket multicastServerSocket;
	private InetAddress multicastGroup;
	
	public int getMulticastServerPort() {
		return multicastServerSocket.getPort();
	}
	
	public MulticastSocket getMulticastListenerSocket() {
		return multicastListenerSocket;
	}

	public void setMulticastListenerSocket(MulticastSocket multicastListenerSocket) {
		this.multicastListenerSocket = multicastListenerSocket;
	}

	public MulticastSocket getMulticastServerSocket() {
		return multicastServerSocket;
	}

	public void setMulticastServerSocket(MulticastSocket multicastServerSocket) {
		this.multicastServerSocket = multicastServerSocket;
	}

	public MemberServer getServer() {
		return server;
	}

	public void setServer(MemberServer server) {
		this.server = server;
	}

	public MulticastServer(MemberServer server) throws IOException {
		this.server = server;
		try {
			this.multicastGroup = InetAddress.getByName(MULTICAST_GROUP);
			this.multicastServerSocket = new MulticastSocket();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public synchronized void ensureRunning() throws IOException {
		if (!isRunning) {
			try {
			this.multicastServerSocket = new MulticastSocket();
			this.multicastListenerSocket = new MulticastSocket(MULTICAST_LISTENER_PORT);
			this.multicastListenerSocket.joinGroup(multicastGroup);
			start();
			isRunning = true;
			} catch(IOException e)  {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stop() throws IOException {
		multicastListenerSocket.leaveGroup(multicastGroup);
		multicastListenerSocket.close();
		multicastServerSocket.close();
		isRunning = false;
	}

	void multicastUpdate(Message multicastMessage) throws Exception, IOException {

		ProcessorThread.getServer().getLogger().info("Sending multicast update of Node"+multicastMessage.getAlteredNode().getHostAddress());

	    byte[] bytes = multicastMessage.toBytes();
		DatagramPacket packet =
				new DatagramPacket(bytes, bytes.length, getMulticastGroup(), multicastListenerSocket.getLocalPort());
		ProcessorThread.getServer().getSocket().send(packet);
	}
}
