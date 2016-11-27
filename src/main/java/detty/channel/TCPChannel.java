package detty.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import detty.channel.pipeline.Pipeline;

public class TCPChannel implements Channel {

	private static final int INPUT_CAPACITY = 256 * 1024;
	private final SocketChannel javaChannel;
	private final Pipeline pipeline;
	
	public TCPChannel() throws IOException {
		this.javaChannel = SocketChannel.open();
		this.javaChannel.configureBlocking(false);
		this.pipeline = new Pipeline(this);
	}
	
	public Pipeline pipeline() {
		return this.pipeline;
	}

	public SocketChannel javaChannel() {
		return this.javaChannel;
	}

	@Override
	public void unsafeRead() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(INPUT_CAPACITY);
		int status = javaChannel().read(buffer); //TODO: handle number of bytes read
		if (status == -1) {
			javaChannel().close();
			return;
		}
		buffer.flip();
		pipeline().fireChannelRead(buffer);
	}

	@Override
	public void writeAndFlush(byte[] message) {
		try {
			javaChannel().write(ByteBuffer.wrap(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect(InetSocketAddress inetSocketAddress) throws IOException {
		javaChannel().connect(inetSocketAddress);
	}

	@Override
	public boolean finishConnection() throws IOException {
		return this.javaChannel.finishConnect();
	}

}
