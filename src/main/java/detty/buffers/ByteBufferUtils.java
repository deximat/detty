package detty.buffers;

import java.nio.ByteBuffer;

/**
 * Library class with helper methods related to byte buffers.
 */
public class ByteBufferUtils {
	
	/**
	 * Reads all bytes from provided {@link ByteBuffer}.
	 * 
	 * @param buffer from which reading is done
	 * 
	 * @return byte[] with read bytes
	 */
	public static byte[] readAllBytes(final ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.remaining()]; 
		buffer.get(bytes);
		return bytes;
	}
}
