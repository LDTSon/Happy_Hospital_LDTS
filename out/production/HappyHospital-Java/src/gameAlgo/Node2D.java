//package gameAlgo;
//
//import classes.statistic.Constant;
//
//
//enum StateOfNode2D {
//    EMPTY,
//    BUSY,
//    NOT_ALLOW,
//}
//
//public class Node2D {
//
//    final float lambda = 0.4f;
//
//    public int x;
//    public int y;
//    public Node2D nodeW;
//    public Node2D nodeN;
//    public Node2D nodeS;
//    public Node2D nodeE;
//    public int w_edge_W = Integer.MAX_VALUE;
//    public int w_edge_N = Integer.MAX_VALUE;
//    public int w_edge_S = Integer.MAX_VALUE;
//    public int w_edge_E = Integer.MAX_VALUE;
//    private float w=0;
//    private float u=0;
//    public StateOfNode2D state = StateOfNode2D.NOT_ALLOW;
//    public float p_random = (float) 0.05;
//    public float t_min = 2000;
//    public float t_max = 3000;
//    public Node2D nodeVW;
//    public Node2D nodeVN;
//    public Node2D nodeVS;
//    public Node2D nodeVE;
//    public int w_edge_VW = Integer.MAX_VALUE;
//    public int w_edge_VN = Integer.MAX_VALUE;
//    public int w_edge_VS = Integer.MAX_VALUE;
//    public int w_edge_VE = Integer.MAX_VALUE;
//    public boolean isVirtualNode = false;
//    public float _weight=0;
//
//    public Node2D(
//            int x,
//            int y,
//            boolean isVirtualNode,
//            StateOfNode2D state,
//            float p_random,
//            float t_min,
//            float t_max
//    ){
//        this.x = x;
//        this.y = y;
//        this.state = state;
//        this.p_random = p_random;
//        this.t_min = t_min;
//        this.t_max = t_max;
//        this.isVirtualNode = isVirtualNode;
//    }
//    public float getW(){
//        if(Constant.MODE == ModeOfPathPlanning.FRANSEN)
//            return this.w;
//        else
//            return this._weight;
//    }
//    public float get_weight() {
//        return this._weight;
//    }
//    public void set_weight(float value) {
//        this._weight = value;
//    }
//    public void setNeighbor(Node2D node)  {
//        if(node == null)
//            return;
//        if(node.isVirtualNode) {
//            if (this.x + 1 == node.x && this.y == node.y) {
//                this.nodeVE = node;
//                this.w_edge_VE = 1;
//            } else if (this.x == node.x && this.y + 1 == node.y) {
//                this.nodeVS = node;
//                this.w_edge_VS = 1;
//            } else if (this.x - 1 == node.x && this.y == node.y) {
//                this.nodeVW = node;
//                this.w_edge_VW = 1;
//            } else if (this.x == node.x && this.y - 1 == node.y) {
//                this.nodeVN = node;
//                this.w_edge_VN = 1;
//            }
//            return;
//        }
//        this.setRealNeighbor(node);         // hàng hóm là node thật
//        return;
//    }
//    private void setRealNeighbor(Node2D node) {
//        if (this.x + 1 == node.x && this.y == node.y) {
//            this.nodeE = node;
//            this.w_edge_E = 1;
//        } else if (this.x == node.x && this.y + 1 == node.y) {
//            this.nodeS = node;
//            this.w_edge_S = 1;
//        } else if (this.x - 1 == node.x && this.y == node.y) {
//            this.nodeW = node;
//            this.w_edge_W = 1;
//        } else if (this.x == node.x && this.y - 1 == node.y) {
//            this.nodeN = node;
//            this.w_edge_N = 1;
//        }
//    }
//    public void setState( StateOfNode2D state) {
//        this.state = state;                   // trạng thái của node
//    }
//    public boolean equal(Node2D node) {
//        if(node.isVirtualNode != this.isVirtualNode)
//            return false;
//        return this.x == node.x && this.y == node.y;
//    }
//    public boolean madeOf(Node2D node){
//        return this.equal(node);
//    }
//
//    public void setU(float u) {
//        this.u = (float) Math.floor(u);
//        this.updateW();
//    }
//
//    public void updateW() {
//        this.w = (1 - lambda) * this.w + lambda * this.u;
//    }
//
//}