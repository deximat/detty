package detty.channel.pipeline;

import detty.channel.Channel;

/**
 * {@link ChannelInputHandler} will be used inside {@link Pipeline} to process
 * incoming messages.
 *
 * @param <Input>
 *            - type of incoming message
 * @param <Output>
 *            - type of message to which this handler will transform it
 */
public interface ChannelInputHandler<Input, Output> extends ChannelHandler {

	/**
	 * This method will be called every time incoming message passes trough this
	 * handler in {@link Pipeline}.
	 * 
	 * @param channel
	 *            - channel from which message came
	 * @param message
	 *            - message which came for processing
	 * @return - handler should return processed message for further processing
	 *         in pipeline, or just null in case that no more processing is
	 *         needed
	 */
	public Output onMessageReceived(Channel channel, Input message);
}
