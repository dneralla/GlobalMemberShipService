package edu.illinois.cs425.mp2;
import java.io.Serializable;
import java.net.InetAddress;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static final int MAX_MESSAGE_LENGTH=1024;
	
	private String messageType; //Can be JOIN ,LEAVE,HEARTBEAT
	private InetAddress host;
	private InetAddress port;
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
	
	
	
	
	
}
