package edu.illinois.cs425.mp2;

public class RelayJoinMessage extends RelayMessage {

	public RelayJoinMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public RelayJoinMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode)  {
		super(sourceNode, centralNode, alteredNode);
	}
	@Override
	public Message getNewMulticastMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new MulticastJoinMessage(sourceNode, centralNode, alteredNode);
	}

}
