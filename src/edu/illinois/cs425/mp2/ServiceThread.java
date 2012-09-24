package edu.illinois.cs425.mp2;

public class ServiceThread extends Thread{
	
	private MemberShipService ms;
	public ServiceThread(MemberShipService ms)
	{
		this.setMs(ms);
	}
	public MemberShipService getMs() {
		return ms;
	}
	public void setMs(MemberShipService ms) {
		this.ms = ms;
	}

}
