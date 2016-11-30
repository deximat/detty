package detty.channel.pipeline.coder;

import java.nio.ByteBuffer;

import detty.buffers.ByteBufferUtils;
import detty.channel.Channel;
import detty.channel.pipeline.ChannelInputHandler;

/**
 * {@link StringDecoderHandler} shows one way transformation of {@link ByteBuffer} to {@link String}.
 */
public class StringDecoderHandler implements ChannelInputHandler<ByteBuffer, String> {
	@Override
	public String onMessageReceived(Channel channel, ByteBuffer message) {
		return new String(ByteBufferUtils.readAllBytes(message));
	}
}
