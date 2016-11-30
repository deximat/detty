package detty.bootstrap;

import java.net.InetSocketAddress;

import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.InternalChannel;
import detty.channel.TCPChannel;
import detty.channel.event.EventLoop;
import detty.channel.event.EventLoopGroup;

/**
 * {@link AbstractBootstrap} is there to give you easy way to setup your network
 * layer.
 */
public abstract class AbstractBootstrap<BootstrapType> {

	private AbstractChannelInitializer<Channel> channelInitializer;

	private Class<? extends InternalChannel> channelClass = TCPChannel.class;

	private final EventLoopGroup group = produceEventGroup();

	/**
	 * Sets channel initializer for this bootstrap, it will be executed on every
	 * channel when it is first created.
	 * 
	 * @param initializer
	 *            which will be applied to every new channel
	 * 
	 * @return bootstrap, so it can be chained
	 */
	@SuppressWarnings("unchecked")
	public BootstrapType channelInitializer(
			final AbstractChannelInitializer<Channel> initializer) {
		this.channelInitializer = initializer;
		return (BootstrapType) this;
	}

	/**
	 * Protected method which should be implemented in concrete bootstrap to create enough {@link EventLoop}s.
	 * 
	 * @return {@link EventLoopGroup} to be used by this bootstrap
	 */
	protected abstract EventLoopGroup produceEventGroup();

	/**
	 * Sets channel class provided in parameter, if channel is not set
	 * {@link TCPChannel} will be used.
	 * 
	 * @param channelClass
	 *            which we want to be used for our bootstrap
	 * 
	 * @return bootstrap, so it can be chained
	 */
	@SuppressWarnings("unchecked")
	public BootstrapType channel(
			Class<? extends InternalChannel> channelClass) {
		this.channelClass = channelClass;
		return (BootstrapType) this; 
	}

	protected Channel connect(InetSocketAddress inetSocketAddress)
			throws Exception {
		InternalChannel channel = newChannel();
		// TODO: maybe init from loop thread
		register(channel);
		channel.unsafeConnect(inetSocketAddress);
		return channel;
	}

	protected void register(InternalChannel channel) throws Exception {
		this.channelInitializer.init(channel);
		this.group.register(channel);
	}

	private InternalChannel newChannel() {
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
