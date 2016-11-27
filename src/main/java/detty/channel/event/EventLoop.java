package detty.channel.event;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import detty.channel.Channel;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

public class EventLoop {
	
	private Selector selector;
	private Thread thread;
	private ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();
	
	public EventLoop(final int id) {
		try {
			this.selector = SelectorProvider.provider().openSelector();
			this.thread = new Thread(this::run);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void register(Channel channel) throws ClosedChannelException {
		int allOps = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
		executeInLoop(() ->  {
			try {
				channel.javaChannel().register(this.selector, allOps, channel);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		});
	}

	private void executeInLoop(Runnable task) {
		this.tasks.offer(task);
		this.selector.wakeup();
	}

	public void start() {
		this.thread.start();
	}
	
	private void run() {
		while (true) {
			try {
				int selectedKeys = this.selector.select();
				Iterator<SelectionKey> keysIterator = this.selector.selectedKeys().iterator();
				while (keysIterator.hasNext()) {
					SelectionKey key = pollKey(keysIterator);
					processKey(key, (Channel) key.attachment());
				}
				
				for (Runnable task : this.tasks) {
					task.run();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processKey(SelectionKey key, Channel channel) throws IOException {
		if (key.isConnectable()) {
			channel.finishConnection();
			key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		}
		
		if (key.isReadable()) {
			channel.unsafeRead();
			// drop interest op
			key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
		}
	}

	private static SelectionKey pollKey(Iterator<SelectionKey> keys) {
		SelectionKey key = keys.next();
		keys.remove();
		return key;
	}

}
