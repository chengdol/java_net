package inet_Address;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test
{
	public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException
	{
		// this form will do DNS 
		InetAddress[] address = InetAddress.getAllByName("www.baidu.com");	
		System.out.println(address[0].getCanonicalHostName());
		
		// on host name will be provided if literal IP address as argument
		InetAddress ipo = InetAddress.getByName("192.4.23.42");
		System.out.println(ipo);
		

	}
}
