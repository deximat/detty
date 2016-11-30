package detty.channel;

/**
 * {@link AbstractChannelInitializer} is interface which should be implemented by developer to handle {@link Channel} initialization.
 * 
 * @param <C> type of channel 
 */
public interface AbstractChannelInitializer<C extends Channel> {
	
	/**
	 * This method will be called on creation of new {@link Channel}.
	 * 
	 * @param channel on which method will be applied
	 * 
	 * @throws Exception which can be thrown by business logic
	 */
	void init(C channel) throws Exception;
}
