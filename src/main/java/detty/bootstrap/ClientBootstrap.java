package detty.bootstrap;

import java.net.InetSocketAddress;

import detty.channel.Channel;
import detty.channel.event.EventLoopGroup;

/**
 * {@link ClientBootstrap} is easy to use creating of {@link Channel} for
 * connecting to some server.
 */
public final class ClientBootstrap extends AbstractBootstrap<ClientBootstrap> {

	/**
	 * Creates channel defined by this bootsrap, and connects it to provided
	 * address and port in parameters.
	 * 
	 * @param address
	 *            to which this channel will be connected
	 * @param port
	 *            to which this channel will be connected
	 * 
	 * @return newly created channel connected to provided address/port
	 * 
	 * @throws Exception
	 *             in case of connection failure
	 */
	public Channel connect(String address, int port) throws Exception {
		return connect(new InetSocketAddress(address, port));
	}

	@Override
	protected EventLoopGroup produceEventGroup() {
		// since there is only one client we don't need that much threads
		return new EventLoopGroup(1);
	}

}
