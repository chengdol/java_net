package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

// this is a abstract server class
public abstract class UDPServer implements Runnable
{
	private final int bufferSize;
	private final int port;
	protected final Logger logger = Logger.getLogger(UDPServer.class.getCanonicalName());
	// volatile
	private volatile boolean isShutDown = false;
	
	public UDPServer(int bufferSize, int port)
	{
		this.bufferSize = bufferSize;
		this.port = port;
	}
	
	public UDPServer(int port)
	{
		this(8192, port);
	}
	

	@Override
	public void run()
	{
		System.out.println("Echo server: start...");
		byte[] buffer = new byte[bufferSize];
		try (DatagramSocket ds = new DatagramSocket(port))
		{
			ds.setSoTimeout(10000);
			DatagramPacket dpr = new DatagramPacket(buffer, bufferSize);
			while (true)
			{
				if (isShutDown) { return; }
				// wait for receiving
				try
				{
					// may be timeout
					ds.receive(dpr);
					respond(ds, dpr);
				} 
				// check shutdown if timeout
				catch (SocketTimeoutException e)
				{
					if (isShutDown) return;
				}
				catch (IOException e)
				{
					logger.log(Level.WARNING, e.getMessage(), e);
				}
				
			}	
		} catch (SocketException e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		} 
	}
	
	// abstract method
	public abstract void respond(DatagramSocket ds, DatagramPacket dp);
	
	public void setShutDown()
	{
		isShutDown = true;
	}
}
