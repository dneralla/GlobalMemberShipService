package edu.illinois.cs425.mp2;

public class JoinMessage {
	
	static final int MAX_LEN = 100;
	static final String message = "Join Message";
	
    public static byte[] getBytes()
    {
    	return message.getBytes();
    }

}
