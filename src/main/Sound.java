package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30]; 

    public Sound() {
        int i = 0;
        soundURL[i] = getClass().getResource("/sound/BlueBoyadventure.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/coin.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/powerup.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/unlock.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/fanfare.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/hitmonster.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/receivedamage.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/0110.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/levelup.wav");
        i++;
        soundURL[i] = getClass().getResource("/sound/cursor.wav");

    } 
    public void setFile(int i) {
        try {
             AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
             clip = AudioSystem.getClip();
             clip.open(ais);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    public void play() {
        clip.start();
    }
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();
    }
}
