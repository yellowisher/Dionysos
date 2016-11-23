package RoomScreen.Manager;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayManager {
	HashMap<String, Clip> clipMap = new HashMap<>();

	public PlayManager() {

	}
	public void play(String instru, String code) throws Exception {
		String fileName = "Resource/Audio/" + instru + "/" + code + ".wav";
		if (!clipMap.containsKey(fileName)) {
			File file = new File(fileName);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clipMap.put(fileName, clip);
		}
		Clip clip = clipMap.get(fileName);
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
		clip.start();
	}
}