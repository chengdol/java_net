package Http_URL_Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// set the method as OPTIONS
// get the header, find the Allow field
public class HttpMethod_OPTIONS
{
	public static void main(String[] args)
	{
		try
		{
			URL url = new URL("http://www.ibiblio.org/xml");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			
			// set options method
			http.setRequestMethod("OPTIONS");
			// print response header
			// server only respond a header!
			http.getHeaderFields().forEach((k, v) -> {
				System.out.println(k + " : " + v);
			});
			
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
				
	}
}
