package inet_Address;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

// example for URL encoder and decoder
public class URLCoder
{
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		// need to be encoded piece by piece
		// otherwise will encode all special characters
		String res = URLEncoder.encode("https://www.google.com/search?hl=en&as_q=Java&as_epq=I/O", "UTF-8");
		System.out.println(res);
		
		
		String input = "https://www.google.com/" +
				"search?hl=en&as_q=Java&as_epq=I%2FO";
		String output = URLDecoder.decode(input, "UTF-8");
		System.out.println(output);
	}
	
}
