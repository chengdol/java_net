package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

// pair with DayTimeUDPClient
public class DayTimeUDPServer
{
	private final static int PORT = 2999;
	private final static Logger audit = Logger.getLogger("audit");
	private final static Logger error = Logger.getLogger("error");
	
	public static void main(String[] args)
	{
		try (DatagramSocket server = new DatagramSocket(PORT))
		{
			audit.info("Server: ready to serve...");
			while (true)
			{
				DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
				// blocking call
				server.receive(request);
				
				// send day time to client
				String time = new Date().toString();
				byte[] data = time.getBytes("US-ASCII");
				DatagramPacket reponse = new DatagramPacket(data, data.length
						, request.getAddress()
						, request.getPort());
				
				server.send(reponse);
				audit.info("Server: " + time + " " + request.getAddress());		
			}
				
			
		} catch (SocketException e)
		{
			error.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e)
		{
			error.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
