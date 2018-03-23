package non_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;


// this is a char generator client
// receive char sequence from server when connected

// this process will never terminate 
public class CharGenClient
{
	
	private final static int DEF_PORT = 19;
	
	public static void main(String[] args)
	{
		// open a socket channel
		try
		{
			// create address and client channel
			SocketAddress address = new InetSocketAddress("localhost", DEF_PORT);
			// here is blocking default
			// on server side will be non-blocking
			SocketChannel client = SocketChannel.open(address);
			// allocate buffer
			ByteBuffer buffer = ByteBuffer.allocate(74);
			
			// to console
			// Channels utility class
			WritableByteChannel out = Channels.newChannel(System.out);
			
			while (client.read(buffer) != -1)
			{
				// flip buffer
				buffer.flip();
				out.write(buffer);
				// reset position and limit
				buffer.clear();
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
