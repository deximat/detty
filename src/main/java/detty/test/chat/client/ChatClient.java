package detty.test.chat.client;

import detty.bootstrap.Bootstrap;
import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.TCPChannel;

public class ChatClient {
	
	private Channel channel;
	private String name;
	
	public ChatClient(String name) {
		this.name = name;
	}
	
	public void connect(String address, int port) throws Exception {
		this.channel = new Bootstrap()
				.channel(TCPChannel.class)
				.channelInitializer(new AbstractChannelInitializer<Channel>() {
					@Override
					public void init(Channel channel) throws Exception {
						channel.pipeline().addLast(new MessageReceivedHandler());
					}
				})
				.connect(address, port);
	}
	
	public void sendMessage(String message) {
		this.channel.writeAndFlush(message.getBytes());
	}
}
