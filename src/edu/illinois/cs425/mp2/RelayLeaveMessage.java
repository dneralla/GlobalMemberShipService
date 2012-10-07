package edu.illinois.cs425.mp2;

public class RelayLeaveMessage extends RelayMessage {
	@Override
	public Message getNewMulticastMessage(MemberNode sourceNode,
			MemberNode centralNode, MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new MulticastLeaveMessage(sourceNode, centralNode, alteredNode);
	}

	public RelayLeaveMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}
}
