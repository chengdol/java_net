package Socket_program;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// server
// cannot handle multiple request at the same time
public class Server
{
	private Socket socket;
	private ServerSocket server;
	private DataInputStream in;
	
	public Server(int port)
	{
		try
		{
			// start server and wait for connection...
			server = new ServerSocket(port);
			System.out.println("Server: start service");
			System.out.println("Server: wait for connection...");
			
			// block until connection established
			socket = server.accept();
			System.out.println("Server: client accepted!");
			
			// decorator pattern
			// take input from socket
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			
			String line = "";
			while (!line.equals("over"))
			{
				line = in.readUTF();
				System.out.println(">>> " + line);
			}
			
			// close connection
			System.out.println("Server: connection closed!");
			socket.close();
			// no need
//			in.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		Server server = new Server(5000);
	}
	
}
