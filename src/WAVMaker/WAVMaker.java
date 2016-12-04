package WAVMaker;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class WAVMaker {
	final static int FADE_TIME = 12000;

	private int numChannel = 1;
	int sampleRate = 44100;
	int bitPerSample = 16;

	int dataLength;
	byte[] dataBytes;

	HashMap<String, byte[]> noteMap;

	FileInputStream history;

	// Read recored time from history text file
	private int readTime() throws IOException {
		byte[] time = new byte[9];
		history.read(time, 0, 9);
		history.skip(1);
		String[] str = new String(time).split(":");

		int milliseconds = Integer.parseInt(str[0]) * 60 * 1000;
		milliseconds += Integer.parseInt(str[1]) * 1000;
		milliseconds += Integer.parseInt(str[2]);
		return milliseconds;
	}

	// Read protocol from history text file
	String readMsg() throws IOException {
		byte[] msg = new byte[6];
		history.read(msg, 0, 6);
		history.skip(2);
		return new String(msg);
	}

	// Convert time to index
	int toIndex(int time) {
		int index = (int) Math.ceil(time * 0.001 * sampleRate * (bitPerSample / 8) * numChannel);
		if (index % 2 == 1) index++;
		return index;
	}

	public WAVMaker(FileInputStream stream) {
		history = stream;
		noteMap = new HashMap<String, byte[]>();

		try {
			int duration = readTime();
			history.skip(2);
			dataLength = toIndex(duration);
			System.out.println(dataLength);
			dataBytes = new byte[dataLength];
		} catch (IOException e) {
			System.out.println("Unknown history file");
			e.printStackTrace();
		}
	}

	private byte[] getSound(Character type, String noteName) {
		if (noteMap.containsKey(type + noteName)) return noteMap.get(type + noteName);

		String inst = null;
		switch (type) {
			case 'P' :
				inst = "Piano/";
				break;
			case 'D' :
				inst = "Drum/";
				break;
			case 'G' :
				inst = "Guitar/";
				break;
		}

		try {
			FileInputStream fis = new FileInputStream(new File("Resource/Audio/" + inst + noteName + ".wav"));
			fis.skip(44);
			byte[] copy = new byte[fis.available()];
			fis.read(copy);
			noteMap.put(type + noteName, copy);
			fis.close();
			return copy;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void createWAV() {
		try {
			while (history.available() > 0) {
				int time = (readTime());
				String msg = readMsg();
				String note = msg.substring(3);

				switch (msg.charAt(0)) {

					// Case of Piano
					case 'P' :
						if (msg.charAt(1) == 'D') {
							write(getSound(msg.charAt(0), note), time);
						}
						break;

					// Case of Drum
					case 'D' :
						write(getSound(msg.charAt(0), note), time);
						break;

					// Case of Guitar
					case 'G' :
						write(getSound(msg.charAt(0), note), time);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Write a note on empty(output) file
	private void write(byte[] data, int time) {
		int start = toIndex(time);

		for (int i = 0; i < data.length; i += 2) {

			if (start + i + 1 >= dataLength) return;

			// In default setting, our WAV file uses 16bits(2bytes) for sample size.
			// And it is stored in little endian. So get each bytes, convert to big
			// endian, calculate(add) and convert back to little endian,
			// store them to WAV file.
			short orig_0 = (short) (dataBytes[start + i] & 0xff);
			short orig_1 = (short) ((dataBytes[start + i + 1] & 0xff) << 8);
			orig_0 |= orig_1;

			short addi_0 = (short) (data[i] & 0xff);
			short addi_1 = (short) ((data[i + 1] & 0xff) << 8);
			addi_0 |= addi_1;

			// Remove clippings; Thanks stackOverflow!
			float sample0f = orig_0 / 32768.0f;
			float sample1f = addi_0 / 32768.0f;
			float mixed = sample0f + sample1f * 0.8f;

			if (mixed > 0.7f) mixed = 0.7f;
			else if (mixed < -0.7f) mixed = -0.7f;

			orig_0 = (short) (mixed * 32768.0f);

			dataBytes[start + i] = (byte) orig_0;
			dataBytes[start + i + 1] = (byte) (orig_0 >> 8);
		}
	}

	public void save(String name) {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(name));

			byte[] header;
			WAVHeader headerMaker = new WAVHeader(dataLength);
			header = headerMaker.getHeader();

			dos.write(header);
			dos.write(dataBytes);
			dos.close();
		} catch (FileNotFoundException e) {
			// cannot create a file
			e.printStackTrace();
		} catch (IOException e) {
			// error during writing a file
			e.printStackTrace();
		}
	}
}
