package main;
import entity.Agent;
import entity.Agv;
import entity.AutoAgv;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.x + entity.solidArea.x;
        int entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;

        int entityTopWorldY = entity.y + entity.solidArea.y;
        int entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        if(entity instanceof Agv || entity instanceof AutoAgv) {
            switch (entity.direction) {
                case "up" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agvCollision || gp.TileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "down" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agvCollision || gp.TileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "left" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                    if (gp.TileM.tile[tileNum1].agvCollision || gp.TileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "right" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agvCollision || gp.TileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
            }
        } else if(entity instanceof Agent) {
            switch (entity.direction) {
                case "up" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agentCollision || gp.TileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "down" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agentCollision || gp.TileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "left" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityLeftCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityLeftCol];
                    if (gp.TileM.tile[tileNum1].agentCollision || gp.TileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "right" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    tileNum1 = gp.TileM.mapTileNum[entityTopRow][entityRightCol];
                    tileNum2 = gp.TileM.mapTileNum[entityBottomRow][entityRightCol];
                    if (gp.TileM.tile[tileNum1].agentCollision || gp.TileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
            }
        }
    }

    // AGV va phải agent
//    public void checkEntity(Entity entity, Entity target) {
//
//        if(target != null) {
//            //Get entity's solid area position
//            entity.solidArea.x = entity.x + entity.solidArea.x;
//            entity.solidArea.y = entity.y + entity.solidArea.y;
//            //Get the object's solid area position
//            target.solidArea.x = gp.agent.x;
//            target.solidArea.y = gp.agent.y;
//
//            switch (entity.direction) {
//                case "up" -> {
//                    entity.solidArea.y -= entity.speed;
//                    if (entity.solidArea.intersects(gp.agent.solidArea)) entity.justCollided = true;
//                }
//                case "down" -> {
//                    entity.solidArea.y += entity.speed;
//                    if (entity.solidArea.intersects(gp.agent.solidArea)) entity.justCollided = true;
//                }
//                case "right" -> {
//                    entity.solidArea.x += entity.speed;
//                    if (entity.solidArea.intersects(gp.agent.solidArea)) entity.justCollided = true;
//                }
//                case "left" -> {
//                    entity.solidArea.x -= entity.speed;
//                    if (entity.solidArea.intersects(gp.agent.solidArea)) entity.justCollided = true;
//                }
//            }
//
//            entity.solidArea.x = entity.solidAreaDefaultX;
//            entity.solidArea.y = entity.solidAreaDefaultY;
//            target.solidArea.x = target.solidAreaDefaultX;
//            target.solidArea.y = target.solidAreaDefaultY;
//        }
//    }

    public void checkPlayer(Entity entity) {
        //Get entity's solid area position
        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;
        //Get the object's solid area position
        gp.player.solidArea.x = gp.player.x;
        gp.player.solidArea.y = gp.player.y;

        if(gp.player.solidArea.intersects(entity.solidArea)) {
            entity.justCollided = true;
            gp.player.justCollided = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        return;
    }
}
