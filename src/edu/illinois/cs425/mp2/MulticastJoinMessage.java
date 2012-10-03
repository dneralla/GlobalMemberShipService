package edu.illinois.cs425.mp2;

public class MulticastJoinMessage extends MulticastMessage {

	public MulticastJoinMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RelayMessage getNewRelayMessage() {
		// TODO Auto-generated method stub
		return new RelayJoinMessage("RELAY_JOIN");
	}

}
