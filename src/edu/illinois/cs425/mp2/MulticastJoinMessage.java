package edu.illinois.cs425.mp2;

public class MulticastJoinMessage extends MulticastMessage {


	public MulticastJoinMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	public MulticastJoinMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RelayMessage getNewRelayMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		// TODO Auto-generated method stub
		RelayJoinMessage message = new RelayJoinMessage(sourceNode, centralNode, alteredNode);
		return message;
	}

}
