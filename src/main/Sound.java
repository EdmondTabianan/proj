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
    private boolean volumeControlAvailable = true;

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
        soundURL[i] = getClass().getResource("/sound/blocked.wav");i++; //15
        soundURL[i] = getClass().getResource("/sound/FinalBattle.wav");i++; //16
    } 
    
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            // Try to get volume control - but don't crash if not available
            try {
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControlAvailable = true;
                checkVolume();
            } catch (IllegalArgumentException e) {
                // MASTER_GAIN not available - try VOLUME control instead
                try {
                    fc = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
                    volumeControlAvailable = true;
                    checkVolume();
                } catch (IllegalArgumentException e2) {
                    // No volume control available on this system
                    volumeControlAvailable = false;
                    System.out.println("Volume control not available on this system");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }
    
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
    
    public void checkVolume() {
        // Only try to set volume if control is available
        if (fc != null && volumeControlAvailable) {
            try {
                switch (volumeScale) {
                    case 0: volume = -80f; break;
                    case 1: volume = -20f; break;
                    case 2: volume = -12f; break;
                    case 3: volume = -5f; break;
                    case 4: volume = 1f; break;
                    case 5: volume = 6f; break;
                    default: volume = -5f; break;
                }
                fc.setValue(volume);
            } catch (IllegalArgumentException e) {
                // Ignore - volume control not supported
                volumeControlAvailable = false;
            }
        }
    }
    
    // Optional: Add method to adjust volume safely
    public void setVolumeScale(int scale) {
        if (scale >= 0 && scale <= 5) {
            volumeScale = scale;
            checkVolume();
        }
    }
}