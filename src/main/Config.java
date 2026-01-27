package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // full scren if have
            // if gp.fullScreenOn == true) {
            // bw.write("On");
            // }
            // if gp.fullScreenOn == false) {
            // bw.write("off");
            // }
            bw.newLine();

            // music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SE vol
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();

            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("config.txt"));
            String s = br.readLine();

            // if have full screen

            // Music volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            // SE volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

}
