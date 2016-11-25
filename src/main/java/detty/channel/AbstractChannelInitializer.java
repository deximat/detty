package detty.channel;

public abstract class AbstractChannelInitializer<C extends Channel> {

	public abstract void init(C channel) throws Exception;
}
