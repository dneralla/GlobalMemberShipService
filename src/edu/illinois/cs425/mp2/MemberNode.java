package edu.illinois.cs425.mp2;

/**
 * Class for holding all the properties of server.
 * All these properties are final. This also takes care
 * of managing sockets like opening, closing them.
 */
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
public class MemberNode {
	private InetAddress hostAddress;
	private int hostPort;
	private DatagramSocket socket;
	private Date timeStamp;

	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public DatagramSocket getSocket() {
			return socket;
	}
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

    private MemberNode(String hostName, int hostPort) {
    	try {
			this.hostAddress = InetAddress.getByName(hostName);
			this.hostPort = hostPort;
		} catch (UnknownHostException e) {
			System.out.println("Error: Hostname unknown");
			e.printStackTrace();
		}
	}

    /*
     * Starts the sockets on all the ports. All initialization steps must
     * go here.
     */
    public static MemberNode start(String hostName, int hostPort) throws SocketException {
		MemberNode node = new MemberNode(hostName, hostPort);
        node.socket = new DatagramSocket(hostPort);
        //start multicast socket here
        return node;
	}

    public void stopNode() {
    	socket.close();
    }

	public boolean compareTo(MemberNode node) {
		if (this.getHostAddress().equals(node.getHostAddress())
				&& this.getHostPort() == node.getHostPort())
			return true;
		return false;
	}
}
