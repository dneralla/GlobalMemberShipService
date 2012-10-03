package edu.illinois.cs425.mp2;

public class RelayJoinMessage extends RelayMessage {

	public RelayJoinMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message getNewMulticastMessage() {
		// TODO Auto-generated method stub
		return new MulticastJoinMessage("MULTICAST_JOIN");
	}

}
