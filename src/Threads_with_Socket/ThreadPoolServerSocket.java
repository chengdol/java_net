package Threads_with_Socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

// 这种结构比较好
// don't use try-with for child socket
// use executor limit thread pool size

// this is a time server example
// add logging to it
public class ThreadPoolServerSocket
{
	private final static int PORT = 1992;
	// logging
	// 这里没有配置，输出在console
	private final static Logger auditLogger = Logger.getLogger("request");
	private final static Logger errorLogger = Logger.getLogger("error");
	
	
	private static class ChildSocket implements Runnable
	{
		private Socket socket;
		
		public ChildSocket(Socket socket)
		{
			super();
			this.socket = socket;
		}

		@Override
		public void run()
		{
			try(Writer writer = new BufferedWriter(
									new OutputStreamWriter(
											socket.getOutputStream())))
			{
				Date current = new Date();
				// log
				auditLogger.info(current + " " + socket.getRemoteSocketAddress());
				
				// mimic processing time
				TimeUnit.SECONDS.sleep(2);
				writer.write("\r\n");
				writer.write("Time: " + current + "\r\n");
				
				// send data
				writer.flush();
				writer.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
			// can use finally to close socket here
			
		}
	}

	
	public static void main(String[] args)
	{
		// create pool
		ExecutorService pool = Executors.newFixedThreadPool(
											Runtime.getRuntime().availableProcessors());
		// create server socket
		try(ServerSocket server = new ServerSocket(PORT))
		{
			while (true)
			{
				// using regular try catch block
				try
				{
					Socket child = server.accept();
					// create new task
					ChildSocket thread = new ChildSocket(child);
					// submit
					pool.submit(thread);
					
				}
				// this catch for child socket when accept
				catch(IOException e)
				{
					errorLogger.log(Level.SEVERE, "accept error", e);
				}
				catch(RuntimeException e)
				{
					errorLogger.log(Level.SEVERE, "unexpected error " + e.getMessage(), e);					
				}
			}
			
		} 
		catch (IOException e)
		{
			errorLogger.log(Level.SEVERE, "could not start server", e);					
		}
		catch(RuntimeException e)
		{
			errorLogger.log(Level.SEVERE, "could not start server " + e.getMessage(), e);								
		}
		
		pool.shutdown();
	}
}






