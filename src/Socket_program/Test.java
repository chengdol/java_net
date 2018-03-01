package Socket_program;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		Socket socket = new Socket();
		SocketAddress address = new InetSocketAddress("www.google.com", 80);
		socket.connect(address);
		
		System.out.println(socket.getLocalPort());
		System.out.println(socket.getLocalAddress());
		System.out.println(socket.getLocalSocketAddress());
		
	}
}
