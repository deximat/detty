package detty.bootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.TCPChannel;
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
		register(channel);
		channel.connect(inetSocketAddress);
		return channel;
	}

	private void register(Channel channel) throws Exception {
		this.channelInitializer.init(channel);
		this.group.register(channel);
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
	
	public void bind(final int port) {
		Thread acceptorThread = new Thread(() -> {
			ServerSocketChannel serverChannel;
			try {
				serverChannel = ServerSocketChannel.open();
				serverChannel.bind(new InetSocketAddress(port));
				while (true) {
					try {
						System.out.println("Server is waiting for clients.");
						SocketChannel javaChannel = serverChannel.accept();
						System.out.println("New client accepted from " + javaChannel.getRemoteAddress());
						TCPChannel channel = new TCPChannel(javaChannel);
						register(channel);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		acceptorThread.start();
		
	}
}
