package Http_URL_Connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


// check the last time the file was modified
public class HttpMethod_HEAD
{
	public static void main(String[] args)
	{
		URL url;
		try
		{
			url = new URL("http://www.ibiblio.org/xml/");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			// set request method
			// by default is the GET method
			// if set as GET will print response body
			http.setRequestMethod("HEAD");
			System.out.println("The last time the file was modified: "
					+ new Date(http.getLastModified()));
			System.out.println();
			System.out.println("response body: ");
			
			// print the body
			try(Reader reader = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(http.getInputStream()))))
			{
				int c = -1;
				while ((c = reader.read()) != -1)
				{
					System.out.print((char) c);
				}
			}
			
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println();
	}
}
