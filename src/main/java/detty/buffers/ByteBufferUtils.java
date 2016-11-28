package detty.buffers;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
	public static byte[] readAllBytes(ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.remaining()]; 
		buffer.get(bytes);
		return bytes;
	}
}
