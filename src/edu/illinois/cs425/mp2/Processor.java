package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.TimerTask;

public class Processor {
	private static MemberServer server;
	TimerTask task ;
	
	
	public Processor(MemberServer server) {
         Processor.server=server;
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
				System.out.println("Join message receieved");
				new ServiceThread(senderAddress,port,message)
			  	{  public void run()
			  	    {
			  		try {
			  			
						serviceJoinRequest(this.getAddr(),this.getPort(),this.getMessage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  	    }
			  	}.start();
			}
			else if(message.getMessageType().equals(MessageTypes.HEART_BEAT))
			{
		        //System.out.println("heartbeat receieved");
				updateTimer();
		    }
			else if(message.getMessageType().equals(MessageTypes.LEAVE))
			{
				new Thread()
				{
					public void run()
					{
						serviceLeaveRequest();
					}
				}.start();
			}
			else if (message.getMessageType().equals(MessageTypes.JOIN_ACK))
			{
				System.out.println("Join ack receieved");
				onJoinAck(senderAddress,port,message);
			}
			
		}catch(Exception e)
		{
			
			
		}
		
		

	}
	
	public void updateTimer()
	{
		Processor.server.getLastReceivedHeartBeat().cancel();
		Processor.server.getLastReceivedHeartBeat().schedule(this.task,2*1000);
	}
	
	public void serviceJoinRequest(InetAddress addr, int port, Message message) throws Exception
	{
	    System.out.println("Servicing Join request");
		Processor.server.setNeighborNode(new MemberNode(addr,port));
	    byte [] buf = new Message(MessageTypes.JOIN_ACK).toBytes();
	    DatagramPacket packet = new DatagramPacket(buf,buf.length,addr,port);
	    Processor.server.getSocket().send(packet);
	    //multicast to all servers too
     }
	
	public void onJoinAck(InetAddress addr,int port,Message message)
	{
		System.out.println("Join Acknowledging");
		Processor.server.setNeighborNode(new MemberNode(addr,port));
		Processor.server.setSendHeartBeat(true);
	}
	public void serviceLeaveRequest()
	{
		
	}
	
	public void processFailure()
	{
		System.out.println("failure Detected");
		//TODO update table and multicast
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


