package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

// this server support tcp and udp on same port
// 很难收到回复 如果 用官方的host
public class DayTimeUDPClient
{
//	private final static int PORT = 13;
	private final static int PORT = 2999;
//	private final static String HOST = "time.nist.gov";
	private final static String HOST = "localhost";
	
	public static void main(String[] args)
	{
		// pass 0 to randomly assign a port to local socket
		try (DatagramSocket socket = new DatagramSocket(0))
		{
			// set time out
			socket.setSoTimeout(10000);
			// address
			InetAddress address = InetAddress.getByName(HOST);
			// set request and response datagram packet
			DatagramPacket request = new DatagramPacket(new byte[1], 1, address, PORT);
			DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
			// send and receive
			// these are blocking calls
			socket.send(request);
			socket.receive(response);
			
			// parse
			String result = new String(response.getData(), 0, response.getLength(), "US-ASCII");
			
			System.out.println(result);
			
		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
