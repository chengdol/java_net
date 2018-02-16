package URL;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

// retrieve data from URL
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			// must specify the protocol
			URL url = new URL("http://www.baidu.com");
			// put in try-with resources statement
			try(InputStream in = new BufferedInputStream(url.openStream()))
			{
				int c = -1;
				while ((c = in.read()) != -1)
				{
					// 这种write貌似自动转换了byte -> char
					System.out.write(c);
				}
			}
		} catch (IOException e)
		{
			// this is for URL
			e.printStackTrace();
		}
		
	}
}
