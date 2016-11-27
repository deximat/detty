package detty.channel.pipeline;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import detty.channel.Channel;
import detty.channel.ChannelHandler;

public class Pipeline {
	
	private List<ChannelHandler> handlers = new ArrayList<>();
	private Channel channel;
	
	public Pipeline(final Channel channel) {
		this.channel = channel;
	}
	
	public void addLast(ChannelHandler handler) {
		this.handlers.add(handler);
	}

	public void fireChannelRead(ByteBuffer buffer) {
		this.handlers.get(0).onMessageReceived(this.channel, buffer);
	}
}
