package detty.channel.pipeline;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import detty.channel.Channel;
import detty.channel.InternalChannel;

/**
 * {@link Pipeline} is ordered list of processing handlers for inbound and
 * outbound messages used by abstract {@link InternalChannel} to process messages before
 * proceeding to business logic.
 */
public class Pipeline {
	
	private List<ChannelHandler> handlers = new ArrayList<>();
	private InternalChannel channel;

	/**
	 * Creates new pipeline for given {@link Channel}.
	 * 
	 * @param channel which will be set for pipeline
	 */
	public Pipeline(final InternalChannel channel) {
		this.channel = channel;
	}

	/**
	 * Adds provided handler to last position in {@link Pipeline}.
	 * 
	 * @param handler which is being added
	 */
	public void addLast(ChannelHandler handler) {
		this.handlers.add(handler);
	}

	/**
	 * This method will be called by detty system when new message is ready for reading, shouldn't be called by developer.
	 * 
	 * @param buffer incoming bytes from java channel
	 */
	public void fireChannelRead(ByteBuffer buffer) {
		Object messageToForward = buffer;
		for (int i = 0; i < this.handlers.size(); i++) {
			if (messageToForward == null) {
				// nothing to forward
				return;
			}

			ChannelHandler handler = this.handlers.get(i);
			if (handler.isInput()) {
				@SuppressWarnings("unchecked")
				ChannelInputHandler<Object, ?> inputHandler = (ChannelInputHandler<Object, ?>) handler;
				messageToForward = inputHandler.onMessageReceived(this.channel,
						messageToForward);
			}

		}
	}

	/**
	 * This method will be called by detty system when new message is sent by developer, shouldn't be called manually.
	 * 
	 * @param message which is being sent
	 */
	public void fireChannelWrite(Object message) {
		Object messageToForward = message;
		if (!this.handlers.isEmpty()) {
			for (int i = this.handlers.size() - 1; i >= 0; i--) {
				ChannelHandler handler = this.handlers.get(i);
				if (handler.isOutput()) {
					@SuppressWarnings("unchecked")
					ChannelOutputHandler<Object, Object> outputHandler = (ChannelOutputHandler<Object, Object>) handler;
					messageToForward = outputHandler.onMessageSent(this.channel,
							messageToForward);
				}
			}
		}

		// last output handler must produce byte buffer
		if (messageToForward instanceof ByteBuffer) {
			this.channel.unsafeWrite((ByteBuffer) messageToForward);
		} else {
			System.out.println(
					"Last output handler didn't produce byte buffer aborting.");
		}
	}
}
