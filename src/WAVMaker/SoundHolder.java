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
			case 'P' :
				return pianoSound.getSound(noteName);
			case 'D' :
				return drumSound.getSound(noteName);
			case 'G' :
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
		}
	}

	private class DrumSound {
		HashMap<String, FileInputStream> notes;

		DrumSound() {
			notes = new HashMap<String, FileInputStream>();
		}

		public FileInputStream getSound(String noteName) {
			if (!notes.containsKey(noteName)) {
				try {
					FileInputStream fos = new FileInputStream(new File("Resource/Audio/Drum/" + noteName + ".wav"));

					// Cut header
					fos.skip(44);
					notes.put(noteName, fos);
				} catch (Exception e) {
					System.out.println("Cannot find audio file");
					e.printStackTrace();
				}
			}
			return notes.get(noteName);
		}
	}

	private class GuitarSound {

	}
}
