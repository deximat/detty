package detty.test.chat.server;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import detty.buffers.ByteBufferUtils;
import detty.channel.Channel;
import detty.channel.ChannelHandler;

public class BroadcastToAllHandler extends ChannelHandler {	
	private final ConcurrentLinkedQueue<Channel> allUsers;

	public BroadcastToAllHandler(ConcurrentLinkedQueue<Channel> allUsers) {
		this.allUsers = allUsers;
	}

	@Override
	public void onMessageReceived(Channel channel, ByteBuffer buffer) {
		System.out.println("Forwaring chat message to " + this.allUsers.size() + " users.");
		byte[] message = ByteBufferUtils.readAllBytes(buffer);
		for (Channel userChannel : this.allUsers) {
			userChannel.writeAndFlush(message);
		}
	}

}
