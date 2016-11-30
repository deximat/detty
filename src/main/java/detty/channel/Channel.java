package detty.channel;

import java.nio.channels.SelectableChannel;

import detty.channel.pipeline.Pipeline;

/**
 * {@link Channel} is higher level abstraction on java channels, which gives
 * more business logic friendly interface for communcation with network. Channel
 * has it's pipeline, so it can handle messages of any type and transform them
 * so that they are socket friendly on one side and business logic friendly on
 * the other side of the {@link Pipeline}.
 */
public interface Channel {

	/**
	 * Pipeline is list of handlers which transform all incoming/outgoing
	 * messages.
	 * 
	 * @return pipeline which is applied on this channel
	 */
	Pipeline pipeline();

	/**
	 * If needed it is possible to access backing java channel.
	 * 
	 * @return backing java channel
	 */
	SelectableChannel javaChannel();

	/**
	 * Writes any business logic message to {@link Channel}, under condition
	 * that {@link Pipeline} know how to transform it.
	 * 
	 * @param message
	 *            which will be sent to channel
	 */
	void write(Object message);

	/**
	 * Checks if underlying java channel is alive.
	 * 
	 * @return true, if alive
	 */
	boolean isAlive();

	/**
	 * Possibility to set listener on channel closed event.
	 * 
	 * @param onChannelClosed
	 *            will be called if connection is closed on this channel
	 */
	void setChannelClosedListener(Runnable onChannelClosed);

	/**
	 * Kills channel.
	 */
	void kill();


}
