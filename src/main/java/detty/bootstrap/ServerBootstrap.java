package detty.bootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import detty.channel.TCPChannel;
import detty.channel.event.EventLoopGroup;

/**
 *{@link ServerBootstrap} is extension of {@link AbstractBootstrap} which is optimized for non-blocking server.
 */
public final class ServerBootstrap extends AbstractBootstrap<ServerBootstrap> {

	/**
	 * By binding bootstrap to provided port, we are actually starting an
	 * acceptor ServerSocket which will be used to accept new clients, and they
	 * will be configured as provided in bootstrap.
	 * 
	 * @param port
	 *            on which server will be listening
	 */
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
						System.out.println("New client accepted from "
								+ javaChannel.getRemoteAddress());
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

	@Override
	protected EventLoopGroup produceEventGroup() {
		// we need more paralelization on server
		return new EventLoopGroup(4);
	}
}
