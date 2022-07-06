package gameAlgo;

public class Node {

    public Node parent;
    public int col;
    public int row;
    public int gCost;
    public int hCost;
    public int fCost;
    public boolean solid;
    public boolean open;
    public boolean checked;

    public Node(int col, int row) {
        this.row = row;
        this.col = col;
    }

    public boolean equal(Node node) {
        if(this.col == node.col && this.row == node.row) return true;
        return false;
    }
}
