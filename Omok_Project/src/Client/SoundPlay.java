package Client;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlay {
	Operator o;
	FloatControl mgainControl;
	Clip musicClip;
	private long musicPosition = 0;

	public SoundPlay(Operator _o) {
		this.o = _o;

	}

	public void stoneSound() {
		try {
			File soundFile = new File("C:/Users/도석환/eclipse-workspace/Omok_Project/sound/stonesound.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();

			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * o.fontopt.stonevolume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void musicSound() {
		try {
			File soundFile = new File("C:/Users/도석환/eclipse-workspace/Omok_Project/sound/backgroundmusic.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			musicClip = AudioSystem.getClip();
			musicClip.open(audioIn);

			mgainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
			float range = mgainControl.getMaximum() - mgainControl.getMinimum();
			float gain = (range * o.fontopt.musicvolume) + mgainControl.getMinimum();
			mgainControl.setValue(gain);

			// 반복 재생 설정
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);

			// 음악 재생 시작
			musicClip.setMicrosecondPosition(musicPosition);
			musicClip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setmusicSound() {
		float range = mgainControl.getMaximum() - mgainControl.getMinimum();
		float gain = (range * o.fontopt.musicvolume) + mgainControl.getMinimum();
		mgainControl.setValue(gain);
	}

	// 음악 정지 메소드
	public synchronized void stopMusic() {
		if (musicClip != null && musicClip.isRunning()) {
			musicPosition = musicClip.getMicrosecondPosition();
			musicClip.stop();
			musicClip.close();
		}
	}

	// 음악 재생 메소드
	public synchronized void playMusic() {
		// 음악이 멈춰있을 때만 다시 재생
		if (musicClip != null && !musicClip.isRunning()) {
			musicClip.setMicrosecondPosition(musicPosition);
			musicClip.start();
		}
	}

}