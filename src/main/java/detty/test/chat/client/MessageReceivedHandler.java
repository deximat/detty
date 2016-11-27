package detty.test.chat.client;

import java.nio.ByteBuffer;

import detty.channel.Channel;
import detty.channel.ChannelHandler;

public class MessageReceivedHandler extends ChannelHandler {
		
	@Override
	public void onMessageReceived(Channel channel, ByteBuffer buffer) {
		// some kind of parsing message
		byte[] bytes = new byte[buffer.remaining()]; // create a byte array the length of the number of bytes written to the buffer
		buffer.get(bytes); // read the bytes that were written
		String message = new String(bytes);
		
		// some kind of representing the message
		System.out.println(message);
	}
	
}
