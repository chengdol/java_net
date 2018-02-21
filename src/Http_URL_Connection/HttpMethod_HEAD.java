package Http_URL_Connection;

import java.io.IOException;
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
			http.setRequestMethod("HEAD");
			System.out.println("The last time the file was modified: "
					+ new Date(http.getIfModifiedSince()));
			
			
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
