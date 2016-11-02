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

	int numChannel = 1;
	int sampleRate = 44100;
	int bitPerSample = 16;

	int dataLength;
	byte[] dataBytes;

	FileInputStream history;
	HashMap<String, Integer> noteMap;
	SoundHolder soundHolder;

	int readTime() throws IOException {
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
		noteMap = new HashMap<String, Integer>();
		soundHolder = new SoundHolder();

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
		FileInputStream origSrc = soundHolder.getSound(type, noteName);

		try {
			int length = origSrc.available();
			byte[] copy = new byte[length];
			origSrc.read(copy);
			return copy;
		} catch (IOException e) {
			e.printStackTrace();
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
					// Main idea : when key(note) is down, store it to map.
					// When key is up, if that key(note) exists in map, pop it out and
					// write on empty file.
					// And if key is up earlier than its(note) play time, fade out
					// it.
					case 'P' :
						switch (msg.charAt(1)) {

							// If piano key is up, fade out note according to key
							// up/down time
							case 'U' :
								if (noteMap.containsKey(note)) {
									byte[] origNote = getSound(msg.charAt(0), note);
									int keyDownTime = noteMap.get(note);
									noteMap.remove(note);
									byte[] newNote = fadeOut(origNote, time - keyDownTime);
									write(newNote, keyDownTime);
								}
								noteMap.remove(note);
								break;

							// If piano key is down, put it into map with current time
							case 'D' :
								noteMap.put(note, time);
								break;
						}
						break;

					// Case of Drum
					case 'D' :

						// Case of Guitar
					case 'G' :
				}
			}

			// Write unmerged notes of piano
			for (String noteName : noteMap.keySet()) {
				write(getSound('P', noteName), noteMap.get(noteName));
			}
			noteMap.clear();
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

			orig_0 += addi_0;
			dataBytes[start + i] = (byte) orig_0;
			dataBytes[start + i + 1] = (byte) (orig_0 >> 8);
		}
	}

	// Fade out given note
	private byte[] fadeOut(byte[] note, int fadeStart) {
		int start = toIndex(fadeStart);
		if (start + FADE_TIME > note.length) return note;

		for (int i = 0; i < FADE_TIME; i += 2) {
			short orig_0 = (short) (note[start + i] & 0xff);
			short orig_1 = (short) ((note[start + i + 1] & 0xff) << 8);
			orig_0 |= orig_1;

			orig_0 *= (float) (FADE_TIME - i) / FADE_TIME;

			note[start + i] = (byte) orig_0;
			note[start + i + 1] = (byte) (orig_0 >> 8);
		}
		byte[] newNote = new byte[start + FADE_TIME];
		System.arraycopy(note, 0, newNote, 0, start + FADE_TIME);
		return newNote;
	}

	public void save() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream("Output.wav"));

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

	public static void main(String[] args) throws Exception {
		WAVMaker wavMaker = new WAVMaker(new FileInputStream(new File("TEST.txt")));
		wavMaker.createWAV();
		wavMaker.save();
	}
}
