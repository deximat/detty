package detty.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import detty.channel.event.EventLoop;

/**
 * {@link InternalChannel} is interface which extends {@link Channel} and is
 * intended to be used by detty sytem only, not to expose unsafe methods to
 * outer world.
 */
public interface InternalChannel extends Channel {

	/**
	 * This is reserved for detty system and shouldn't be used by developer.
	 * Sets assigned {@link EventLoop} for this {@link Channel}.
	 * 
	 * @param eventLoop
	 *            which will be set
	 */
	void usafeSetEventLoop(EventLoop eventLoop);
	
	/**
	 * This method is reserved for detty system only, and shouldn't be used. It
	 * is called when channel is initially connected to server.
	 * 
	 * @param inetSocketAddress
	 *            to which channel should be connected
	 * 
	 * @throws Exception
	 *             in case of any network error
	 */
	void unsafeConnect(InetSocketAddress inetSocketAddress) throws Exception;

	/**
	 * This method is reserved for detty system only, and shouldn't be used. It
	 * is called when channel is successfully connected and needs to finish
	 * connection.
	 * 
	 * @return true, if connection finished succesfully
	 * @throws IOException
	 *             in case of connection closed for some reason
	 */
	boolean unsafeFinishConnection() throws IOException;
	
	/**
	 * This method is reserved for detty system only, and shouldn't be used. It
	 * is called whenever selector finds something useful on java channel.
	 * 
	 * @return true, if read is successful
	 * 
	 * @throws IOException
	 *             in case of channel problem
	 */
	boolean unsafeRead() throws IOException;

	/**
	 * This method is reserved for detty system only, and shouldn't be used. It
	 * is called whenever write operation should be done.
	 * 
	 * @param message
	 *            which will be written
	 */
	void unsafeWrite(ByteBuffer message);

}
