package detty.channel.event;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicInteger;

import detty.channel.Channel;

public class EventLoopGroup {
	private final EventLoop[] loops;
	private final AtomicInteger nextLoop = new AtomicInteger();
	
	public EventLoopGroup(int loopCount) {
		
		this.loops = new EventLoop[loopCount];
		for (int i = 0; i < loopCount; i++) {
			this.loops[i] = new EventLoop(i);
			this.loops[i].start();
		}
	}
	
	private EventLoop nextLoop() {
		int nextIndex = this.nextLoop.updateAndGet((value) -> {
			return (value + 1) % this.loops.length;
		});
		return this.loops[nextIndex];
	}
	
	public void register(Channel channel) throws ClosedChannelException {
		nextLoop().register(channel);
	}
}
