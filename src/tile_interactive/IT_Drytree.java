package tile_interactive;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;

public class IT_Drytree extends InteractiveTile {

    GamePanel gp;

    public IT_Drytree(GamePanel gp,int map, int col, int row) {
        super(gp,map, col, row);
        this.gp = gp;

        this.worldX = gp.TileSize * col;
        this.worldY = gp.TileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.TileSize, gp.TileSize);
        destructible = true;
        life = 1;
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
        InteractiveTile tile = new IT_Trunk(gp, 0, worldX/gp.TileSize, worldY/gp.TileSize);
        return tile;
    }
    public void checkDrop() {

        //cast a die 
        int i = new Random().nextInt(100)+1;
        //set the monster drop
        if (i<50) {
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        if (i >=50 && i < 75) {
            dropItem(new OBJ_Heart(gp));
        }
        if (i >=75 && i < 100) {
            dropItem(new OBJ_ManaCrystal(gp));
        }
        if (i == 100) {
            dropItem(new OBJ_Potion_Red(gp));
        }
    }
}