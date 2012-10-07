package edu.illinois.cs425.mp2;

/**
 * Class for holding all the properties of server.
 * All these properties are final. This also takes care
 * of managing sockets like opening, closing them.
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
public class MemberNode  implements java.io.Serializable{

	private InetAddress hostAddress;
	private int hostPort;
	private Date timeStamp;

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public MemberNode(InetAddress hostAddress, int hostPort)
	{
		this.hostAddress=hostAddress;
		this.hostPort=hostPort;
		this.timeStamp = new Date();
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

    public MemberNode(String hostName, int hostPort) throws UnknownHostException {
			this(InetAddress.getByName(hostName), hostPort);
	}

	public boolean compareTo(MemberNode node) {
		if(node == null)
			return false;
		if (this.getHostAddress().equals(node.getHostAddress())
				&& this.getHostPort() == node.getHostPort())
			return true;
		return false;
	}
	
	@Override
	public boolean equals(Object node) {
		if( node == null || !(node instanceof MemberNode))
			return false;
		if (hostAddress.equals(((MemberNode) node).getHostAddress())
				&& hostPort == ((MemberNode) node).getHostPort())
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		  int hashCode = 0;
		  hashCode = 31*hashCode + (hostAddress ==null ? 0 : hostAddress.hashCode());
		  hashCode = 31*hashCode + (hostPort);
		  return hashCode;
	}
}
