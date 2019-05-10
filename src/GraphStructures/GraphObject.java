package GraphStructures;

public class GraphObject {
    private int x;
    private int y;
    public int getY(){
        return this.y;
    }
    public int getX(){
        return this.x;
    }
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    public GraphObject(int x, int y){
        this.x = x;
        this.y = y;
    }
}
