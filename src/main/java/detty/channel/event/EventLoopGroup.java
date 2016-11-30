package detty.channel.event;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicInteger;

import detty.channel.InternalChannel;

/**
 * {@link EventLoopGroup} contains N loops which results in N parallelization of
 * client processing. This class assigns {@link InternalChannel}s to some of
 * {@link EventLoop} by provided strategy (currently only round robin
 * implemented).
 */
public class EventLoopGroup {
	private final EventLoop[] loops;
	private final AtomicInteger nextLoop = new AtomicInteger();

	/**
	 * Creates new {@link EventLoopGroup} with provided number of {@link EventLoop}s.
	 * 
	 * @param loopCount is number of event loops which will be created
	 */
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

	/**
	 * Registers {@link InternalChannel} to one of {@link EventLoop}s in group.
	 * 
	 * @param channel which will be assigned to one {@link EventLoop}
	 * 
	 * @throws ClosedChannelException will be thrown if channel is closed when we try to register it
	 */
	public void register(InternalChannel channel) throws ClosedChannelException {
		nextLoop().register(channel);
	}
}
