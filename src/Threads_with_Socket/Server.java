package Threads_with_Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// server side
// can handle multiple requests
public class Server
{
	public static void main(String[] args) throws IOException
	{
		// server socket
		ServerSocket server = new ServerSocket(5056);
		
		// loop to accept
		Socket client = null;
		while (true)
		{
			try
			{
				// get incoming request
				client = server.accept();
				System.out.println("Server: new client is connected..." + client);
				
				
				// get in and out stream
				DataInputStream dis = new DataInputStream(client.getInputStream());
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());
				
				System.out.println("Server: assign new thread for client...");
				// new thread for this request
				ClientHandler clientHandler = new ClientHandler(client, dis, dos);
				// start
				clientHandler.start();
				
			} 
			catch (Exception e)
			{
				client.close();
				e.printStackTrace();
			}

		}
	}
	
}

class ClientHandler extends Thread
{
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	    
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
		
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
	{
		super();
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run()
	{
		String received;
        String toreturn;
        
        while (true) 
        {
            try {
 
                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n"+
                            "Type exit to terminate connection.");
                 
                // receive the answer from client
                received = dis.readUTF();
                 
                if(received.equals("exit"))
                { 
                    System.out.println("Client " + s + " sends exit...");
                    System.out.println("Closing this connection.");
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
                 
                // creating Date object
                Date date = new Date();
                 
                // write on output stream based on the
                // answer from the client
                switch (received) {
                 
                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;
                         
                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;
                         
                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         
        try
        {
            // closing resources
            dis.close();
            dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}
