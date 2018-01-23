package Threads_with_Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args) throws IOException
	{
		// here use Scanner 
		Scanner scn = new Scanner(System.in);
		
		// getting local ip address
		InetAddress ip = InetAddress.getByName("localhost");
		// establish a connection here!
		Socket socket = new Socket(ip, 5056);
		
		// obtain in and out stream
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		// exchange information between client and server
		while (true)
		{
			// after new socket
			// server will send message to client
			System.out.println(dis.readUTF());
			String toSend = scn.nextLine();
			dos.writeUTF(toSend);
			
			if (toSend.equals("exit"))
			{
				System.out.println("Clent: closing connection...");
				socket.close();
				System.out.println("Client: connection closed: " + socket);
				
				break;
			}
			
			// print time or date from server
			System.out.println("Client: " + dis.readUTF());
		}
		
		// close resources
		scn.close();
		dis.close();
		dos.close();
	}
}
