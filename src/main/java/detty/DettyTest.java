package detty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DettyTest {

	public static void main(String[] args) {
		// new channel
		// new other channel
		// add handler
		// talk to one another
	}
	

	private static void newUdpChannel(final int port) {
		Bootstrap boot = new Bootstrap()
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<Channel>() {
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(handlers);
					};
				})
				.
				// .handler(new UdpChannelInitializer(UDPMessageProcessor::new))
				// .group(nioEventLoopGroup)
				// .channel(NioDatagramChannel.class);

		ChannelFuture future = boot.bind(port).syncUninterruptibly();
		if (!future.isSuccess()) {
			throw new RuntimeException(String.format("Failed to bind on [port = %d].", port), future.cause());
		}
	}
}
