package DataStructures;

import java.util.ArrayList;

/**
 * A Finite Automata Data Structure - a base for a DFA or NFA implementation.
 */
public class FA {
    /**
     * An array of State objects representing all the nodes in the machine
     */
    private ArrayList<State> states;
    /**
     * An array of Transition objects representing all the transitions in the machine
     */
    private ArrayList<Transition> transitions;

    /**
     * Method which returns all the transitions in this machine
     * @return ArrayList<Transition>
     */
    public ArrayList<Transition> getTransitions(){
        return this.transitions;
    }

    /**
     * Method which returns all the states in this machine.
     * @return ArrayList<State>
     */
    public ArrayList<State> getStates(){
        return this.states;
    }
    public void setStates(ArrayList<State> states){
        this.states = states;
    }

    /**
     * Field which holds the names of all the final states
     */
    private ArrayList<String> finalStates;
    /**
     * Field which holds all the symbols in the given alphabet
     */
    private ArrayList<String> alphabet;

    /**
     * Method which returns an arraylist of the alphabet given to the machine
     * @return ArrayList<String>
     */
    public ArrayList<String> getAlphabet(){
        return this.alphabet;
    }

    /**
     * Field which indicates which state the program begins at.
     */
    private State startState;

    /**
     * Returns the state object for the starting state of this machine
     * @return State
     */
    public State getStartState(){
        return this.startState;
    }

    /**
     * Constructor method for the FA - designed to work with this project's input files
     * @param transitions - ArrayList<String[]> Raw data taken from the transition section of the file. Contains a triple - from state, to state, and transition
     * @param states - String[] list of all the state names that need to be created
     * @param alphabet - ArrayList<String> list of all the symbols present in the alphabet
     * @param startState - String name of the startingState, used for labeling
     * @param finalStates - ArrayList<String> list of the names of all the final states, used to label the State Objects accordingly
     */
    public FA(ArrayList<String[]> transitions, String[] states, ArrayList<String> alphabet, String startState, ArrayList<String> finalStates) {
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.states = createStates(states, startState);
        createTransitions(transitions);
    }

    /**
     * Method which will print out whether or not a given string has been accepted by the machine.
     * Also will return the console result as a String for filewriting.
     * @param line - the String to add the result to.
     * @param status - Whether or not the String was accepted.
     * @param input - The actual string that was checked by the machine.
     * @return String - result of verification
     */
    public String getString(String line, boolean status, String input) {
        if (status) {
            line += "The string " + input + " has been accepted. \n";
            System.out.println("The string " + input + " has been accepted.");
        } else {
            line += "The string " + input + " has been rejected. \n";
            System.out.println("The string " + input + " has been rejected.");
        }
        return line;
    }

    /**
     * Loops through all the states given in the input file and
     * creates State objects for them. The States lack transitions,
     * but contain the state name and whether or not the state is a
     * final state or not.
     * @param states - taken from constructor - array of state names.
     * @return ArrayList<State> used for foreach loops and easy navigation
     */
    private ArrayList<State> createStates(String[] states, String startState){
        ArrayList<State> newStates = new ArrayList<State>();
        for (String name:states){
            State temp = new State(name);
            if (name.equals(startState)) this.startState=temp;
            if (this.finalStates.contains(name)) temp.setFinal(true);
            newStates.add(temp);
        }
        
        return newStates;
    }

    /**
     * Loops through all of the transition data pulled from the input file and
     * creates new Transition objects based off of them.
     * It will also add those Transition objects to the corresponding From state.
     * @param values - ArrayList of the raw transition data taken from the input file
     */
    private void createTransitions(ArrayList<String[]> values){
        ArrayList<Transition> newTrans = new ArrayList<Transition>();
        for (String[] transData : values){
            String first = transData[0];
            String second = transData[2];
            String symbol = transData[1];
            State firstState = this.getState(first);
            State secondState = this.getState(second);
            Transition temp = new Transition(firstState, symbol, secondState);
            // Now that you made the object, attach it to this state
            this.getState(first).addTransition(temp);
            newTrans.add(temp);
        }
        this.transitions = newTrans;
    }

    /**
     * Returns the state that matches the input name specified
     * @param name - the name of the state you're looking for
     * @return - State object with the given name, otherwise null
     */
    private State getState(String name){
        for(State item:states){
            if (item.getName().equals(name)) return item;
        }
        return null;
    }
}