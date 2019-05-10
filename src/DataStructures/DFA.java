package DataStructures;

import java.util.ArrayList;

/**
 * Class which represents a Deterministic Finite Automata, an extension of the Finite Automata Class
 */
public class DFA extends FA {
    /**
     * Field that represents a sink state to use in the DFA.
     */
    private State sinkState = new State(" ф");

    /**
     * Constructor method for a new DFA object. Takes all the same fields as a FA.
     * Will also initialize a sink state.
     * @param transitions - ArrayList<String[]> Raw data taken from the transition section of the file. Contains a triple - from state, to state, and transition
     * @param states - String[] list of all the state names that need to be created
     * @param alphabet - ArrayList<String> list of all the symbols present in the alphabet
     * @param startState - String name of the startingState, used for labeling
     * @param finalStates - ArrayList<String> list of the names of all the final states, used to label the State Objects accordingly
     */
    public DFA(ArrayList<String[]> transitions, String[] states, ArrayList<String> alphabet, String startState, ArrayList<String> finalStates){
        super(transitions, states, alphabet, startState, finalStates);
        // Go ahead and add the self-loops to the sink state
        for(String symbol : alphabet) sinkState.addTransition(new Transition(sinkState, symbol, sinkState));
        getStates().add(sinkState);
        cleanup();
    }

    public String verifyInput(ArrayList<String> values){
        String line = "";
        boolean status;
        String input = "";
        System.out.println("=====[DFA RESULTS]=====");
        for (String value : values) {
            input = value;
            status = verify(input);
            line = getString(line, status, input);
        }
        return line;
    }



    private boolean verify(String input){
        State currentState = this.getStartState();
        for (int i = 0; i<input.length(); i++){
            String symbol = input.charAt(i)+"";
            currentState = currentState.nextState(symbol);
        }
        return currentState.isFinal();
    }
    // adds all the missing transitions to the DFA
    private void cleanup(){
        ArrayList<State> revisedStates = new ArrayList<State>();
        for (State currentState : this.getStates()){
            for (String symbol : this.getAlphabet()){
                if (!currentState.hasTransition(symbol)){
                    Transition temp = new Transition(currentState, symbol, sinkState);
                    currentState.addTransition(temp);
                    this.getTransitions().add(temp);
                }
            }
            revisedStates.add(currentState);
        }
        setStates(revisedStates);
    }
}
