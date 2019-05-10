package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import GraphStructures.GraphState;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import DataStructures.DFA;
import DataStructures.NFA;
import DataStructures.State;
import DataStructures.Transition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author colin
 */
public class GUI extends Application{
    private static ArrayList<State> states;
    private static ArrayList<Transition> transitions;
    private static ArrayList<GraphState> graphStates = new ArrayList<GraphState>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DFA | NFA DIAGRAM");
        Group root = new Group();
        Canvas canvas = new Canvas(1500,1500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        ArrayList<Path> paths = drawShapes(gc, states);
        for(Path item:paths){
            root.getChildren().add(item);
        }
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public ArrayList<Path> drawShapes(GraphicsContext gc, ArrayList<State> states) {
        int i = 0;
        for (State s:states){
            int x, y;
            switch(i){
                case (0):
                case (1):
                case (2):
                case (3):
                case (4):
                    x = 50+(250*i);
                    y = 200;
                    break;
                case (5):
                case (6):
                case (7):
                case (8):
                case (9):
                    x = 50+(250*(i-5));
                    y = 500;
                    break;
                case (10):
                case (11):
                case (12):
                case (13):
                case (14):
                    x = 50+(250*(i-10));
                    y = 750;
                    break;
                case (15):
                case (16):
                case (17):
                case (18):
                case (19):
                    x = 50+(250*(i-15));
                    y = 1000;
                    break;
                case (20):
                case (21):
                case (22):
                case (23):
                case (24):
                    x = 50+(250*(i-20));
                    y = 1250;
                    break;
                default:
                    x = 0;
                    y = 0;
                    break;
            }
            GraphState temp = new GraphState(x+30, y+30, s);
            graphStates.add(temp);
            gc.setFill(Color.BLACK);
            gc.setFill(Color.DARKBLUE);
            gc.setLineWidth(2);
            if(s.isFinal()){
                gc.strokeArc(x-15, y-15,80,80, 0, 360, ArcType.OPEN);
            }
            gc.strokeArc(x, y, 50, 50, 0, 360, ArcType.OPEN);
            gc.strokeText(s.getName(), x+15, y+30);
            //check if it has connections

            for(Transition t : transitions){
                if (t.getTo()==s){
                    gc.strokeLine(x+25, y, x+15, y-10);
                    gc.strokeLine(x+25, y, x+35, y-10);
                    gc.strokeLine(x+25, y, x+25, y-20);
                }
            }
            i++;
        }

        //CLEAN UP THE STUFF
        for (int w = 0; w< transitions.size(); w++){

            Transition t = transitions.get(w);
            String newline = t.getSymbol();

            for (int q = 0; q<transitions.size();q++){

                Transition r = transitions.get(q);

                if (t!=r){
                    if (t.getFrom()==r.getFrom()&&t.getTo()==r.getTo()&&!t.getSymbol().equals(r.getSymbol())){
                        newline+="/"+r.getSymbol();
                        transitions.remove(r);
                        w--;
                        q--;

                    }
                }
            }
            t.setSymbol(newline);
        }


        ArrayList<Path> paths = new ArrayList<Path>();

        for (Transition t : transitions){
            GraphState first = findState(t.getFrom());
            GraphState second = findState(t.getTo());
            int x1 = first.getX();
            int x2 = second.getX();
            int y1 = first.getY();
            int y2 = second.getY();

            if(x1==x2&&y1==y2) {
                gc.strokeArc(x1 - 20, y1 + 20, 30, 30, 90, 300, ArcType.OPEN);
                gc.strokeLine(x1 + 8, y1 + 25, x1 + 23, y2 + 35);
                gc.strokeLine(x1 + 8, y1 + 25, x1 - 2, y2 + 40);
                gc.fillText(t.getSymbol(), x1 - 10, y2 + 60);
                continue;
            }
            paths.add(drawLine(x1,y1,x2,y2,gc,t));
        }
        return paths;
    }
    public static Path drawLine(int x1, int y1, int x2, int y2, GraphicsContext gc, Transition t){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(x1);
        moveTo.setY(y1);
        CubicCurveTo cct = new CubicCurveTo();

        if(x1==x2){
            if(y1==y2){
                System.out.println("ERROR IN CODE");
            } else if (y1<y2){
                cct.setControlX1(x1+100);
                cct.setControlY1(y1);
                cct.setControlX2(x2+100);
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(),x1+100,((y2-y1)/2)+y1, 1);
            } else { //y1>y2
                cct.setControlX1(x1-100);
                cct.setControlY1(y1);
                cct.setControlX2(x2-100);
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(), x1-100, ((y1-y2)/2)+y2,1);
            }
        } else if (x1<x2){
            if(y1==y2){
                cct.setControlX1(x1);
                cct.setControlY1(y1-100-((x2-x1)/10));
                cct.setControlX2(x2);
                cct.setControlY2(y2-100-((x2-x1)/10));
                gc.fillText(t.getSymbol(),((x2-x1)/2)+x1, y1-100-((x2-x1)/10)+20);
            } else if (y1<y2){
                cct.setControlX1(x1+((x2-x1)/2));
                cct.setControlY1(y1);
                cct.setControlX2(x2-((x2-x1)/2));
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(), (x1+(x2-x1)/2)-10, ((y2-y1)/2)+y1);
            } else { //y1>y2
                cct.setControlX1(x1+((x2-x1)/2));
                cct.setControlY1(y1);
                cct.setControlX2(x2-((x2-x1)/2));
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(), x1+((x2-x1)/2)-15, ((y1-y2)/2)+y2-5);
            }
        } else { //x1>x2
            if(y1==y2){
                cct.setControlX1(x1);
                cct.setControlY1(y1+100+((x1-x2)/10)+20);
                cct.setControlX2(x2);
                cct.setControlY2(y1+100+((x1-x2)/10)+20);
                gc.fillText(t.getSymbol(), ((x1-x2)/2+x2), (y1+100+((x1-x2)/10)+20));
            } else if (y1<y2){
                cct.setControlX1(x1-((x1-x2)/2));
                cct.setControlY1(y1);
                cct.setControlX2(x2+((x1-x2)/2));
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(), (x1-(x1-x2)/2), ((y2-y1)/2)+y1+5);
            } else { //y1>y2
                cct.setControlX1(x1-((x1-x2)/2));
                cct.setControlY1(y1);
                cct.setControlX2(x2+((x1-x2)/2));
                cct.setControlY2(y2);
                gc.fillText(t.getSymbol(), (x1-(x1-x2)/2), ((y1-y2)/2)+y2);
            }
        }
        cct.setX(x2-5);
        cct.setY(y2-50);
        path.getElements().add(moveTo);
        path.getElements().add(cct);
        return path;
    }

    public GraphState findState(State temp){
        for (GraphState item : graphStates){
            if (item.getState() == temp){
                return item;
            }
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length!=3) {
            System.out.println("ERROR - please run again and specify arguments.");
            System.exit(0);
        }
        String faFile = args[0];
        String stringFile = args[1];
        String writeFile = args[2];

        File fa = new File(faFile);
        File strings = new File(stringFile);
        File outputFile = new File(writeFile);

        ArrayList<String> values = readStrings(strings);

        boolean dfa = checkFile(fa);
        Construct data = createConstruct(fa);
        if (dfa){
            DFA var = new DFA(data.getTransitions(), data.getStates(), data.getAlphabet(), data.getStartState(), data.getFinalStates());
            try {
                writeToFile(outputFile, var.verifyInput(values));
            } catch (IOException e) {
                e.printStackTrace();
            }
            states = var.getStates();
            transitions = var.getTransitions();
            launch(args);
        }
        else {
            NFA var = new NFA(data.getTransitions(), data.getStates(), data.getAlphabet(), data.getStartState(), data.getFinalStates());
            try {
                writeToFile(outputFile, var.verifyInput(values));
            } catch (IOException e) {
                e.printStackTrace();
            }
            states = var.getStates();
            transitions = var.getTransitions();
            launch(args);
        }
    }
    private static void writeToFile(File file, String data) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.close();
    }
    private static ArrayList<String> readStrings(File file) throws FileNotFoundException {
        Scanner reader =  new Scanner(file);
        ArrayList<String> values = new ArrayList<>();
        while(reader.hasNextLine()){
            String line = reader.nextLine();
            if(line.length()!=0){
                values.add(line);
            }
        }
        return values;
    }
    /**
     * Converts a line like States =, Alphabet =, Final States =
     * to a usable String[] for our nodes later.
     * @param line current line to read
     * @param trans whether or not this is a transition parse (True = yes, False = no)
     * @return String[]
     */
    private static String[] getData(String line, boolean trans){
        StringBuilder temp = new StringBuilder(line);
        int leftIndex = 0;
        int rightIndex = 0;
        if(trans){
            leftIndex = temp.indexOf("(")+1;
            rightIndex = temp.indexOf(")");
        } else {
            leftIndex = temp.indexOf("{")+1;
            rightIndex = temp.indexOf("}");
        }
        String splice = temp.substring(leftIndex, rightIndex);
        //clean up the line, remove everything that isn't the names
        temp = new StringBuilder(splice);
        while(temp.toString().contains(",")){
            temp.deleteCharAt(temp.indexOf(","));
        }
        if (temp.charAt(0)==' ') temp.deleteCharAt(0);
        return temp.toString().split(" ");
    }
    /**
     * Fetches an individual token from the stuff in this line
     * @param line line to read
     * @return String
     */
    private static String getStartData(String line){
        String value = "";
        int i = 0;
        for (i = 0; i<line.length();i++){
            if (line.charAt(i)=='=') break;
        }
        i++;
        while(i<line.length()&&line.charAt(i)!=','){
            if(line.charAt(i)!=' ') value+=line.charAt(i);
            i++;
        }
        return value;
    }
    // Checks if the file is NFA or DFA. True for DFA, false for NFA.
    private static boolean checkFile(File file){
        Scanner in = null;
        try {
            in = new Scanner(file);
            while (in.hasNextLine()){
                String line = in.nextLine();
                if (line.contains("Relation")) return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;

    }
//    private static NFA parseNFA(File file){
//
//    }

    /**
     * Reads the FA file and sifts out the data it needs
     * @param file file to read
     */
    private static Construct createConstruct(File file){
        String[] states = new String[0];
        String[] alphabet = new String[0];
        String startState = "";
        String[] finalStates = new String[0];
        ArrayList<String[]> transitions = new ArrayList<>();
        boolean dfa = true;

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()){
                String line = reader.nextLine();
                while (line.length()==0) line = reader.nextLine();

                //Is this the header line? Are we dealing with a DFA or NFA?
                if (line.contains("M")) {
                    if (line.contains("Relation")) dfa = false;
                }

                // This is not the header line
                else {
                    // Is this the States line?
                    if(line.contains("States")) {
                        if (line.contains("Final")) finalStates = getData(line, false);
                        else states = getData(line, false);
                    }
                    // Is this the Alphabet Line?
                    if (line.contains("Alphabet")) alphabet = getData(line, false);
                    // Is this the Starting State line?
                    if (line.contains("Starting State")) startState = getStartData(line);
                    // Is this the Transition section?
                    if (line.contains("Transition")){
                        if(reader.hasNextLine()) line = reader.nextLine();
                        while(!line.contains("}")&&reader.hasNextLine()){
                            String[] temp = getData(line, true);
                            transitions.add(temp);
                            line = reader.nextLine();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> finalStates2 = new ArrayList<>(Arrays.asList(finalStates));
        ArrayList<String> alphabet2 = new ArrayList<>(Arrays.asList(alphabet));
        return new Construct(transitions,states,alphabet2,startState,finalStates2);
//        if (isDFA) {
//            DFA var = new DFA(transitions, states, alphabet2, startState, finalStates2);
//            return var;
//        }
//        return null;
//        } else {
//            NFA var = new DFA(transitions, states, alphabet2, startState, finalStates2);
//        }
//        FA var = new FA(transitions, states, alphabet2, startState, finalStates2);
//        return var;
    }
    static class Construct {
        private ArrayList<String[]> transitions;
        private String[] states;
        private ArrayList<String> alphabet;
        private String startState;
        private ArrayList<String> finalStates;
        public Construct(ArrayList<String[]> transitions, String[] states, ArrayList<String> alphabet, String startState, ArrayList<String> finalStates){
            this.transitions = transitions;
            this.states = states;
            this.alphabet = alphabet;
            this.startState = startState;
            this.finalStates = finalStates;
        }
        public ArrayList<String[]> getTransitions(){
            return this.transitions;
        }
        public String[] getStates(){
            return this.states;
        }
        public ArrayList<String> getAlphabet(){
            return this.alphabet;
        }
        public String getStartState(){
            return this.startState;
        }
        public ArrayList<String> getFinalStates(){
            return this.finalStates;
        }
    }

}
