package detty.test.chat.client;

import java.nio.ByteBuffer;

import detty.buffers.ByteBufferUtils;
import detty.channel.Channel;
import detty.channel.pipeline.ChannelDuplexHandler;

public class MessageCodecHandler implements ChannelDuplexHandler<ByteBuffer, String> {
	
	@Override
	public String onMessageReceived(Channel channel, ByteBuffer message) {
		return new String(ByteBufferUtils.readAllBytes(message));
	}

	@Override
	public ByteBuffer onMessageSent(Channel channel, String message) {
		return ByteBuffer.wrap(message.getBytes());
	}
	
}
