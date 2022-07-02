package gameAlgo.algorithm;

import entity.Agent;
import entity.Agv;
import entity.AutoAgv;
import entity.Entity;
import gameAlgo.Node;
import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNode();
    }

    public void instantiateNode() {

        node = new Node[gp.maxScreenCol][gp.maxScreenRow];

        int row = 0;
        int col = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            node[col][row] = new Node(col, row);

            row++;
            if (row == gp.maxScreenRow) {
                row = 0;
                col++;
            }
        }
    }

    public void resetNodes() {
        int row = 0;
        int col = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            //Reset open, checked and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            row++;
            if (row == gp.maxScreenRow) {
                row = 0;
                col++;
            }
        }

        //Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        resetNodes();

        //Set Start and Goal node
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int row = 0;
        int col = 0;
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            //SET SOLID NODE
            //CHECK TILES
            int tileNum = gp.tileM.mapTileNum[col][row];
            if (entity instanceof Agent) {
                if (gp.tileM.tile[tileNum].agentCollision) node[col][row].solid = true;
            } else if (entity instanceof Agv || entity instanceof AutoAgv) {
                if (gp.tileM.tile[tileNum].agvCollision) node[col][row].solid = true;
            }

            //SET COST
            getCost(node[col][row]);

            row++;
            if(row == gp.maxScreenRow) {
                row = 0;
                col++;
            }
        }
    }

    private void getCost(Node node) {

        //G cost
        int xDis = Math.abs(node.row - startNode.row);
        int yDis = Math.abs(node.col - startNode.col);
        node.gCost = xDis + yDis;
        //H cost
        xDis = Math.abs(node.row - goalNode.row);
        yDis = Math.abs(node.col - goalNode.col);
        node.hCost = xDis + yDis;
        //F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search(Entity entity) {

        while (!goalReached && step < 500) {
            int row = currentNode.row;
            int col = currentNode.col;

            //Check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // CHECK AUTOAGV

            int tileNum = gp.tileM.mapTileNum[col][row];
            String d = gp.tileM.tile[tileNum].tileDirection;

            //Open the up node
            if(row - 1 >= 0) {
                //System.out.println("open up");
                if(entity instanceof AutoAgv){
                    if(d.equals("undirected")) {
                        int tmpValue=gp.tileM.mapTileNum[col][row-1];
                        String tmpDirection = gp.tileM.tile[tmpValue].tileDirection;
                        if(tmpDirection.equals("up"))
                        {
                            openNode(node[col][row-1]);
                            System.out.println("open up!!!");
                        }
                    }
                    else if(d.equals("up")) {
                        System.out.println("open up");
                        openNode(node[col][row-1]);
                    }
                }
                else
                   openNode(node[col][row-1]);
            }
            //Open the left node
            if(col - 1 >= 0) {
                //System.out.println("open left");
                if(entity instanceof AutoAgv){
                    if(d.equals("undirected")) {
                        int tmpValue=gp.tileM.mapTileNum[col-1][row];
                        String tmpDirection = gp.tileM.tile[tmpValue].tileDirection;
                        if(tmpDirection.equals("left"))
                        {
                            System.out.println("open left!!!");
                            openNode(node[col-1][row]);
                        }
                    }
                    else if(d.equals("left")) {
                        System.out.println("open left");
                        openNode(node[col-1][row]);
                    }
                }
                else
                  openNode(node[col-1][row]);
            }
            //Open the down node
            if(row+1 < gp.maxScreenRow) {
                //System.out.println("open down");
                if(entity instanceof AutoAgv){
                    if(d.equals("undirected")) {
                        int tmpValue=gp.tileM.mapTileNum[col][row+1];
                        String tmpDirection = gp.tileM.tile[tmpValue].tileDirection;

                        if(tmpDirection.equals("down"))
                        {
                            System.out.println("open down!!!");
                            openNode(node[col][row+1]);
                        }
                    }
                    else if(d.equals("down")) {
                        System.out.println("open down");
                        openNode(node[col][row+1]);
                    }
                }
                else
                  openNode(node[col][row+1]);
            }
            //Open the right node
            if(col+1 < gp.maxScreenCol) {
                //System.out.println("open right");
                if(entity instanceof AutoAgv){
                    if(d.equals("undirected")) {
                        int tmpValue=gp.tileM.mapTileNum[col+1][row];
                        String tmpDirection = gp.tileM.tile[tmpValue].tileDirection;

                        if(tmpDirection.equals("right"))
                        {
                            System.out.println("open right!!!");
                            openNode(node[col+1][row]);
                        }
                    }
                    else if(d.equals("right")) {
                        System.out.println("open right");
                        openNode(node[col+1][row]);
                    }
                }
                else
                  openNode(node[col+1][row]);
            }

            //Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int  i = 0; i < openList.size(); i++) {
                //System.out.println(openList.size());
                //System.out.println(openList.get(i).fCost);
                //Check if this node's F cost is better
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //If F cost is equal, check the G cost
                if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            //If there is node in the openList, end the loop
            if(openList.isEmpty()) break;

            //After the loop, openList[bestNodeIndex] is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    private void trackThePath() {

        Node current = goalNode;

        while (current != startNode) {

            pathList.add(0, current);
            current = current.parent;
        }
    }

    private int heuristic1 (Node node1, Node node2){
        return Math.abs(node1.row - node2.row) + Math.abs(node1.col - node2.col);
    }

    private double heuristic2 (Node node1, Node node2){
        return Math.sqrt((node1.row - node2.row) * (node1.row - node2.row) + (node1.col - node2.col) * (node1.col - node2.col));
    }


    private void openNode(Node node) {

        if(!node.open && !node.checked && !node.solid) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
}