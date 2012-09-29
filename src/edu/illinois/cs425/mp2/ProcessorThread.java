package edu.illinois.cs425.mp2;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.TimerTask;

public class ProcessorThread extends Thread {
	private static MemberServer server;
	TimerTask task ;
	static boolean toStartHeartBeating=false;


	public ProcessorThread(MemberServer server) {
         ProcessorThread.server=server;
        
         }




    @Override
	public void run() {
        
		DatagramPacket packet;
		Message message;
		InetAddress senderAddress;
		int port;
		while (true)
		{
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
			  	{  @Override
				public void run()
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
				if(!ProcessorThread.toStartHeartBeating)
					{
					startTimerThread();
					ProcessorThread.toStartHeartBeating=true;
					}
				updateTimer();
		    }
			else if(message.getMessageType().equals(MessageTypes.LEAVE))
			{
				new Thread()
				{
					@Override
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

	}

	public void updateTimer()
	{  
	
		/*ProcessorThread.server.getLastReceivedHeartBeat().cancel();
		ProcessorThread.server.getLastReceivedHeartBeat().schedule(this.task,2*1000);
	    System.out.println("timertask");*/
		ProcessorThread.server.setLastReceivedHeartBeatTime(System.currentTimeMillis());
	}

	public void serviceJoinRequest(InetAddress addr, int port, Message message) throws Exception
	{
	    System.out.println("Servicing Join request");
		server.setNeighborNode(new MemberNode(addr,port));
	    byte [] buf = new Message(MessageTypes.JOIN_ACK).toBytes();
	    DatagramPacket packet = new DatagramPacket(buf,buf.length,addr,port);
	    ProcessorThread.server.getSocket().send(packet);
	    
	    ProcessorThread.server.setSendHeartBeat(true);
	    //multicast to all servers too
     }

	public void onJoinAck(InetAddress addr,int port,Message message)
	{
		System.out.println("Join Acknowledging");
		ProcessorThread.server.setNeighborNode(new MemberNode(addr,port));
		System.out.println("heartbeat setting true");
		ProcessorThread.server.setSendHeartBeat(true);
	}
	public void serviceLeaveRequest()
	{

	}

	public void processFailure()
	{
	
		System.out.println("failureDetected");
	}

	
	
	public void startTimerThread()
    {
            new Thread(){
                    public void run()
                    {
                       while(true)
                       {
                               if(System.currentTimeMillis()-ProcessorThread.server.getLastReceivedHeartBeatTime() >5*1000)
                               {  
                            	  
                                 System.out.println("Detected");
                                 processFailure();
                                 ProcessorThread.toStartHeartBeating=false;
                                 this.stop();
                               }}
                           
                      }
            }.start();

    }
	
}


