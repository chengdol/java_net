package http_servers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

// redirect all requests to a new site using 302 FOUND
public class HttpRedirector
{
	private final static Logger logger = Logger.getLogger("Redirector");
	
	private final int port;
	private final String newSite;
	
	public HttpRedirector(int port, String newSite)
	{
		this.port = port;
		this.newSite = newSite;
	}

	public void start()
	{
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		// start server
		try(ServerSocket server = new ServerSocket(port))
		{
			logger.info("Server: accepting request on port " + port);
			
			while (true)
			{
				try
				{
					Socket request = server.accept();
					pool.submit(new RedirectHandler(request));
				}
				catch (IOException e)
				{
					logger.log(Level.SEVERE, "Server: could not start server...", e);
				}
				catch (RuntimeException e)
				{
					logger.log(Level.SEVERE, "Server: unexpected error...", e);
				}
			}
			
		} 
		catch (IOException e)
		{
			logger.log(Level.SEVERE, "Server: could not start server...", e);
		}
		catch (RuntimeException e)
		{			
			logger.log(Level.SEVERE, "Server: unexpected error...", e);
		}
	}
	
	private class RedirectHandler implements Runnable
	{
		private Socket socket;
		
		public RedirectHandler(Socket socket)
		{
			this.socket = socket;
		}

		@Override
		public void run()
		{
			try
			{
				// out, need to be encoded as US-ASCII
				Writer out = new BufferedWriter(
								new OutputStreamWriter(
										socket.getOutputStream(), "US-ASCII"));
				
				// in, decoded as US-ASCII
				Reader in = new BufferedReader(
								new InputStreamReader(
										socket.getInputStream(), "US-ASCII"));
				
				// read request line
				StringBuilder sb = new StringBuilder();
				while (true)
				{
					int c = in.read();
					if (c == -1 || c == '\r' || c == '\n') { break; }
					sb.append((char) c);
				}
				
				String requestLine = sb.toString();
				if (requestLine.indexOf("HTTP") != -1)
				{
					out.write("HTTP/1.0 302 FOUND\r\n");
					Date now = new Date();
					out.write("Date: " + now + "\r\n");
					out.write("Server: Redirector 1.1\r\n");
					out.write("Location: " + newSite + "\r\n");
					out.write("Content-type: text/html\r\n\r\n");
					out.flush();
				}
				
				// for old browser not support redirection
				out.write("<HTML><HEAD><TITLE>Document moved</TITLE></HEAD>\r\n");
				out.write("<BODY><H1>Document moved</H1>\r\n");
				out.write("The document "
				+ " has moved to\r\n<A HREF=\"" + newSite + "\">"
				+ newSite
				+ "</A>.\r\n Please update your bookmarks<P>");
				out.write("</BODY></HTML>\r\n");
				out.flush();
				
				logger.info("Server: redirected " + socket.getRemoteSocketAddress() + "\r\n");
			}
			catch (IOException e)
			{
				logger.log(Level.SEVERE, "Server: unexpected error when redirecting...", e);
			}
			finally
			{
				try
				{
					socket.close();
				} catch (IOException e)
				{
					logger.log(Level.SEVERE, "Server: close redirect thread failed...", e);
				}
			}

		}
		
	}
	
	public static void main(String[] args)
	{
		// redirect all requests to google.com
		int port = 1997;
		String newSite = "https://www.google.com";
		
		HttpRedirector redirector = new HttpRedirector(port, newSite);
		redirector.start();
	}
}
