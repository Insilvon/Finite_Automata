package GraphStructures;

import DataStructures.State;

public class GraphState extends GraphObject{
    private String name;
    private boolean isDrawn = false;
    private boolean isFinal = false;
    private State state;
    private int radius;

    public GraphState(int x, int y, State state){
        super(x, y);
        this.name = state.getName();
        this.state = state;
        this.isFinal = state.isFinal();
    }
    public String getName(){
        return this.name;
    }
    public State getState(){
        return this.state;
    }



    public void setSize(int radius){
        this.radius = radius;
    }

    public int radius(){
        return this.radius;
    }
}
