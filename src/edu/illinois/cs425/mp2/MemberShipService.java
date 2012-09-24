package edu.illinois.cs425.mp2;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.net.MulticastSocket;


public class MemberShipService {

//node information
private MemberNode memberNode;
private DatagramSocket memberSocket;
private MulticastSocket multicastSocket;

public MulticastSocket getMulticastSocket() {
	return multicastSocket;
}


public void setMulticastSocket(MulticastSocket multicastSocket) {
	this.multicastSocket = multicastSocket;
}



//TODO
private MemberNode joinNode=null;
@SuppressWarnings("unused")
private MemberNode multicastGroup=null;
private MemberNode heartBeatReceiver;
private MemberNode heartBeatSending;

//TODO
private Logger logger=null;


//HashMap to store states of other node
HashMap<InetAddress,String> state;



public MemberShipService(InetAddress hostAddress,int port,MemberNode multicastGroup)
{
  this.memberNode= new MemberNode(hostAddress,port);
  this.multicastGroup=multicastGroup;
	
}



//Timeout 
static final int TIMEOUT=2;


//getter setters

public MemberNode getHeartBeatReceiver() {
	return heartBeatReceiver;
}


public void setHeartBeatReceiver(MemberNode heartBeatReceiver) {
	this.heartBeatReceiver = heartBeatReceiver;
}
public synchronized DatagramSocket getMemberSocket() {
	return memberSocket;
}
public MemberNode getHeartBeatSending() {
	return heartBeatSending;
}


public void setHeartBeatSending(MemberNode heartBeatSending) {
	this.heartBeatSending = heartBeatSending;
}

// member functions
public void joinGroupRequest() throws IOException
{
    //TODO Send the nodeinformation to the joinserver to join	
	byte [] b =this.nodeToBytes(this.memberNode);
	DatagramPacket packet = new DatagramPacket(b,b.length,joinNode.getHostAddress(),joinNode.getHostPort());
	getMemberSocket().send(packet);
	
	
}

//If this is a join node
public void serviceJoinGroup(MemberNode node) 
{
	try {
    if(node!=null) 
    {
    	boolean b =(this.memberNode).equals(this.getHeartBeatReceiver());
    	this.setHeartBeatReceiver(node);
    	if(b)
    	{
    	
        new ServiceThread(this)
    	{
    		public void run()
    		{
    		  while(true)
    		  {
    			  try {
					sendHeartBeat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		}
    	}.start();
    	
    	new ServiceThread(this)
    	{
    		public void run()
    		{
    		  while(true)
    		  {
    			  try {
					receiveHeartBeat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		}
    	}.start();
    	
    }
    	
    	sendHeartBeatInfo(node,this.memberNode);
    
    
    }   }catch(IOException e)
          {  //TODO
    	     e.printStackTrace();
          }
	
	
}
public void leaveGroupRequest() throws IOException
{
	//TODO Send heartBeatSendingNode instead of dummy leave message
	byte [] b= this.nodeToBytes(this.getHeartBeatSending());
	DatagramPacket packet = new DatagramPacket(b,b.length,this.getHeartBeatReceiver().getHostAddress(),this.getHeartBeatReceiver().getHostPort());
	getMemberSocket().send(packet);
}

public void serveLeaveRequest(MemberNode node) throws IOException
{
	byte [] b  =new byte[40];
	DatagramPacket packet =new DatagramPacket(b,40);
	getMemberSocket().receive(packet);
	//TODO add actual message
	 //TODO selfupdate
	this.setHeartBeatSending(this.bytesToNode(b));
	sendLeaveMulticastUpdate(b);
}

public void sendLeaveMulticastUpdate(byte [] b) throws IOException
{
   	//TODO
	DatagramPacket packet = new DatagramPacket(b,b.length,this.multicastGroup.getHostAddress(),this.multicastGroup.getHostPort());
	getMemberSocket().send(packet);
	
	
}

public void receiveMulticastupdate() throws IOException
{
	//TODO update state table
	byte[] b = new byte[40];
	DatagramPacket packet =new DatagramPacket(b,40);
	this.multicastSocket.receive(packet);
	
	if(this.getHeartBeatReceiver()) //if failure or left node neighbor update neighbor to multicasting node
	
}

public void sendHeartBeatInfo(MemberNode toNode, MemberNode neighborNode) throws IOException
{
	byte b[] = nodeToBytes(neighborNode);
	DatagramPacket packet = new DatagramPacket(b,b.length,toNode.getHostAddress(),toNode.getHostPort());
	getMemberSocket().send(packet);
	
}
  public void receieveHeartBeatInfo() throws IOException{
	 byte[] buffer = new byte[HeartBeatMessage.MAX_LEN];//TODO MAX_LEN USAGE
	   DatagramPacket packet = new DatagramPacket(buffer,HeartBeatMessage.MAX_LEN);
	    getMemberSocket().receive(packet);
	    this.setHeartBeatReceiver(this.bytesToNode(buffer));
	    new ServiceThread(this)
    	{
    		public void run()
    		{
    		  while(true)
    		  {
    			  try {
					sendHeartBeat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		}
    	}.start();
    	
    	new ServiceThread(this)
    	{
    		public void run()
    		{
    		  while(true)
    		  {
    			  try {
					receiveHeartBeat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		}
    	}.start();
}
public void sendHeartBeat() throws IOException
{  //send node info 
    byte b[] = this.nodeToBytes(this.memberNode);
	DatagramPacket packet = new DatagramPacket(b,b.length,this.getHeartBeatReceiver().getHostAddress(),this.getHeartBeatReceiver().getHostPort());	
   getMemberSocket().send(packet);
   logger.info("Heart beat sent"+b.toString()); //TODO

}

public void receiveHeartBeat() throws IOException
{
    byte[] buffer = new byte[HeartBeatMessage.MAX_LEN];
    DatagramPacket packet = new DatagramPacket(buffer,HeartBeatMessage.MAX_LEN);
    getMemberSocket().receive(packet);
    this.setHeartBeatSending(this.bytesToNode(buffer));
    logger.info("heart Beat received"+buffer.toString());
}


public void notifyFailure()
{
	
}

public byte[] nodeToBytes(MemberNode node)
{
	//TODO make this nodetoBytes(node,Message) message indicates join or etc etc
	return null;
}

public MemberNode bytesToNode(byte [] b)
{
     //TODO modify according to above 
	 return null;
}

public static void main(String args[])
{
	
}






}