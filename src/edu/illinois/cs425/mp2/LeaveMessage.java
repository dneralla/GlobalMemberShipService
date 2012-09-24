package edu.illinois.cs425.mp2;

public class LeaveMessage {
	
	static final int MAX_LEN = 100;
	static final String message = "Leave Message";
	
    public static byte[] getBytes()
    {
    	return message.getBytes();
    }

}
