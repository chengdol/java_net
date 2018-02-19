package URLConnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
			// 这个调用已经使用了connect() method了
			Map<String, List<String>> header = urlCon.getHeaderFields();
			header.forEach((k, v) -> {
				System.out.println(k
						+ ":"
						+ v.toString());
			});
			// get content from server
			System.out.println();
			System.out.println("URL complete source code");
			System.out.println("---------------------------------");
			
			// 注意这里怎么使用IO的
			// stream-->stream buffer-->reader-->reader buffer
			try(Reader in = new BufferedReader(
					new InputStreamReader(
					new BufferedInputStream(urlCon.getInputStream()))))
			{
				int c = 0;
				while ((c = in.read()) != -1)
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

	}
}
