package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.TimerTask;

public class Processor {
	private final MemberServer server;
	TimerTask task ;
	
	
	public Processor(MemberServer server) {
         this.server=server;
         initializeTimerTask();
        
         }
	

	
	
	
	public void run() {
		
		DatagramPacket packet;
		Message message;
		InetAddress senderAddress;
		int port;
		byte receiveMessage[] = new byte[Message.MAX_MESSAGE_LENGTH];
		
		try
		{
			packet=new DatagramPacket(receiveMessage,receiveMessage.length);
			server.getNode().getSocket().receive(packet);
			senderAddress = packet.getAddress();
			port = packet.getPort();
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
			ObjectInputStream in = null;
		    in = new ObjectInputStream(bis);
		    message = (Message)in.readObject();
			if(message.getMessageType().equals(MessageTypes.JOIN))
			{
			  	new Thread()
			  	{  public void run()
			  	    {
			  		serviceJoinRequest();
			  	     }
			  	}.start();
			}
			else if(message.getMessageType().equals(MessageTypes.HEART_BEAT))
			{
		        updateTimer();
		      
			   
			}
			else if(message.getMessageType().equals(MessageTypes.LEAVE))
			{
				
			}
			
			
			
			}
		catch(Exception e)
		{
			
			
		}
		
		

	}
	
	public void updateTimer()
	{
		this.server.getLastReceivedHeartBeat().cancel();
		this.server.getLastReceivedHeartBeat().schedule(this.task,2*1000);
	}
	
	public void serviceJoinRequest()
	{
		
	}
	
	public void processFailure()
	{
		
	}
	
	public void initializeTimerTask()
	{
		this.task = new TimerTask(){
			public void run()
			{
				System.out.println("failure Detected");
				processFailure();
			}
		};
		
	}
}


