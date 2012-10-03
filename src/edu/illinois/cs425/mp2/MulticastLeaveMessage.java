package edu.illinois.cs425.mp2;



public class MulticastLeaveMessage extends MulticastMessage {

	public MulticastLeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RelayLeaveMessage getNewRelayMessage() {
		// TODO Auto-generated method stub
		return new RelayLeaveMessage("RELAY_LEAVE");
	}

}
