package detty.test.chat.client;

import detty.channel.Channel;
import detty.channel.pipeline.ChannelInputHandler;

public class DisplayChatMessageHandler implements ChannelInputHandler<String, Void> {
	@Override
	public Void onMessageReceived(Channel channel, String message) {
		// some kind of representing the message
		System.out.println(message);
		return null;
	}
}
