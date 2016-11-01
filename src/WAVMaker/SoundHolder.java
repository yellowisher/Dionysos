package WAVMaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundHolder {
	public PianoSound pianoSound;
	public DrumSound drumSound;
	public GuitarSound guitarSound;

	public SoundHolder() {
		pianoSound = new PianoSound();
		drumSound = new DrumSound();
		guitarSound = new GuitarSound();
	}

	public FileInputStream getSound(Character type, String noteName) {
		switch (type) {
		case 'P':
			return pianoSound.getSound(noteName);
		case 'D':
		case 'G':
		}
		System.out.println("Unknown note!");
		return null;
	}

	private class PianoSound {
		HashMap<String, FileInputStream> notes;

		PianoSound() {
			notes = new HashMap<String, FileInputStream>();
		}

		public FileInputStream getSound(String noteName) {

			switch (noteName) {
			case "C_0":
			case "C#0":
			case "D_0":
			case "D#0":
			case "E_0":
			case "F_0":
			case "F#0":
			case "G_0":
			case "G#0":
			case "A_0":
			case "A#0":
			case "B_0":
			case "C_1":
			case "C#1":
			case "D_1":
			case "D#1":
			case "E_1":
				if (!notes.containsKey(noteName)) {
					try {
						FileInputStream fos = new FileInputStream(new File("Resource/Audio/Piano/" + noteName + ".wav"));

						// Cut header
						fos.skip(44);
						notes.put(noteName, fos);
					} catch (Exception e) {
						System.out.println("Cannot find audio file");
						e.printStackTrace();
					}
				}
				return notes.get(noteName);

			default:
				System.out.println("Wrong note!");
				return null;
			}
		}
	}

	private class DrumSound {

	}

	private class GuitarSound {

	}
}
