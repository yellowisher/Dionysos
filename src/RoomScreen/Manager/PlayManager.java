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
	public void play(String instru, String code) throws Exception {
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

		/*
		 * Before playing clip, it must be rewound. But call clip.stop() and
		 * clip.flush() sometimes fails to rewind,
		 * 
		 * I didn't check for implementation of Clip class, so I'm not sure what
		 * happened, but I think start() clip just after set position, sometimes
		 * start() starts faster than rewinding done. (because Clip class uses
		 * thread innerly)
		 * 
		 * So busy wait for rewinding is done, but I'm not sure bug is totally
		 * gone
		 */
	}
}