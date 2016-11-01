package Recorder;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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

	public void stop(int finishTime) {
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

	public static void main(String[] args) {
		long currentTime = System.currentTimeMillis();
		Recorder recorder = new Recorder();

		try {
			Thread.sleep(2343);
			recorder.record("Piano,G");
			Thread.sleep(2222);
			recorder.record("Drum,A");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		recorder.saveAs("WAHAHA.txt");
		System.out.println("done!");
	}

}