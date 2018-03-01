package Socket_program;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

// socket connect to a public server to obtain standard time
public class DayTimeClient
{
	public static void main(String[] args)
	{
		// establish the connection
		try(Socket socket = new Socket("time.nist.gov", 13))
		{
			// must set timeout for read operation from input stream
			socket.setSoTimeout(3000);
			
			// get input
			// using ASCII encoding
			try(Reader reader = new InputStreamReader(
					new BufferedInputStream(
							// this specific protocol use ACSII
							socket.getInputStream()), "ASCII"))
			{
				StringBuilder sb = new StringBuilder();
				for (int c = reader.read(); c != -1; c = reader.read())
				{
					sb.append((char) c);
				}
				System.out.println(sb.toString());
			}
			catch(IOException e)
			{
				System.out.println("socket time out!");
			}
			
			
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
