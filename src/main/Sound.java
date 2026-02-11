package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30]; 
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
        int i = 0;
        soundURL[i] = getClass().getResource("/sound/hunt.wav");i++; //0
        soundURL[i] = getClass().getResource("/sound/coin.wav");i++; //1
        soundURL[i] = getClass().getResource("/sound/powerup.wav");i++; // 2
        soundURL[i] = getClass().getResource("/sound/unlock.wav");i++; //3
        soundURL[i] = getClass().getResource("/sound/fanfare.wav");i++; //4
        soundURL[i] = getClass().getResource("/sound/hitmonster.wav");i++; // 5
        soundURL[i] = getClass().getResource("/sound/receivedamage.wav");i++; //6
        soundURL[i] = getClass().getResource("/sound/0110.wav");i++; //7
        soundURL[i] = getClass().getResource("/sound/levelup.wav");i++; //8
        soundURL[i] = getClass().getResource("/sound/cursor.wav");i++; // 9
        soundURL[i] = getClass().getResource("/sound/cuttree.wav");i++; //10
        soundURL[i] = getClass().getResource("/sound/loading.wav");i++;//11
        soundURL[i] = getClass().getResource("/sound/gameover.wav");i++; //12
        soundURL[i] = getClass().getResource("/sound/stairs.wav");i++; //13
        soundURL[i] = getClass().getResource("/sound/parry.wav");i++; //14
    } 
    public void setFile(int i) {
        try {
             AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
             clip = AudioSystem.getClip();
             clip.open(ais);
             fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
             checkVolume();
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
    public void checkVolume() {
        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
