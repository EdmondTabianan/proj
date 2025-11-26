package main;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new object.OBJ_Key(gp);
        gp.obj[0].worldX = gp.TileSize * 23;
        gp.obj[0].worldY = gp.TileSize * 7;

        gp.obj[1] = new object.OBJ_Key(gp);
        gp.obj[1].worldX = gp.TileSize * 23;
        gp.obj[1].worldY = gp.TileSize * 40;

        gp.obj[2] = new object.OBJ_Key(gp);
        gp.obj[2].worldX = gp.TileSize * 38;
        gp.obj[2].worldY = gp.TileSize * 8;

        gp.obj[3] = new object.OBJ_Door(gp);
        gp.obj[3].worldX = gp.TileSize * 10;
        gp.obj[3].worldY = gp.TileSize * 11;
        gp.obj[4] = new object.OBJ_Door(gp);
        gp.obj[4].worldX = gp.TileSize * 8;
        gp.obj[4].worldY = gp.TileSize * 28;
        gp.obj[5] = new object.OBJ_Door(gp);
        gp.obj[5].worldX = gp.TileSize * 12;
        gp.obj[5].worldY = gp.TileSize * 22;
        gp.obj[6] = new object.OBJ_Chest(gp);
        gp.obj[6].worldX = gp.TileSize * 10;
        gp.obj[6].worldY = gp.TileSize * 7;
        gp.obj[7] = new object.OBJ_Boots(gp);
        gp.obj[7].worldX = gp.TileSize * 37;
        gp.obj[7].worldY = gp.TileSize * 42;
        // not properly placed
    }
}
