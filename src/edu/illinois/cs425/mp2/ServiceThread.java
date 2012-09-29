package edu.illinois.cs425.mp2;

import java.net.InetAddress;

public class ServiceThread extends Thread{
	
	
	private InetAddress addr;
	
	private int port;
	
	private Message message;
	
	
	public InetAddress getAddr() {
		return addr;
	}

   public int getPort()
   {
	   return port;
   }
	
  public Message getMessage()
  {
	  return message;
  }

	public ServiceThread(InetAddress addr,int port,Message message )
	{
		this.addr=addr;
		this.port=port;
		this.message=message;
	}
  
}
