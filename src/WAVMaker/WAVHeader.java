package WAVMaker;

public class WAVHeader {
	static final byte[] RIFF = { 'R', 'I', 'F', 'F' };
	static final byte[] WAVE = { 'W', 'A', 'V', 'E' };
	static final byte[] FMT = { 'f', 'm', 't', ' ' };
	static final byte[] DATA = {'d', 'a', 't', 'a'};

	byte[] header;
	int dataLength;
	int sampleRate;
	int bitPerSample;
	int numChannel;

	public WAVHeader(int dataLength) {
		header = new byte[44];
		this.dataLength = dataLength;
		this.sampleRate = 44100;
		this.bitPerSample = 16;
		this.numChannel = 1;
	}

	// Second version of constructor for further update (will we?)
	public WAVHeader(int dataLength, int sampleRate, int bitPerSample, int numChannel) {
		header = new byte[44];
		this.dataLength = dataLength;
		this.sampleRate = sampleRate;
		this.bitPerSample = bitPerSample;
		this.numChannel = numChannel;
	}

	public byte[] getHeader() {
		// Fills WAV header for given information.
		// standard for WAV header -> http://soundfile.sapp.org/doc/WaveFormat/

		// Chunk ID: fixed ASCII value "RIFF"
		System.arraycopy(RIFF, 0, header, 0, 4);

		// Chunk Size: left data size (data size + 36)
		System.arraycopy(Util.toLittleEndian(dataLength + 36), 0, header, 4, 4);

		// Format: fixed ASCII value "WAVE"
		System.arraycopy(WAVE, 0, header, 8, 4);

		// SubChunk ID: fixed ASCII value "fmt "
		System.arraycopy(FMT, 0, header, 12, 4);

		// SubChunk Size: left header size (fixed value, 16)
		System.arraycopy(Util.toLittleEndian(16), 0, header, 16, 4);

		// Audio format: usually fixed value 1
		System.arraycopy(Util.toLittleEndian(1), 0, header, 20, 2);

		// Number of Channel: mono -> 1, stereo -> 2
		System.arraycopy(Util.toLittleEndian(numChannel), 0, header, 22, 2);

		// Sample rate
		System.arraycopy(Util.toLittleEndian(sampleRate), 0, header, 24, 4);

		// Byte rate
		System.arraycopy(Util.toLittleEndian(sampleRate * numChannel * bitPerSample / 8), 0, header, 28, 4);

		// Block Align: number of channel * sample size
		System.arraycopy(Util.toLittleEndian(numChannel * bitPerSample / 8), 0, header, 32, 2);

		// Bit per sample
		System.arraycopy(Util.toLittleEndian(bitPerSample), 0, header, 34, 2);

		// SubChunk2 ID: fixed ASCII value "data"
		System.arraycopy(DATA, 0, header, 36, 4);

		// SubChunk2 Size: Actual data size.
		System.arraycopy(Util.toLittleEndian(dataLength), 0, header, 40, 4);

		return header;
	}
}
