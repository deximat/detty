package detty.test.chat.server;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import detty.bootstrap.ServerBootstrap;
import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.TCPChannel;
import detty.channel.pipeline.security.IncomingBurstControlHandler;

public class ChatServer {

	private final ConcurrentLinkedQueue<Channel> users = new ConcurrentLinkedQueue<>();

	private final int port;

	public ChatServer(int port) {
		this.port = port;
	}

	public void start() {
		new ServerBootstrap().channel(TCPChannel.class)
				.channelInitializer(new AbstractChannelInitializer<Channel>() {
					@Override
					public void init(Channel channel) throws Exception {
						ChatServer.this.users.add(channel);
						channel.pipeline().addLast(new IncomingBurstControlHandler(10, 1, TimeUnit.SECONDS));
						channel.pipeline().addLast(new BroadcastToAllHandler(
								ChatServer.this.users));
						channel.setChannelClosedListener(() -> {
							ChatServer.this.users.remove(channel);
						});
					}
				}).bind(this.port);
	}

}
