package non_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

// non-blocking UDP echo client
// can be paired with UDP echo server in udp package

// send 0 to 100 and receive the same
public class UDPEchoClient
{
	private final static int PORT = 2999;
	// [0, limit)
	private final static int LIMIT = 100;
	
	public static void main(String[] args)
	{
		// create remote host address
		SocketAddress host = new InetSocketAddress("localhost", PORT);
		
		try (DatagramChannel channel = DatagramChannel.open())
		{
			// non-blocking
			channel.configureBlocking(false);
			// connect
			channel.connect(host);
			// register
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			
			int n = 0;
			int readNum = 0;
			ByteBuffer buffer = ByteBuffer.allocate(4);
			
			while (true)
			{
				// finish 
				if (readNum == LIMIT) { break; }
				
				// wait 10 seconds for final round
				selector.select(10000);
				Set<SelectionKey> set = selector.selectedKeys();
				// all packets have been sent and no more data incoming
				if (set.isEmpty() && n == LIMIT) { break; }
				
				Iterator<SelectionKey> iterator = set.iterator();
				while (iterator.hasNext())
				{
					SelectionKey key = iterator.next();
					iterator.remove();
					
					if (key.isReadable())
					{
						buffer.clear();
						((DatagramChannel) key.channel()).read(buffer);
						buffer.flip();
						// get number
						int echo = buffer.getInt();
						System.out.println("Echo: " + echo);
						
						readNum++;
					}
					else if (key.isWritable())
					{
						buffer.clear();
						buffer.putInt(n);
						buffer.flip();
						
						((DatagramChannel) key.channel()).write(buffer);
						System.out.println("Write: " + n);
						
						n++;
						if (n == LIMIT)
						{
							key.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			}
			
			System.out.println("Finish: " + readNum + " out of " + LIMIT);
			System.out.println("Success rate: " + 100.0 * readNum / LIMIT + "%");
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
