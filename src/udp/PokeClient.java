package udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

// this is poke client using UDP
// send empty datagram to server and print response

// 这个class 相当于一个通用的client，和只返回数据的server通信
public class PokeClient
{
	private int bufferSize;
	private InetAddress host;
	private int timeOut;
	private int port;
	
	public PokeClient(int bufferSize, InetAddress host, int timeOut, int port)
	{
		this.bufferSize = bufferSize;
		this.host = host;
		this.timeOut = timeOut;
		this.port = port;
	}

	// send and receive from server
	public byte[] poke()
	{
		// random local port
		try(DatagramSocket ds = new DatagramSocket())
		{
			DatagramPacket dps = new DatagramPacket(new byte[1], 1, host, port);
			// specify remote host
			ds.connect(host, port);
			// set timeout
			ds.setSoTimeout(timeOut);
			
			ds.send(dps);
			// blocking call
			DatagramPacket dpr = new DatagramPacket(new byte[bufferSize], bufferSize);
			ds.receive(dpr);
			
			// extract data
			// this is the len of valid data! not the length of underlying array!
			int len = dpr.getLength();
			byte[] reponse = new byte[len];
			System.arraycopy(dpr.getData(), 0, reponse, 0, len);
			// return 
			return reponse;
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			PokeClient client = new PokeClient(2048, InetAddress.getByName("localhost")
					, 10000, 2999);
			byte[] reponse = client.poke();
			
			if (reponse == null)
			{
				System.out.println("Client: no reponse received...");
				return;
			}
			
			String data = new String(reponse, "US-ASCII");
			System.out.println("Client: " + data);
			
			
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
