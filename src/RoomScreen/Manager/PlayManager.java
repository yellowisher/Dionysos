package RoomScreen.Manager;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayManager {
	public PlayManager() {

	}
	public void play(String instru, String code) {
		File file = new File("Resource/Audio/" + instru + "/" + code + ".wav");
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		} catch (Exception e) {
		}
	}
}