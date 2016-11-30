package detty.channel.pipeline;

/**
 * {@link ChannelDuplexHandler} is handler for {@link Pipeline} which is both
 * {@link ChannelInputHandler} and {@link ChannelOutputHandler}. Which means
 * that he will have logic which will process incoming and outgoing messages in
 * detty system.
 *
 * @param <Input> - type of input message
 * @param <Output> - type of output message
 * 
 * Types are in reverse order for {@link ChannelOutputHandler}.
 */
public interface ChannelDuplexHandler<Input, Output>
		extends
			ChannelInputHandler<Input, Output>,
			ChannelOutputHandler<Output, Input> {

}
