package WAVMaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.omg.CORBA.PRIVATE_MEMBER;

public class WAVMaker {
	final static int FADE_TIME = 12000;

	private int numChannel = 1;
	int sampleRate = 44100;
	int bitPerSample = 16;

	int dataLength;
	byte[] dataBytes;

	HashMap<String, byte[]> noteMap;

	FileInputStream history;
	SoundHolder soundHolder;

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

	String readMsg() throws IOException {
		byte[] msg = new byte[6];
		history.read(msg, 0, 6);
		history.skip(2);
		return new String(msg);
	}

	int toIndex(int time) {
		int index = (int) Math.ceil(sampleRate * bitPerSample / 8 * numChannel * time * 0.001);
		if (index % 2 == 1) index++;
		return index;
	}

	public WAVMaker(FileInputStream stream) {
		history = stream;
		soundHolder = new SoundHolder();
		noteMap = new HashMap<String, byte[]>();

		try {
			int duration = readTime();
			history.skip(2);
			dataLength = toIndex(duration);
			dataBytes = new byte[dataLength];
		} catch (IOException e) {
			System.out.println("Unknown history file");
			e.printStackTrace();
		}
	}

	private byte[] getSound(Character type, String noteName) {
		if (noteMap.containsKey(type + noteName)) return noteMap.get(type + noteName);

		FileInputStream origSrc = soundHolder.getSound(type, noteName);
		try {
			int length = origSrc.available();
			byte[] copy = new byte[length];
			origSrc.read(copy);
			noteMap.put(type + noteName, copy);
			return copy;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ERROR: WAVMaker returns null");
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
					// Main idea : when key(note) is down, store it to map.
					// When key is up, if that key(note) exists in map, pop it out and
					// write on empty file.
					// And if key is up earlier than its(note) play time, fade out
					// it.
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

			// For debug
			if (start + i + 1 >= dataLength) {
				System.out.println("Index error in write() function");
				return;
			}

			// In default setting, our WAV file uses 16bits(2bytes) for sample
			// size.
			// And it is stored in little endian. So get each bytes, convert to
			// big
			// endian, calculate(add) and convert back to little endian,
			// store them to WAV file.
			short orig_0 = (short) (dataBytes[start + i] & 0xff);
			short orig_1 = (short) ((dataBytes[start + i + 1] & 0xff) << 8);
			orig_0 |= orig_1;

			short addi_0 = (short) (data[i] & 0xff);
			short addi_1 = (short) ((data[i + 1] & 0xff) << 8);
			addi_0 |= addi_1;

			//orig_0 += addi_0;
			float sample0f = orig_0 / 32768.0f;
			float sample1f = addi_0 / 32768.0f;
			float mixed = sample0f + sample1f * 0.8f;

			if (mixed > 0.7f) {
				System.out.println("HIGH");
				mixed = 0.7f;
			}
			else if (mixed < -0.7f) {
				System.out.println("LOW");
				mixed = -0.7f;
			}

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
			// error during write a file
			e.printStackTrace();
		}
	}
}
