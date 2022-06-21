package main;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;
    public  CollisionChecker(GamePanel gp){
        this.gp=gp;
    }
    public void CheckTile(Entity entity){
        int entityLeftWorldX=entity.worldX+entity.solidArea.x;
        int entityRightWorldX=entity.worldX+entity.solidArea.x+entity.solidArea.width;

        int entityTopWorldY= entity.worldY+entity.solidArea.y;
        int entityBottomWorldY=entity.worldY+entity.solidArea.y+entity.solidArea.height;

        int entityLeftCol=entityLeftWorldX/gp.tileSize;
        int entityRightCol=entityRightWorldX/gp.tileSize;
        int entityTopRow=entityTopWorldY/gp.tileSize;
        int entityBottomRow=entityBottomWorldY/gp.tileSize;

        int tileNum1,tileNum2;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                if (gp.TileM.tile[tileNum1].collison || gp.TileM.tile[tileNum2].collison) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.TileM.tile[tileNum1].collison || gp.TileM.tile[tileNum2].collison) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                if (gp.TileM.tile[tileNum1].collison || gp.TileM.tile[tileNum2].collison) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.TileM.tile[tileNum1].collison || gp.TileM.tile[tileNum2].collison) {
                    entity.collisionOn = true;
                }
            }
        }
    }
}
