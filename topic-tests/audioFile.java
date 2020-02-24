
/* BitterliStudios 2020
 * audioFile.java
 * 
 * The actual audio file object and it's playback methods.
 * Only supports wav and au files.
 */

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class audioFile {
	private Long currentFrame;
	private Clip clip;
	private String status;
	private AudioInputStream audioInputStream;
	private String filePath;
	private boolean loop = false;
	private boolean opened = false;

	// constructor to initialize streams and clip
	public audioFile(String filePathway) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		filePath = "Jazz-Album\\" + filePathway;
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

		clip = AudioSystem.getClip();

		clip.open(audioInputStream);
	}

	public void play() {
		clip.start();
		status = "play";
	}

	public void pause() {
		if (!status.equals("paused")) {
			this.currentFrame = this.clip.getMicrosecondPosition();
			clip.stop();
			status = "paused";
		}
	}

	public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (!status.equals("play")) {
			clip.close();
			resetAudioStream();
			clip.setMicrosecondPosition(currentFrame);
			this.play();
		}
	}

	public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		clip.stop();
		clip.close();
		resetAudioStream();
		currentFrame = 0L;
		clip.setMicrosecondPosition(0);
		this.play();
	}

	public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		currentFrame = 0L;
		clip.stop();
		clip.close();
		opened = true;
	}

	public void jump(long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (c > 0 && c < clip.getMicrosecondLength()) {
			clip.stop();
			clip.close();
			resetAudioStream();
			currentFrame = c;
			clip.setMicrosecondPosition(c);
			this.play();
		}
	}

	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		clip.open(audioInputStream);
		// clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void loop(boolean state) {
		if (state) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.loop(0);
		}
		if (loop) {
			loop = false;
		} else {
			loop = true;
		}
	}

	public boolean isLooping() {
		return loop;
	}

	public boolean isDone() {
		return (clip.getMicrosecondLength() == clip.getMicrosecondPosition());
	}

	public String getName() {
		return filePath.replace("Jazz-Album\\", "");
	}

	public boolean opened() {
		return opened;
	}

}
