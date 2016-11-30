package detty.channel.event;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import detty.channel.InternalChannel;

/**
 * {@link EventLoop} is single threaded {@link Selector} executor. Idea is that
 * one {@link EventLoop} handles multiple clients and when they are asigned to
 * {@link EventLoop}, they are bound to it until their death. That simplifies
 * concurrency problems per user session and gives nice interface to developer,
 * since all things related to one connection will be executed by the same
 * thread.
 */
public class EventLoop {

	private Selector selector;
	private Thread thread;
	private ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

	/**
	 * Creates new EventLoop with provided id.
	 * 
	 * Shouldn't be created outside of {@link EventLoopGroup} that's why it is
	 * package visibility.
	 * 
	 * @param id
	 *            of event loop
	 */
	EventLoop(final int id) {
		try {
			this.selector = SelectorProvider.provider().openSelector();
			this.thread = new Thread(this::run);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void register(InternalChannel channel) throws ClosedChannelException {
		int allOps = SelectionKey.OP_CONNECT | SelectionKey.OP_READ
				| SelectionKey.OP_WRITE;
		submitTask(() -> {
			try {
				if (channel.isAlive()) {
					channel.javaChannel().register(this.selector, allOps,
							channel);
					channel.usafeSetEventLoop(this);
				} else {
					System.out.println(
							"Tried to register but channel was closed.");
				}
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Submits runnable task to be executed on {@link EventLoop}'s thread,
	 * shouldn't be used by developer but only by detty system.
	 * 
	 * @param task which will be executed on {@link EventLoop}'s thread
	 */
	public void submitTask(Runnable task) {
		this.tasks.offer(task);
		this.selector.wakeup();
	}

	void start() {
		this.thread.start();
	}

	private void run() {
		while (true) {
			try {
				this.selector.select();
				Iterator<SelectionKey> keysIterator = this.selector
						.selectedKeys().iterator();
				while (keysIterator.hasNext()) {
					SelectionKey key = pollKey(keysIterator);
					processKey(key, (InternalChannel) key.attachment());
				}
				while (this.tasks.size() > 0) {
					this.tasks.poll().run();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processKey(SelectionKey key, InternalChannel channel)
			throws IOException {
		if (key.isConnectable()) {
			channel.unsafeFinishConnection();
			key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		}

		if (key.isReadable()) {
			channel.unsafeRead();
		}
	}

	private static SelectionKey pollKey(Iterator<SelectionKey> keys) {
		SelectionKey key = keys.next();
		keys.remove();
		return key;
	}

}
