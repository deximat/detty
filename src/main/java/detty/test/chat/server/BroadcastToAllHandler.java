package detty.test.chat.server;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import detty.buffers.ByteBufferUtils;
import detty.channel.Channel;
import detty.channel.pipeline.ChannelInputHandler;

public class BroadcastToAllHandler implements ChannelInputHandler<ByteBuffer, Void> {
	
	private final ConcurrentLinkedQueue<Channel> allUsers;

	public BroadcastToAllHandler(ConcurrentLinkedQueue<Channel> allUsers) {
		this.allUsers = allUsers;
	}

	@Override
	public Void onMessageReceived(Channel channel, ByteBuffer buffer) {
		System.out.println("Forwaring chat message to " + this.allUsers.size() + " users.");
		byte[] message = ByteBufferUtils.readAllBytes(buffer);
		for (Channel userChannel : this.allUsers) {
			userChannel.write(ByteBuffer.wrap(message));
		}
		return null;
	}

}
