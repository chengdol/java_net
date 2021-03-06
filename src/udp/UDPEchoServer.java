package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

// this class extends abstract UDP server
public class UDPEchoServer extends UDPServer
{
	private final static int PORT = 2999;
	
	public UDPEchoServer(int bufferSize, int port)
	{
		super(bufferSize, port);
	}

	@Override
	public void respond(DatagramSocket ds, DatagramPacket dp)
	{
		DatagramPacket dps = new DatagramPacket(dp.getData(), dp.getLength()
				, dp.getAddress()
				, dp.getPort());
		try
		{
			ds.send(dps);
		} catch (IOException e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static void main(String[] args)
	{
		UDPServer server = new UDPEchoServer(8192, PORT);
		Thread thread = new Thread(server);
		thread.start();
	}
}
