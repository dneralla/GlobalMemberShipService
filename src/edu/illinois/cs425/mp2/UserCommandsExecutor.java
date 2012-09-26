package edu.illinois.cs425.mp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UserCommandsExecutor {
	public static void main(String args[]) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		String inputLine;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while ((inputLine = in.readLine()) != null) {
			System.out.println("[Please Enter Command]$");
			if (inputLine.startsWith("join")) {
				byte[] buf = new byte[256];
				buf = inputLine.getBytes();
				// InetAddress address = InetAddress.getByName(args[0]);
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.send(packet);
			} else if (inputLine.startsWith("leave")) {

			} else if (inputLine.equals("help")) {
				System.out.println("Usage: [join|leave] <hostname:hostport>");
			} else if (inputLine.equals("exit")) {
				return;
			}

		}
	}
}
