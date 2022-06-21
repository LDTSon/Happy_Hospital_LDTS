//package gameAlgo;
//import entity.Agent;
//import entity.Agv;
//import entity.AutoAgv;
//
//import java.util.*;
//public class Graph {
//    public Node2D[][] nodes;
//    public int width;
//    public int height;
//    public Agent[] agents;
//    public int[][] busy;
//    public Position[] pathPos;
//    private Set<AutoAgv> autoAgvs;
//    private Agv agv;
//
//    public Graph(int width, int height, Position[][][] danhsachke, Position[] pathPos) {
//        this.width = width;
//        this.height = height;
//        this.nodes = new Node2D[width][height];
//        this.pathPos = pathPos;
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                this.nodes[i][j].x=i;
//                this.nodes[i][j].y=j;
//            }
//        }
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                for (int k = 0; k < danhsachke[i][j].length; k++) {
//                    Position nutke = danhsachke[i][j][k];
//                    this.nodes[i][j].setNeighbor(this.nodes[(int) nutke.x][(int) nutke.y]);
//                }
//            }
//        }
//        for (Position p : pathPos) {
//            this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
//        }
//
//        this.busy = new int[52][28];
//        for (int i = 0; i < 52; i++) {
//            for (int j = 0; j < 28; j++) {
//                if (this.nodes[i][j].state == StateOfNode2D.EMPTY) {
//                    this.busy[i][j] = 0;
//                } else {
//                    this.busy[i][j] = 2;
//                }
//            }
//        }
//    }
//
//    public void setAutoAgvs(Set<AutoAgv> autoAgvs) {
//        this.autoAgvs = autoAgvs;
//    }
//
//    public Set<AutoAgv> getAutoAgvs() {
//        return autoAgvs;
//    }
//
//    public void setAgv(Agv agv) {
//        this.agv = agv;
//    }
//
//    public void setAgents(Agent[] agents) {
//        for (Position p : pathPos) {
//            this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
//        }
//        this.busy = new int[52][28];
//        for (int i = 0; i < 52; i++) {
//            for (int j = 0; j < 28; j++) {
//                if (this.nodes[i][j].state == StateOfNode2D.EMPTY) {
//                    this.busy[i][j] = 0;
//                } else {
//                    this.busy[i][j] = 2;
//                }
//            }
//        }
//        this.agents = agents;
//    }
//
//    public void updateState() {
//        int[][] cur = new int[52][28];
//        for (int i = 0; i < 52; i++) {
//            for (int j = 0; j < 28; j++) {
//                cur[i][j] = 0;
//            }
//        }
//        for (Agent agent : this.agents) {
//            if (agent.isActive) {
//                int xl = (int) Math.floor(agent.x / 32);
//                int xr = (int) Math.floor((agent.x + 31) / 32);
//                int yt = (int) Math.floor(agent.y / 32);
//                int yb = (int) Math.floor((agent.y + 31) / 32);
//                cur[xl][yt] = 1;
//                cur[xl][yb] = 1;
//                cur[xr][yt] = 1;
//                cur[xr][yb] = 1;
//            }
//        }
//        for (int i = 0; i < 52; i++) {
//            for (int j = 0; j < 28; j++) {
//                if (this.busy[i][j] == 0)
//                {
//                    if ((cur[i][j] == 0)) continue;
//                    this.nodes[i][j].setState(StateOfNode2D.BUSY);
//                    this.busy[i][j] = 1;
//                }
//                else if(this.busy[i][j] == 1)
//                {
//                    if (cur[i][j] == 1) continue;
//                    this.nodes[i][j].setState(StateOfNode2D.EMPTY);
//                    this.busy[i][j] = 0;
//                }
//                //if(this.busy[i][j] == 2) continue;
//            }
//        }
//    }
//    public void removeAgent(Agent agent)
//    {
//        int i = (int) (agent.x / 32);
//        int j = (int) (agent.y / 32);
//        this.nodes[i][j].setState(StateOfNode2D.EMPTY);
//        this.busy[i][j] = 0;
//    }
//    public Node2D[] calPathAStar(Node2D start, Node2D end)
//    {
//        /*
//         * Khoi tao cac bien trong A*
//         */
//        Node2D[] openSet = {start};
//        Node2D[] closeSet = new Node2D[0];
//        Node2D[] path = new Node2D[0];
//        int[][] astar_f = new int[this.width][this.height];
//        int[][] astar_g = new int[this.width][this.height];
//        int[][] astar_h = new int[this.width][this.height];
//        Node2D[][] previous = new Node2D[this.width][this.height];
//        for (int i = 0; i < this.width; i++) {
//            for (int j = 0; j < this.height; j++) {
//                astar_f[i][j] = 0;
//                astar_g[i][j] = 0;
//                astar_h[i][j] = 0;
//            }
//        }
//        int lengthOfPath = 1;
//        /*
//         * Thuat toan
//         */
//        push(openSet, this.nodes[start.x][start.y]);
//
//        while (openSet.length > 0) {
//            int winner = 0;
//            for (int i = 0; i < openSet.length; i++) {
//                if (astar_f[openSet[i].x][openSet[i].y] < astar_f[openSet[winner].x][openSet[winner].y])
//                { winner = i; }
//            }
//            Node2D current = openSet[winner];
//            if (Objects.equals(openSet[winner], end)) {//???
//                Node2D cur = this.nodes[end.x][end.y];
//                push(path, cur);
//                while (previous[cur.x][cur.y] != null) {
//                    push(path, previous[cur.x][cur.y]);
//                    cur = previous[cur.x][cur.y];
//                }
//                reverse(path);
//                return path;
//            }
//            //openSet.splice(winner, 1);
//            remove_ele(openSet, winner);
//
//            push(closeSet, current);
//            Node2D[] neighbors = {current.nodeN, current.nodeE, current.nodeS, current.nodeW,
//                    current.nodeVN, current.nodeVE, current.nodeVS, current.nodeVW };
//
//            for (Node2D neighbor : neighbors) {
//                if (neighbor != null) {
//                    int timexoay = 0;
//                    if (previous[current.x][current.y] != null &&
//                            neighbor.x != previous[current.x][current.y].x &&
//                            neighbor.y != previous[current.x][current.y].y
//                    ) {
//                        timexoay = 1;
//                    }
//                    float tempG = astar_g[current.x][current.y] + 1 + current.getW() + timexoay;
//
//                    if (!this.isInclude(neighbor, closeSet)) {
//                        if (this.isInclude(neighbor, openSet)) {
//                            if (tempG < astar_g[neighbor.x][neighbor.y]) {
//                                astar_g[neighbor.x][neighbor.y] = (int) tempG;
//                            }
//                        } else {
//                            astar_g[neighbor.x][neighbor.y] = (int) tempG;
//                            push(openSet, neighbor);
//                            lengthOfPath++;
//                        }
//                        astar_h[neighbor.x][neighbor.y] = (int) this.heuristic(neighbor, end);
//                        astar_f[neighbor.x][neighbor.y] =
//                                astar_h[neighbor.x][neighbor.y] + astar_g[neighbor.x][neighbor.y];
//                        previous[neighbor.x][neighbor.y] = current;
//                    } else {
//                        if (tempG < astar_g[neighbor.x][neighbor.y]) {
//                            push(openSet, neighbor);
//                            int index = Arrays.asList(closeSet).indexOf(neighbor);
//                            if (index > -1) {
//                                //closeSet.splice(index, 1);
//                                remove_ele(closeSet, index);
//                            }
//                        }
//                    }
//                }
//            }
//        }//end of while (openSet.length > 0)
//        System.out.println("Path not found!");
//        return null;
//    }
//    public boolean isInclude(Node2D node, Node2D[] nodes){
//        for (Node2D node2D : nodes) {
//            if (Objects.equals(node, node2D)) return true;
//        }
//        return false;
//    }
//
//    public float heuristic(Node2D node1, Node2D node2) {
//        return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
//    }
//
//    public Node2D[] push(Node2D[] arr, Node2D x) //đẩy x vào cuối array arr
//    {
//        // create a new ArrayList
//        ArrayList<Node2D> arrlist = new ArrayList<Node2D>(Arrays.asList(arr));
//
//        // Add the new element
//        arrlist.add(x);
//
//        // Convert the Arraylist to array
//        arr = arrlist.toArray(new Node2D[0]);
//
//        // return the array
//        return arr;
//    }
//    public Node2D[] remove_ele(Node2D[] arr, int index) //xóa phần tử đánh số x khỏi array arr
//    {
//        // create a new ArrayList
//        ArrayList<Node2D> arrlist = new ArrayList<Node2D>(Arrays.asList(arr));
//
//        // Add the new element
//        arrlist.remove(index);
//
//        // Convert the Arraylist to array
//        arr = arrlist.toArray(new Node2D[0]);
//
//        // return the array
//        return arr;
//    }
//
//    public Node2D[] reverse(Node2D[] Arr){
//        Collections.reverse(Arrays.asList(Arr));
//        return Arr;
//    }
//
//}
//
//
//
