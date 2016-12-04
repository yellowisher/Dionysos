package RoomScreen.Manager;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class PlayManager {
	public PlayManager() {

	}
	
	/*
	 * Clip get by AudioSystem is OS resource, DO NOT store it to map,
	 * have to return it to OS after use
	 * And attach listener to close Clip
	 */
	public void play(String instru, String code) throws Exception {
		System.out.println("Print " + code);
		Clip clip = AudioSystem.getClip();
		clip.open(AudioSystem.getAudioInputStream(new File("Resource/Audio/" + instru + "/" + code + ".wav")));
		clip.start();
		clip.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent e) {
				if (e.getType() == LineEvent.Type.STOP) {
					e.getLine().close();
				}
			}
		});
	}
}