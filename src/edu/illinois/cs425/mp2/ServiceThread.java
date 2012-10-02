package edu.illinois.cs425.mp2;


public class ServiceThread extends Thread{
	private final Message message;

	public Message getMessage() {
		return message;
	}

	public ServiceThread(Message message)
	{
		this.message = message;
	}

}
