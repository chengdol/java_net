package URLConnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class IsModifiedSince
{
	public static void main(String[] args)
	{
		try
		{
			// 如何测试
			// inspect source code, find img tag then use it;s link
			URL url = new URL("https://images.pexels.com/photos/413195/pexels-photo-413195.jpeg?w=940&h=650&auto=compress&cs=tinysrgb");
			URLConnection connection = url.openConnection();
			
			Date today = new Date();
			long last24Hours = 24 * 60 * 60 * 1000;
			
			System.out.println("Original if modified since: "
					+ new Date(connection.getIfModifiedSince()));
			
			// set check time since
			Date timePoint = new Date(today.getTime() - last24Hours);
			connection.setIfModifiedSince(timePoint.getTime());
			
			// if modified, read data from input stream
			System.out.println("Will retrieve file if modified since "
					+ new Date(connection.getIfModifiedSince()));
			
			// 如果返回304则没有inputStream
			System.out.println("response code: "
					+ connection.getHeaderField(0));
			System.out.println();
			System.out.println();
			try(InputStream in = new BufferedInputStream(connection.getInputStream()))
			{
				Reader reader = new BufferedReader(new InputStreamReader(in));
				int c = 0;
				while ((c = reader.read()) != -1)
				{
					System.out.print((char) c);
				}
				System.out.println();
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
