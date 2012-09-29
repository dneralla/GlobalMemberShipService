package edu.illinois.cs425.mp2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartBeatServiceThread extends Thread {
	private final MemberServer server;

	public HeartBeatServiceThread(MemberServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		Message m = new Message(MessageTypes.HEART_BEAT);
		byte buf[]=new byte[Message.MAX_MESSAGE_LENGTH] ;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(m);
		  buf=bos.toByteArray();
		} 
		catch(Exception e)
		 {
			e.printStackTrace();
		 }finally {
		  try {
			out.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		}
        //hello
		
        InetAddress address;
        int port;
        DatagramSocket socket = server.getSocket();
		while (true) {
			
			if (server.getSendHeartBeat()) {
				// send HeartBeat request
				//System.out.println( server.getNeighborNode().getHostPort());
		        address = server.getNeighborNode().getHostAddress();
		        port = server.getNeighborNode().getHostPort();
		        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		        try {
					socket.send(packet);
				} catch (IOException e) {
					System.out.println("Error in sending hearbeat message");
					e.printStackTrace();
				}
			}
		}
	}
}
