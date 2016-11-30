package detty.channel.pipeline.coder;

import java.nio.ByteBuffer;

import detty.channel.Channel;
import detty.channel.pipeline.ChannelOutputHandler;

/**
 * {@link StringEncoderHandler} shows one way transformation of {@link String} to {@link ByteBuffer}.
 */
public class StringEncoderHandler implements ChannelOutputHandler<String, ByteBuffer> {
	@Override
	public ByteBuffer onMessageSent(Channel channel, String message) {
		return ByteBuffer.wrap(message.getBytes());
	}
}
