package tile_interactive;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class IT_Drytree extends InteractiveTile {

    GamePanel gp;

    public IT_Drytree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.TileSize * col;
        this.worldY = gp.TileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.TileSize, gp.TileSize);
        image = down1;

        destructible = true;
        life = 3;
    }
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        
        if (entity.currentweapon.type == type_axe) {
            isCorrectItem = true;
        }

        return isCorrectItem;
    }
    public void playSE() {
        gp.playSE(10);
    }
    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile = new IT_Trunk(gp, worldX/gp.TileSize, worldY/gp.TileSize);
        return tile;
    }
    public Color getParticleColor() {
        Color color = new Color(65, 50, 30);
        return  color;
    }
    public int getParticleSize() {
        // 6 pixels
        int size = 6;
        return size;
    }
    public int getParticleSpeed() {
        speed = 1;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }
}