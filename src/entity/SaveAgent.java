package entity;

public class SaveAgent {
    public int[][] startPos;
    public int[][] endPos;
    public int[] id;

    public SaveAgent (int[][] startPos, int[][] endPos, int[] id) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.id = id;
    }
}
