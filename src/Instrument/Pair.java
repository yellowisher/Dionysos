package Instrument;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;

class Pair {
	JLabel label;
	Clip clip;

	Pair(JLabel label, Clip clip) {
		this.label = label;
		this.clip = clip;
	}
}
