package edu.illinois.cs425.mp2;

public class RelayFailureMessage extends RelayMessage {
	@Override
	public Message getNewMulticastMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new MulticastFailureMessage(sourceNode, centralNode, alteredNode);
	}

	public RelayFailureMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

}
