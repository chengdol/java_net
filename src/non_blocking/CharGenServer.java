package non_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CharGenServer
{
	public final static int DEF_PORT = 2018;
	
	
	public static void main(String[] args)
	{
		// construct source
		byte[] rotation = new byte[95 * 2];
		for (byte i = ' '; i <= '~'; i++)
		{
			rotation[i - ' '] = i;
			rotation[95 + i - ' '] = i;
		}
		
		try
		{
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// bind
			InetSocketAddress address = new InetSocketAddress(DEF_PORT);
			serverChannel.bind(address);
			
			// non-blocking
			serverChannel.configureBlocking(false);
			
			// create selector
			Selector selector = Selector.open();
			// register
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Server: start to listen...");
			// start to process
			while (true)
			{
				// this is a blocking call
				selector.select();
				
				Set<SelectionKey> readyKey = selector.selectedKeys();
				// iterate the key
				Iterator<SelectionKey> iterator = readyKey.iterator();
				while (iterator.hasNext())
				{
					SelectionKey key = iterator.next();
					// remove the key
					iterator.remove();
					
					if (key.isAcceptable())
					{
						// get channel, need cast
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						// accept
						SocketChannel client = server.accept();
						System.out.println("Server: accept from " + client);
						
						// use selector must be non-blocking
						client.configureBlocking(false);
						SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE);
						
						// create buffer
						ByteBuffer clientBuffer = ByteBuffer.allocate(74);
						clientBuffer.put(rotation, 0, 72);
						clientBuffer.put((byte)'\r');
						clientBuffer.put((byte)'\n');
						clientBuffer.flip();
						// attach buffer to channel
						clientKey.attach(clientBuffer);
						
					}
					else if (key.isWritable())
					{
						SocketChannel client = (SocketChannel) key.channel();
						// get attached buffer
						ByteBuffer clientBuffer = (ByteBuffer) key.attachment();
						
						// 如果都发送完了,则更新
						if (!clientBuffer.hasRemaining())
						{
							// 找到起始
							clientBuffer.rewind();
							int first = clientBuffer.get();
							// 再回起始
							clientBuffer.rewind();
							// 下次元素起始位置
							int idx = first - ' ' + 1;
							clientBuffer.put(rotation, idx, 72);
							clientBuffer.put((byte)'\r');
							clientBuffer.put((byte)'\n');
							
							// prepare for writing
							clientBuffer.flip();
						}
						
						client.write(clientBuffer);
						TimeUnit.SECONDS.sleep(1);
					}
				}
			}
			
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
