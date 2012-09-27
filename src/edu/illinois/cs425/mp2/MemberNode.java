package edu.illinois.cs425.mp2;

import java.net.InetAddress;
import java.net.UnknownHostException;
public class MemberNode {
	private InetAddress hostAddress;
	private int hostPort;

	public MemberNode(InetAddress hostAddress, int hostPort)
	{
		this.hostAddress=hostAddress;
		this.hostPort=hostPort;
	}

	public InetAddress getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(InetAddress hostAddress) {
		this.hostAddress = hostAddress;
	}
	public int getHostPort() {
		return hostPort;
	}
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

    public MemberNode(String hostName, int hostPort) {
    	try {
			this.hostAddress = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			System.out.println("Error: Hostname unknown");
			e.printStackTrace();
		}
	}
}
