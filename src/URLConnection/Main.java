package URLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

// read URL by URLConnection
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			URL url = new URL("https://www.google.com");
			// open connection
			URLConnection urlCon = url.openConnection();
			
			// get header information
			Map<String, List<String>> header = urlCon.getHeaderFields();
			header.forEach((k, v) -> {
				System.out.println(k
						+ ":"
						+ v.toString());
			});
			
			// get content
			System.out.println();
			System.out.println("URL complete source code");
			System.out.println("---------------------------------");
			
			BufferedReader body = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			
			String line = null;
			while ((line = body.readLine()) != null)
			{
				System.out.println(line);
			}
			
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
