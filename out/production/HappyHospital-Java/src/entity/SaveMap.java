package entity;

public class SaveMap {
    public int[] agvPos;
    public SaveAgent agents;

    public SaveMap(int[] agvPos, SaveAgent agents){
        this.agvPos = agvPos;
        this.agents = agents;
    }
}
