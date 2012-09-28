package edu.illinois.cs425.mp2;

public class MemberServiceThread extends Thread {
private MemberServer memberServer;

public MemberServiceThread(MemberServer server)
{
	this.setMemberServer(server);
}

public MemberServer getMemberServer() {
	return memberServer;
}

public void setMemberServer(MemberServer memberServer) {
	this.memberServer = memberServer;
}


	
	
	
}
