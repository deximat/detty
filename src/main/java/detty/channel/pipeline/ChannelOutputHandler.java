package detty.channel.pipeline;

import java.nio.ByteBuffer;

import detty.channel.Channel;

/**
 * {@link ChannelOutputHandler} will be used inside {@link Pipeline} to process
 * all outgoing messages.
 *
 * @param <Input>
 *            - type of outgoing message
 * @param <Output>
 *            - type to which message will be transformed by this handler
 * 
 *            Handler nearest to java channel should always convert any message
 *            to {@link ByteBuffer}
 */
public interface ChannelOutputHandler<Input, Output> extends ChannelHandler {

	/**
	 * This method will be called when message that is being send out comes to
	 * this part of {@link Pipeline}.
	 * 
	 * @param channel
	 *            - to which message will be sent
	 * @param message
	 *            - message which is being processed
	 * @return method should return transformed message for further processing
	 *         or {@link ByteBuffer} in case that this is last handler before
	 *         sending message to java channel
	 */
	public Output onMessageSent(Channel channel, Input message);
}
