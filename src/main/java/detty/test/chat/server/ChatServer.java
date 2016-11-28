package detty.test.chat.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import detty.bootstrap.Bootstrap;
import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;

public class ChatServer {
	
	private final ConcurrentLinkedQueue<Channel> users = new ConcurrentLinkedQueue<>();
	
	private final int port;

	public ChatServer(int port) {
		this.port = port;
	}
	
	public void start() {
		new Bootstrap()
			.channelInitializer(new AbstractChannelInitializer<Channel>() {
				@Override
				public void init(Channel channel) throws Exception {
					ChatServer.this.users.add(channel);
					channel.pipeline().addLast(new BroadcastToAllHandler(ChatServer.this.users));
				}
			})
			.bind(this.port);
	}
	

	
}
