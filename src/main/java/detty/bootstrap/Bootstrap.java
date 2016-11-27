package detty.bootstrap;

import java.net.InetSocketAddress;

import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.ChannelFuture;
import detty.channel.TCPChannel;
import detty.channel.event.EventLoop;
import detty.channel.event.EventLoopGroup;

public class Bootstrap {
	
	private AbstractChannelInitializer<Channel> channelInitializer;
	
	private Class<? extends Channel> channelClass = TCPChannel.class;

	private EventLoopGroup group = new EventLoopGroup(4);

	public Bootstrap channelInitializer(final AbstractChannelInitializer<Channel> initializer) {
		this.channelInitializer = initializer;
		return this;
	}

	public Bootstrap channel(Class<? extends Channel> channelClass) {
		this.channelClass = channelClass;
		return this;
	}
	
	public Channel connect(String address, int port) throws Exception {
		return connect(new InetSocketAddress(address, port));
	}

	
	private Channel connect(InetSocketAddress inetSocketAddress) throws Exception {
		Channel channel = newChannel();
		// TODO: maybe init from loop thread
		this.channelInitializer.init(channel);
		this.group.register(channel);
		channel.connect(inetSocketAddress);
		return channel;
	}

	private Channel newChannel() {
		try {
			return this.channelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
