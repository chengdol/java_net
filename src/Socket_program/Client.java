package Socket_program;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


// client
// send message from terminal to socket
// type "over" to terminate connection
public class Client
{
	// socket
	private Socket socket = null;
	// input stream
	// example use DateInputStream
	// but the readLine() was deprecated
	private BufferedReader in = null;
	// output stream
	private DataOutputStream out = null;
	
	
	public Client(String address, int port)
	{
		try
		{
			// establish a connection
			socket = new Socket(address, port);
			System.out.println("Client: Connected");
			
			// set input from terminal
			in = new BufferedReader(new InputStreamReader(System.in));
			// send output to socket
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// read from terminal
		String line = "";
		// end when type "over"
		while (!line.equals("over"))
		{
			try
			{
				
				line = in.readLine();
				out.writeUTF(line);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
		
		try
		{
			// close
			socket.close();
			// no need
//			in.close();
//			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args)
	{
		// start connection
		Client client = new Client("127.0.0.1", 5000);
	}
}
