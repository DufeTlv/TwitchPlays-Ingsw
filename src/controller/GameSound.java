package controller;

import javax.sound.sampled.*;
import java.io.*;


public class GameSound {

    private Clip clipaudio;
    private AudioInputStream audioInputStream;
    private String filepath;
    private Long currentFrame;

    public GameSound(String url) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
        clipaudio = AudioSystem.getClip();
        clipaudio.open(audioInputStream);
        clipaudio.loop(Clip.LOOP_CONTINUOUSLY);

        filepath = url;
        currentFrame = 0L;
    }


    public void startSound() {
        if (!clipaudio.isActive() && clipaudio != null) {
            FloatControl control = (FloatControl) clipaudio.getControl(FloatControl.Type.MASTER_GAIN);
            float range = control.getMinimum();
            float result = range * (1 - 60/ 100.0f);
            control.setValue(result);
        	
            clipaudio.start();
        }
    }

    public void pauseSound(){
        this.currentFrame = this.clipaudio.getMicrosecondPosition();
        clipaudio.stop();
    }

    public void resumeSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clipaudio.close();
        resetAudioStream();
        clipaudio.setMicrosecondPosition(currentFrame);
        this.startSound();
    }

    public long getCurrentAudioPosition(){
        return clipaudio.getMicrosecondPosition();
    }

    public void stopSound(){
        if (clipaudio != null){
            clipaudio.stop();
            clipaudio.close();
        }
    }

    public void restartSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clipaudio.stop();
        clipaudio.close();
        resetAudioStream();
        currentFrame = 0L;
        clipaudio.setMicrosecondPosition(0);
        this.startSound();
    }

    private void resetAudioStream() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
        clipaudio.open(audioInputStream);
        clipaudio.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
}
