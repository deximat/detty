package detty.bootstrap;

import java.net.InetSocketAddress;

import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.ChannelFuture;
import detty.channel.TCPChannel;
import detty.event_loop.EventLoopGroup;

public class Bootstrap {
	
	private AbstractChannelInitializer<Channel> channelInitializer;
	
	private Class<? extends Channel> channelClass = TCPChannel.class;

	private EventLoopGroup group = new EventLoopGroup();

	public Bootstrap channelInitializer(final AbstractChannelInitializer<Channel> initializer) {
		this.channelInitializer = initializer;
		return this;
	}

	public Bootstrap channel(Class<? extends Channel> channelClass) {
		this.channelClass = channelClass;
		return this;
	}
	
	public Channel bind(String address, int port) throws Exception {
		return bind(new InetSocketAddress(address, port));
	}

	
	private Channel bind(InetSocketAddress inetSocketAddress) throws Exception {
		Channel channel = newChannel();
		this.channelInitializer.init(channel);
		this.group.register(channel);
		return channel;
	}

	private Channel newChannel() {
		try {
			return this.channelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			return null;
		}
	}

	public ChannelFuture bind(int port) {
		throw new RuntimeException("Implement me.");
	}
}
