package Http_URL_Connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// demonstrate getErrorStream() method
public class ErrorCondition
{	
	public static void main(String[] args)
	{
		try
		{
			URL url = new URL("http://www.ibiblio.org/walk");
			// default is GET method
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			
			// whether using proxy server?
			System.out.println("Using proxy: "
					+ http.usingProxy());
			
			try(InputStream raw = http.getInputStream())
			{
				printFromStream(raw);
			}
			// check method to see what exception should use
			catch(IOException e)
			{
				System.out.println("This is from error input stream...");
				printFromStream(http.getErrorStream());
			}			
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	// here need to throw exception
	private static void printFromStream(InputStream raw) throws IOException
	{
		
		try(Reader reader = new BufferedReader(
				new InputStreamReader(
						new BufferedInputStream(raw))))
		{
			int c = -1;
			while (-1 != (c = reader.read()))
			{
				System.out.print((char) c);
			}
		}
		
	}
}
