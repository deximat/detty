package detty.channel.pipeline;

/**
 * {@link ChannelHandler} is the most abstract handler which will be used inside
 * {@link Pipeline} to process messages. It's implementation only contains flag
 * methods to check if he is input or output handler (or both).
 */
public interface ChannelHandler {

	/**
	 * @return true, if channel should process incoming messages
	 */
	default boolean isInput() {
		return this instanceof ChannelInputHandler;
	}

	/**
	 * @return true, if channel should process outgoing messages
	 */
	default boolean isOutput() {
		return this instanceof ChannelOutputHandler;
	}
	
}
