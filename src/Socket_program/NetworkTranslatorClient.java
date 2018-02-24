package Socket_program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

// write and read from a translate server
// using socket

// use URLConnection here?

public class NetworkTranslatorClient
{
	public static void main(String[] args)
	{
		// send request
		// use telnet command to check available dictionary
		try(Socket socket = new Socket("dict.org", 2628))
		{
			socket.setSoTimeout(15000);
			
			Writer writer = new BufferedWriter(
								new OutputStreamWriter(
										socket.getOutputStream(), "UTF-8"));
			// translate hello from English to Italian
			String request = "DEFINE fd-eng-ita hello\r\n";
			writer.write(request);
			writer.flush();
			
			System.out.println("Client:");
			System.out.println(request);
			
			System.out.println("Server:");
			// get response
			// buffered reader has readline() method!
			BufferedReader reader = new BufferedReader(
									new InputStreamReader(
											socket.getInputStream(), "UTF-8"));
			
			for (String line = reader.readLine(); !line.equals("."); line = reader.readLine())
			{
				System.out.println(line);
			}
			
			
			// quit connection
			writer.write("quit\r\n");
			writer.flush();
			
			// close the socket will also close the in and out stream
			
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
}
