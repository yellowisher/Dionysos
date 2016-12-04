package Recorder;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/*
 * Recorder, actually it records protocols, not the sound
 * save it as .txt file and WAVMaker creates a .wav file
 */
public class Recorder {

	String buffer;
	SimpleDateFormat format;
	long startTime;

	public Recorder() {
		buffer = "";
		format = new SimpleDateFormat("mm:ss:SSS ");
		startTime = System.currentTimeMillis();
	}

	public void record(String str) {
		long ms = System.currentTimeMillis() - startTime;
		buffer += format.format(ms);
		buffer += str + "\r\n";
	}

	public void stop() {
		long finishTime = System.currentTimeMillis() - startTime;
		buffer = format.format(finishTime) + "\r\n" + buffer;
	}

	public void saveAs(String fileName) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.write(buffer);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Cannot create file!");
			e.printStackTrace();
		}
	}
}