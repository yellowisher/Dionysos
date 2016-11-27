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
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
			clipMap.put(fileName, clip);
		}

		/*
		 * Before playing clip, it must be rewound. But call clip.stop() and
		 * clip.flush() sometimes fails to rewind,
		 * 
		 * I didn't check for implementation of Clip class, so I'm not sure what
		 * happened, but I think start() clip just after set position, sometimes
		 * start() starts faster than rewinding done. (because Clip class uses
		 * thread innerly)
		 * 
		 * So busy wait for rewinding is done, but I'm not sure bug is totally gone
		 */
		Clip clip = clipMap.get(fileName);
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
		while (clip.getFramePosition() != 0) {
			System.out.println(code + " is waiting");
		}
		System.out.println("Start " + code);
		clip.start();
	}
}