package detty.channel.pipeline.security;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import detty.channel.Channel;
import detty.channel.pipeline.ChannelInputHandler;

/**
 * {@link IncomingBurstControlHandler} controls that users don't spam the server.
 */
public class IncomingBurstControlHandler implements ChannelInputHandler<ByteBuffer, ByteBuffer> {
	
	private final long burstLimit;
	private final long intervalInMillis;
	
	/**
	 * Creates new burst control handler.
	 * 
	 * @param burstLimit - number of messages allowed per duration of time unit provided
	 * @param duration - duration in which burst will be considered
	 * @param unit - time unit of duration
	 */
	public IncomingBurstControlHandler(final long burstLimit, final long duration, final TimeUnit unit) {
		this.burstLimit = burstLimit;
		this.intervalInMillis = unit.toMillis(duration);
	}
	
	private long burstStartTimestamp = 0;
	private long burstCount = 0;
	
	@Override
	public ByteBuffer onMessageReceived(Channel channel, ByteBuffer message) {
		long now = System.currentTimeMillis();
		if (this.burstStartTimestamp + this.intervalInMillis < now) {
			this.burstCount++;
			
			if (this.burstCount > burstLimit) {
				System.out.println("User is spamming, bye bye");
				channel.kill();
				
				// we no longer want to process this message
				return null;
			}
		} else {
			// burst expired, reset
			this.burstStartTimestamp = now;
			this.burstCount = 1;
		}
		
		// we won't touch message if everything is ok
		return message;
	}

}
