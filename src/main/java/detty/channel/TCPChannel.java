package detty.channel;

import java.nio.channels.SelectableChannel;

import detty.pipeline.Pipeline;

public class TCPChannel implements Channel {

	public Pipeline pipeline() {
		return null;
	}

	public SelectableChannel javaChannel() {
		return null;
	}

}
