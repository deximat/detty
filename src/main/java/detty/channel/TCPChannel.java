package detty.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicReference;

import detty.channel.event.EventLoop;
import detty.channel.pipeline.Pipeline;

public class TCPChannel implements InternalChannel {

	private static final int INPUT_CAPACITY = 256 * 1024;
	private final SocketChannel javaChannel;
	private final Pipeline pipeline;
	private AtomicReference<Runnable> onChannelClosedListener = new AtomicReference<>();
	private AtomicReference<EventLoop> eventLoop = new AtomicReference<>();

	public TCPChannel(SocketChannel channel) throws IOException {
		this.javaChannel = channel;
		this.javaChannel.configureBlocking(false);
		this.pipeline = new Pipeline(this);
	}

	public TCPChannel() throws IOException {
		this(SocketChannel.open());
	}

	@Override
	public Pipeline pipeline() {
		return this.pipeline;
	}

	@Override
	public SocketChannel javaChannel() {
		return this.javaChannel;
	}

	@Override
	public boolean unsafeRead() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(INPUT_CAPACITY);
		int status = javaChannel().read(buffer); // TODO: handle number of bytes
													// read
		if (status == -1) {
			close();
			System.out.println("channel closed: " + this);
			return false;
		}
		buffer.flip();
		pipeline().fireChannelRead(buffer);
		return false;
	}

	private void close() throws IOException {
		javaChannel().close();
		Runnable channelClosedListener = this.onChannelClosedListener.get();
		if (channelClosedListener != null) {
			channelClosedListener.run();
		}
	}

	@Override
	public void write(Object message) {
		EventLoop loop = this.eventLoop.get();
		if (loop != null) {
			loop.submitTask(() -> {
				this.pipeline.fireChannelWrite(message);
			});
		} else {
			System.out.println("Event loop doesn't exist.");
		}
	}

	@Override
	public void unsafeConnect(InetSocketAddress inetSocketAddress)
			throws IOException {
		javaChannel().connect(inetSocketAddress);
	}

	@Override
	public boolean unsafeFinishConnection() throws IOException {
		return this.javaChannel.finishConnect();
	}

	@Override
	public boolean isAlive() {
		return this.javaChannel.isOpen();
	}

	@Override
	public void setChannelClosedListener(final Runnable onChannelClosed) {
		this.onChannelClosedListener.set(onChannelClosed);
	}

	@Override
	public void usafeSetEventLoop(EventLoop eventLoop) {
		this.eventLoop.set(eventLoop);
	}

	@Override
	public void unsafeWrite(ByteBuffer message) {
		try {
			this.javaChannel.write(message);
		}  catch (ClosedChannelException e) {
			try {
				close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName()).append("(");

		builder.append(this.javaChannel.socket().getInetAddress()).append(")");

		return builder.toString();
	}

	@Override
	public void kill() {
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
