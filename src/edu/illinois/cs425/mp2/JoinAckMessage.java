package edu.illinois.cs425.mp2;

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


	@Override
	public void processMessage() {
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					//System.out.println("Join Acknowledging");
					ProcessorThread.getServer().getLogger().info("Join Acknowledging and updating neighbor as "+((JoinAckMessage)getMessage()).getNeighbourNode().getHostAddress());
					ProcessorThread.getServer().setNeighborNode(((JoinAckMessage)getMessage()).getNeighbourNode());
				    //System.out.println("In request"+((JoinAckMessage)getMessage()).getNeighbourNode().getHostPort());
					//System.out.println("Join ack neighbor"+ ProcessorThread.getServer().getNeighborNode().getHostPort());
					//ProcessorThread.toStartHeartBeating=false;
					//ProcessorThread.getServer().getTimer().stop();
					ProcessorThread.getServer().getLogger().info("Neighbour node in Join Ack message is: "+ ((JoinAckMessage)getMessage()).getNeighbourNode().getHostPort());
                    ProcessorThread.getServer().setGlobalList(getGlobalList());


					ProcessorThread.getMulticastServer().ensureRunning(getMessage().getMulticastGroup(), getMessage().getMulticastPort());
					MemberNode self = ProcessorThread.getServer().getNode();
					MulticastJoinMessage message = new MulticastJoinMessage(self,self,self);

					ProcessorThread.getMulticastServer().multicastUpdate(message);

				} catch (Exception e) {
				 ProcessorThread.getServer().getLogger().info("Updating neighbor or multicasting update failed");
				 System.out.println("updating neighbor failed");
				}
			}
		}.start();

	}

}
