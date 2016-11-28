package detty.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

import detty.channel.pipeline.Pipeline;


public interface Channel {
	public Pipeline pipeline();
	public SelectableChannel javaChannel();
	public void unsafeRead() throws IOException;
	public void writeAndFlush(byte[] message);
	public void connect(InetSocketAddress inetSocketAddress) throws Exception;
	public boolean finishConnection() throws IOException;
	public boolean isAlive();
}
