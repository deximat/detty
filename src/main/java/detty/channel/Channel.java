package detty.channel;

import java.nio.channels.SelectableChannel;

import detty.pipeline.Pipeline;

public interface Channel {
	public Pipeline pipeline();

	public SelectableChannel javaChannel();
}
