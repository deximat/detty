package detty;

import detty.bootstrap.Bootstrap;
import detty.channel.AbstractChannelInitializer;
import detty.channel.Channel;
import detty.channel.ChannelFuture;
import detty.channel.ChannelHandler;
import detty.channel.TCPChannel;

public class Detty2Test {
	
	public static void main(String[] args) {
		Bootstrap boot = new Bootstrap()
				.channel(TCPChannel.class)
				.channelInitializer(new AbstractChannelInitializer<TCPChannel>() {
					public void init(TCPChannel ch) throws Exception {
						ch.pipeline().addLast(new ChannelHandler() {
							// do something
						});
					};
				});
		Channel future = boot.connect("localhost", 100);
	}

}
