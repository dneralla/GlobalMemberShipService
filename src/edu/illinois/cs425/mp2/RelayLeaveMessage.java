package edu.illinois.cs425.mp2;

public class RelayLeaveMessage extends RelayMessage {

	public RelayLeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message getNewMulticastMessage() {
		// TODO Auto-generated method stub
		return new MulticastLeaveMessage("MULTICAST_LEAVE");
	}
}
