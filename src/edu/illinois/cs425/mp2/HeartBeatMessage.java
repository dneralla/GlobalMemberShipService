package edu.illinois.cs425.mp2;

public class HeartBeatMessage {
	
	static final int MAX_LEN = 100;
	static final String message = "HeartBeat Message";
	
    public static byte[] getBytes()
    {
    	return message.getBytes();
    }


	

}
