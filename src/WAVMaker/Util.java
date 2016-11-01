package WAVMaker;

public class Util {
	static byte[] toLittleEndian(int value) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = (byte) (value & 0xff);
			value >>= 8;
		}
		return bytes;
	}
}
