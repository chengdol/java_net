package Headers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AllHeaders
{
	public static void main(String[] args)
	{
		try
		{
			URL url = new URL("https://www.google.com");
			URLConnection connection = url.openConnection();
			
			// print all header info
			for (int i = 0;; i++)
			{
				// get header value
				String value = connection.getHeaderField(i);
				if (value == null) { break; }
				// get header key
				System.out.println(connection.getHeaderFieldKey(i) + " : " + value);
			}
			
			
		} catch (MalformedURLException e)
		{
			System.out.println("this is not a valid URL!");
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
}
