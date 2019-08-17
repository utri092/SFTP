package client;

import java.io.*; 
import java.net.*; 

public class client {
	
	public static void main(String argv[]) throws Exception{
		Socket clientSocket = new Socket("127.0.0.1", 6789);
		System.out.println("client 1");
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = 
	    new DataOutputStream(clientSocket.getOutputStream()); 
        
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		System.out.println("client 2");
		String outgoingMessage;
		String r;
		while(true) {
			outgoingMessage = inFromUser.readLine();
			outToServer.writeBytes(outgoingMessage + '\n');
			outToServer.flush();
			r = inFromServer.readLine();
			System.out.println(r+"\n");
		}
	
	}

}
