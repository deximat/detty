package detty.test.chat.client;

import java.nio.ByteBuffer;

import detty.buffers.ByteBufferUtils;
import detty.channel.Channel;
import detty.channel.ChannelHandler;

public class MessageReceivedHandler extends ChannelHandler {
		
	@Override
	public void onMessageReceived(Channel channel, ByteBuffer buffer) {
		// some kind of parsing message
		String message = new String(ByteBufferUtils.readAllBytes(buffer));
		
		// some kind of representing the message
		System.out.println(message);
	}
	
}
