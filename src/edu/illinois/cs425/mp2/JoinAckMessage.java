package edu.illinois.cs425.mp2;

import java.util.Date;
import java.util.List;

public class JoinAckMessage extends Message {

	private MemberNode neighbourNode;
	private List<MemberNode> globalList;

	public List<MemberNode> getGlobalList() {
		return globalList;
	}
	
	public void setGlobalList(List<MemberNode> globalList) {
		this.globalList = globalList;
	}

	public MemberNode getNeighbourNode() {
		return neighbourNode;
	}

	public void setNeighbourNode(MemberNode neighbourNode) {
		this.neighbourNode = neighbourNode;
	}

	public JoinAckMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}

	public JoinAckMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}

	public void mergeIntoMemberList(MemberNode node) {
		List<MemberNode> globalList = ProcessorThread.getServer()
				.getGlobalList();
		for (MemberNode member : globalList) {
			if (member.compareTo(node)) {
				globalList.add(getSourceNode());
            }
		}
	}
	
	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					System.out.println("Join Acknowledging");
					ProcessorThread.getServer().setNeighborNode(((JoinAckMessage)getMessage()).getNeighbourNode());
					ProcessorThread.toStartHeartBeating=false;
					ProcessorThread.getServer().getTimer().stop();
					ProcessorThread.getServer().getLogger().info("Neighbour node in Join Ack message is: "+ ((JoinAckMessage)getMessage()).getNeighbourNode().getHostPort());
                    for(MemberNode node: getGlobalList()) {
                    	mergeIntoMemberList(node);
                    }
                    
					System.out.println("heartbeat setting true");
					//ProcessorThread.getServer().setSendHeartBeat(true);
					ProcessorThread.getMulticastServer().ensureRunning(getMessage().getMulticastGroup(), getMessage().getMulticastPort());
					MemberNode self = ProcessorThread.getServer().getNode();
					MulticastJoinMessage message = new MulticastJoinMessage(self,self,self);

					ProcessorThread.getMulticastServer().multicastUpdate(message);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
