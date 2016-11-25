package detty.pipeline;

import detty.channel.ChannelHandler;

public interface Pipeline {
	
	public void addLast(ChannelHandler handler);
}
