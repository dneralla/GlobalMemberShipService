package edu.illinois.cs425.mp2;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;

public class Message implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static final int MAX_MESSAGE_LENGTH=1024;

	private String messageType;
	private InetAddress host;
	private int port;
	private int multicastPort;
	private InetAddress multicastGroup;
    public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}

	public void setMulticastGroup(InetAddress multicastGroup) {
		this.multicastGroup = multicastGroup;
	}

	//Can be JOIN ,LEAVE,HEARTBEAT
	//private InetAddress host;
	//private int port;

	public Message(String messageType)
    {
	  this.messageType=messageType;
	}

	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public byte[] toBytes() throws Exception
	{
		byte[] yourBytes=null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);
		  out.writeObject(this);
		  yourBytes = bos.toByteArray();
		} finally {
			out.close();
			bos.close();
		}

		return yourBytes;
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
