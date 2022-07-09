package main;
import entity.Agent;
import entity.Agv;
import entity.AutoAgv;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.x + entity.solidArea.x;
        int entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;

        int entityTopWorldY = entity.y + entity.solidArea.y;
        int entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        if (entity instanceof Agv || entity instanceof AutoAgv) {
            switch (entity.direction) {
                case "up" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    if (gp.tileM.tile[tileNum1].agvCollision || gp.tileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "down" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agvCollision || gp.tileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "left" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agvCollision || gp.tileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "right" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agvCollision || gp.tileM.tile[tileNum2].agvCollision) {
                        entity.collisionOn = true;
                    }
                }
            }
        } else if (entity instanceof Agent) {
            switch (entity.direction) {
                case "up" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    if (gp.tileM.tile[tileNum1].agentCollision || gp.tileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "down" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agentCollision || gp.tileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "left" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agentCollision || gp.tileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
                case "right" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile[tileNum1].agentCollision || gp.tileM.tile[tileNum2].agentCollision) {
                        entity.collisionOn = true;
                    }
                }
            }
        }
    }


    public void checkPlayer(Entity entity) {
        //Get entity's solid area position
        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;
        //Get the object's solid area position
        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;

        if (gp.player.solidArea.intersects(entity.solidArea)) {
            entity.justCollided = true;
            gp.player.justCollided = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

    public void checkMove(AutoAgv entity,int mul) {

        int i;
        boolean flag = false;
        switch (entity.direction) {
            case "up" -> {
                //Get entity's solid area position
                entity.y = entity.y - mul * entity.speed;
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                for (i = 0; i < gp.autoAgvs.size(); i++) {
                    if (gp.autoAgvs.get(i) != null) {
                        if (entity.agvID == gp.autoAgvs.get(i).agvID) continue;
                        if (flag) break;
                        //Get the object's solid area position

                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).x + gp.autoAgvs.get(i).solidArea.x;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).y + gp.autoAgvs.get(i).solidArea.y;

                        if (gp.autoAgvs.get(i).solidArea.intersects(entity.solidArea)) {
                            entity.checkMoveEntity = true;
                            flag = true;
                        }
                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).solidAreaDefaultX;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).solidAreaDefaultY;
                    }
                }
                entity.y = entity.y + mul * entity.speed;
            }
            case "down" -> {
                //Get entity's solid area position
                entity.y = entity.y + mul * entity.speed;
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                for (i = 0; i < gp.autoAgvs.size(); i++) {
                    if (gp.autoAgvs.get(i) != null) {
                        if (entity.agvID == gp.autoAgvs.get(i).agvID) continue;
                        if (flag) break;
                        //Get the object's solid area position

                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).x + gp.autoAgvs.get(i).solidArea.x;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).y + gp.autoAgvs.get(i).solidArea.y;

                        if (gp.autoAgvs.get(i).solidArea.intersects(entity.solidArea)) {
                            entity.checkMoveEntity = true;
                            flag = true;
                        }
                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).solidAreaDefaultX;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).solidAreaDefaultY;
                    }
                }
                entity.y = entity.y - mul * entity.speed;
            }
            case "left" -> {
                //Get entity's solid area position
                entity.x = entity.x - mul * entity.speed;
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                for (i = 0; i < gp.autoAgvs.size(); i++) {
                    if (gp.autoAgvs.get(i) != null) {
                        if (entity.agvID == gp.autoAgvs.get(i).agvID) continue;
                        if (flag) break;
                        //Get the object's solid area position

                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).x + gp.autoAgvs.get(i).solidArea.x;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).y + gp.autoAgvs.get(i).solidArea.y;

                        if (gp.autoAgvs.get(i).solidArea.intersects(entity.solidArea)) {
                            entity.checkMoveEntity = true;
                            flag = true;
                        }
                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).solidAreaDefaultX;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).solidAreaDefaultY;
                    }
                }
                entity.x = entity.x + mul * entity.speed;
            }
            case "right" -> {
                //Get entity's solid area position
                entity.x = entity.x + mul * entity.speed;
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                for (i = 0; i < gp.autoAgvs.size(); i++) {
                    if (gp.autoAgvs.get(i) != null) {
                        if (entity.agvID == gp.autoAgvs.get(i).agvID) continue;
                        if (flag) break;
                        //Get the object's solid area position

                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).x + gp.autoAgvs.get(i).solidArea.x;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).y + gp.autoAgvs.get(i).solidArea.y;

                        if (gp.autoAgvs.get(i).solidArea.intersects(entity.solidArea)) {
                            entity.checkMoveEntity = true;
                            flag = true;
                        }
                        gp.autoAgvs.get(i).solidArea.x = gp.autoAgvs.get(i).solidAreaDefaultX;
                        gp.autoAgvs.get(i).solidArea.y = gp.autoAgvs.get(i).solidAreaDefaultY;
                    }
                }
                entity.x = entity.x - mul * entity.speed;
            }
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
    }
}