package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Particle extends Entity {

    Entity generator;
    Color color; 
    int size;
    int xd;
    int yd;

    public Particle(main.GamePanel gp, Entity generator, Color color,int size, int maxLife, int speed, int xd, int yd) {
        
        super(gp);
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;

        life = maxLife;
        alive = true;
        int offset = gp.TileSize/2;
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;
        worldX = generator.worldX;
        worldY = generator.worldY;

    }

    public void update() {

        life--;

        if (life  < maxLife/2) {
            yd++; // gravity effect
        }
        worldX += speed * xd;
        worldY += speed * yd;

        if (life <= 0) {
            alive = false;
        }
    }
    public void draw(Graphics2D g2) {
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size);
    }
}
