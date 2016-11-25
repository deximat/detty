package detty.event_loop;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

import detty.channel.Channel;

public class EventLoopGroup {
	
	private Selector selector;
	
	public EventLoopGroup() {
		try {
			this.selector = SelectorProvider.provider().openSelector();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void register(Channel channel) throws ClosedChannelException {
		channel.javaChannel().register(this.selector, 0, channel);
	}

}
