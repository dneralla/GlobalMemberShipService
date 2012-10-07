package edu.illinois.cs425.mp2;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/*
 * Generic class for handling all messages.
 * Depending on the type(class) of message received,
 * the corresponding processMesmethod is invoked.
 */
public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	static final int MAX_MESSAGE_LENGTH = 1024;
	
	private InetAddress host;
	private int port;
	private int multicastPort;
	private InetAddress multicastGroup;
	private InetAddress originalHost;
	private int originalPort;

	// sourceNode always contains the details of message sender
    // centralNode always contains the details of node who multicasts the update 
	// alteredNode always contains the details of joined/failed/left node
	private MemberNode sourceNode, centralNode, alteredNode;

	public Message(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		this.sourceNode = sourceNode;
		this.centralNode = centralNode;
		this.alteredNode = alteredNode;
	}

	public MemberNode getCentralNode() {
		return centralNode;
	}

	public void setCentralNode(MemberNode centralNode) {
		this.centralNode = centralNode;
	}

	public MemberNode getAlteredNode() {
		return alteredNode;
	}

	public void setAlteredNode(MemberNode alteredNode) {
		this.alteredNode = alteredNode;
	}

	public MemberNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(MemberNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public InetAddress getOriginalHost() {
		return originalHost;
	}

	public void setOriginalHost(InetAddress originalHost) {
		this.originalHost = originalHost;
	}

	public int getOriginalPort() {
		return originalPort;
	}

	public void setOriginalPort(int originalPort) {
		this.originalPort = originalPort;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}

	public void setMulticastGroup(InetAddress multicastGroup) {
		this.multicastGroup = multicastGroup;
	}

	public byte[] toBytes() throws Exception {
		byte[] yourBytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			yourBytes = bos.toByteArray();
		} finally {
			out.close();
			bos.close();
		}

		return yourBytes;
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDescription() {
		return "Source Node : " +  getSourceNode().getDescription()
				+ "Central Node: " + getCentralNode().getDescription()
				+ "Altered Node: " + getAlteredNode().getDescription();
	}

	public boolean checkIsIntructionJoinVariant() {
		return this instanceof JoinMessage
				|| this instanceof MulticastJoinMessage
				|| this instanceof RelayJoinMessage;
	}

	public synchronized boolean mergeIntoMemberList() {

		List<MemberNode> globalList = (ArrayList<MemberNode>) ProcessorThread
				.getServer().getGlobalList();
		boolean isLatestUpdate = false;
		Date timeStamp = getSourceNode().getTimeStamp();
		int index = globalList.indexOf(getAlteredNode());
		MemberNode matchingNode = index == -1 ? null : globalList.get(index);
		if (checkIsIntructionJoinVariant()) {
			if (matchingNode == null
					&&
					/* && checkHasJoinArrivedLate() */
					(ProcessorThread.getServer().getRecentLeftNode() == null
							|| !ProcessorThread.getServer().getRecentLeftNode()
									.equals(getAlteredNode()) || getAlteredNode()
							.getTimeStamp()
							.after(ProcessorThread.getServer()
									.getRecentLeftNode().getTimeStamp()))) {
				globalList.add(getAlteredNode());
				return true;
			} else if (matchingNode.getTimeStamp().before(
					getAlteredNode().getTimeStamp())) {
				matchingNode.setTimeStamp(getAlteredNode().getTimeStamp());
				return true;
			}
		} else {

			if (matchingNode != null
					&& matchingNode.getTimeStamp().before(
							getAlteredNode().getTimeStamp())) {
				globalList.remove(getAlteredNode());
				ProcessorThread.getServer().setRecentLeftNode(getAlteredNode());
				return true;
			} else {
				ProcessorThread.getServer().setRecentLeftNode(getAlteredNode());
			}

		}
		return false;
	}

	public abstract void processMessage();
	// public abstract void mergeIntoMessageList();
}
