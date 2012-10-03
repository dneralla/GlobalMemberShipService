package edu.illinois.cs425.mp2;



public class MulticastLeaveMessage extends MulticastMessage {

	public MulticastLeaveMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RelayLeaveMessage getNewRelayMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new RelayLeaveMessage(sourceNode, centralNode, alteredNode);
	}

	public MulticastLeaveMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

}
