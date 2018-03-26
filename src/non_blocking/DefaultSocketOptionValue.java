package non_blocking;

import java.io.IOException;
import java.net.SocketOption;
import java.nio.channels.DatagramChannel;

// default UDP channel socket option value
public class DefaultSocketOptionValue
{
	public static void main(String[] args) throws IOException
	{
		try (DatagramChannel channel = DatagramChannel.open())
		{
			for (SocketOption<?> option : channel.supportedOptions())
			{
				System.out.println(option.name() + " : " + channel.getOption(option));
			}
		}
	}
}
