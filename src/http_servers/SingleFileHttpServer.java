package http_servers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

// using socket implements a HTTP server

// construct header info, send at first with socket
// then send content

// always send the same response message to client request
// can test by browser or telnet command
public class SingleFileHttpServer
{
	private static final Logger logger = Logger.getLogger("SingleFileHttpServer");
	
	private final int port;
	// can used in inner class method
	private final byte[] header;
	private final byte[] content;
	private String encoding;
	
	public SingleFileHttpServer(String data, int port, String encoding, String mimeType) 
			throws UnsupportedEncodingException
	{
		this(data.getBytes(encoding), port, encoding, mimeType);
	}
	
	
	
	public SingleFileHttpServer(byte[] data, int port, String encoding, String mimeType)
	{
		this.port = port;
		content = data;
		this.encoding = encoding;
		
		// add header for HTTP protocol
		String header = "HTTP/1.0 200 OK\r\n"
				+ "Server: OneFile 2.0\r\n"
				+ "Content-length: " + content.length + "\r\n"
				// here need a empty line at end \r\n
				+ "Content-type: " + mimeType + "; charset=" + encoding + "\r\n\r\n";
		
		// convert to byte array
		// use US-ASCII encoding!
		this.header = header.getBytes(Charset.forName("US-ASCII"));
	}


	public void start()
	{
		// create fixed size thread pool
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		try(ServerSocket server = new ServerSocket(port))
		{
			logger.info("Server: listen on port " + port);			
			// while loop
			while (true)
			{
				try
				{
					// no use try-with-resource here for child socket
					Socket child = server.accept();
					pool.submit(new HttpHandler(child));
				}
				catch(IOException e)
				{
					logger.log(Level.SEVERE, "Server: accept error...", e);
				}
				catch(RuntimeException e)
				{
					logger.log(Level.SEVERE, "Server: unexpected error...", e);					
				}
			}
			
			
			
		} catch (IOException e)
		{
			logger.log(Level.SEVERE, "Server: could not start server...", e);
		}
		catch(RuntimeException e)
		{
			logger.log(Level.SEVERE, "Server: could not start server...", e);			
		}
		
	}
	
	
	private class HttpHandler implements Runnable
	{
		private Socket connection;
		
		public HttpHandler(Socket connection)
		{
			this.connection = connection;
		}

		@Override
		public void run()
		{
			try
			{
				// get input/output stream
				InputStream in = new BufferedInputStream(connection.getInputStream());
				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				
				// check protocol
				// if http, then add header
				StringBuilder sb = new StringBuilder();
				// read the request line first
				while (true)
				{
					int c = in.read();
					if (c == -1 || c == '\r' || c == '\n') {  break; }
					sb.append((char) c);
				}
				// output request line from client
				logger.info("Server: first request line from client: \r\n" + sb.toString() + "\r\n");
				
				// 这里引用了containing class的field variable
				if (sb.toString().indexOf("HTTP") != -1)
				{
					out.write(header);
				}
				
				out.write(content);
				out.flush();
				
			} catch (IOException e)
			{
				logger.log(Level.WARNING, "Child: errir writing to client...", e);
			}
			finally
			{
				try
				{
					connection.close();
				} catch (IOException e)
				{
					logger.log(Level.SEVERE, "Child: unexpected error...", e);					
				}
			}
			
		}
		
	}
	
	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String data = "This message is from single file server!\r\n";
		int port = 1949;
		String encoding = "UTF-8";
		// can use URLConnection to get content type from a file
		// see textbook code
		String mimeType = "text/html";
		
		// now can use web browser to test
		// >>>localhost:1949
		SingleFileHttpServer server = new SingleFileHttpServer(data, port, encoding, mimeType);
		server.start();
	}
}
