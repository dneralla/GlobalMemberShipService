package edu.illinois.cs425.mp2;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

public abstract class Message implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static final int MAX_MESSAGE_LENGTH = 1024;

	private String messageType;
	private InetAddress host;
	private int port;
	private int multicastPort;
	private InetAddress multicastGroup;
	private InetAddress originalHost;
	private int originalPort;

	private MemberNode node, sourceNode, predecessorNode;

	public MemberNode getNode() {
		return node;
	}

	public void setNode(MemberNode node) {
		this.node = node;
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

	// Can be JOIN ,LEAVE,HEARTBEAT
	// private InetAddress host;
	// private int port;

	public Message(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
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
		// TODO Auto-generated method stub
		return getMessageType() + " ";
	}

	public boolean checkIsIntructionJoinVariant() {
		return this instanceof JoinMessage ||
			   this instanceof MulticastJoinMessage;
	}
	public boolean mergeIntoMemberList() {
		List<MemberNode> globalList = ProcessorThread.getServer()
				.getGlobalList();
		boolean isLatestUpdate = false, isNewEntry = true;
		Date timeStamp = getNode().getTimeStamp();
		for (MemberNode member : globalList) {
			if (member.compareTo(getNode())) {
				isNewEntry = false;
				if (timeStamp.after(member.getTimeStamp())) {
					if (checkIsIntructionJoinVariant()) {
						member.setTimeStamp(getNode().getTimeStamp());
					} else if (checkIsIntructionJoinVariant()) {
						globalList.remove(member);
					}
					isLatestUpdate = true;
				}
			}
		}
		// missing out the case where join message appears after leave message
		if (isNewEntry) {
			globalList.add(getNode());
			isLatestUpdate = true;
		}
		return isLatestUpdate;
	}
	 public abstract void processMessage();
	// public abstract void mergeIntoMessageList();
}
