package automata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import utils.AutomataFactory;
import utils.JFlapFile;

public class DeterministicAutomata {
    private int totalStates;

    private Set<Character> alphabet = new HashSet<>();;
    private List<State> states = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public  DeterministicAutomata() {
        this.totalStates = 0;
        this.alphabet = new HashSet<>();
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    public  DeterministicAutomata(String file) {
        JFlapFile jflap = new JFlapFile();
        final AutomataFactory factory = new AutomataFactory();
        readAutomaton(file);
    }

        public void readAutomaton(String filename) {

        try {

            if (!filename.endsWith(".jff")) {
                throw new Exception("Não é .jff");
            }
            // String aux = "";
            int id = -1;
            File regExpFile = new File(filename);
            Scanner scanner = new Scanner(regExpFile);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                int startOfExpression = data.indexOf("<") + 1;
                int endOfExpression = data.indexOf(">");
                // if(data.substring(0, endOfExpression).equals("type")){
                    
                // }
                // System.out.println(startOfExpression + " " + endOfExpression);
                switch(data.substring(startOfExpression, endOfExpression).split(" ")[0]){
                    case "type":
                        boolean isFA = data.substring(endOfExpression+1,data.lastIndexOf("<")).equals("fa");
                        if(!isFA){
                            throw new Exception("Automato errado");
                        }
                        break;
                    case "state":
                        id = Integer.parseInt(data.substring(data.indexOf("\"")+1, data.indexOf("\"")+2));
                        this.states.add(new State(id, false));
                        this.totalStates ++;
                        break;
                    case "initial/":
                        int lastIndex = this.states.size() - 1;
                        State lastElement = this.states.get(lastIndex);
                        lastElement.setInitial(true);
                        break;
                    case "final/":
                        lastIndex = this.states.size() - 1;
                        lastElement = this.states.get(lastIndex);
                        lastElement.setAccept(true);
                        break;
                    case "transition":
                        String from = scanner.nextLine();
                        // System.out.println(from + " " + (from.indexOf(">")) + " " + from.indexOf("</from>"));
                        from = from.substring(from.indexOf(">") + 1,from.lastIndexOf("<"));
                        String to = scanner.nextLine();
                        to = to.substring(to.indexOf(">")+1,to.lastIndexOf("<"));
                        String symbol = scanner.nextLine();
                        symbol = symbol.substring(symbol.indexOf(">")+1,symbol.lastIndexOf("<"));
                        char charSymbol = symbol.charAt(0);
                        // System.out.println("TRANSITIONS " + from + " " + to + " " + symbol);
                        Transition t = new Transition(this.states.get(Integer.parseInt(from)), this.states.get(Integer.parseInt(to)), charSymbol);
                        this.transitions.add(t);
                        this.states.get(Integer.parseInt(from)).setTransition(t);
                        this.alphabet.add(charSymbol);
                        break;
                    default:
                        break;
                        // System.out.println(data.substring(startOfExpression,endOfExpression).split(" ")[0]);    
                }
                // aux +=  aux + "\n";
                // System.out.println(aux);
            }

            scanner.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        } catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }


    
    public boolean sampleTest(String sample){
        sample+=" ";
        for (State state : this.states) {
            if(state.getInitial())
                return sampleTest(state, sample,0);
        }
        return false;
    }

    public boolean sampleTest(State state, String sample, int pos){
        boolean resp = false;
        List<Transition> stateTransition = state.getTransitions();
        System.out.println(sample.charAt(pos));
        if(sample.charAt(pos) != ' '){
            for (Transition transition : stateTransition) {
                System.out.println(transition);
                if(transition.getSymbol() == sample.charAt(pos)){
                    System.out.println("------");
                    System.out.println(transition.getTo().getInternalName()); 
                    return sampleTest(transition.getTo(), sample, ++pos);
                }
                          
            }
        }else{
            if(state.isAccept())
                resp = true;
        }
        System.out.println(resp);
        return resp;
    }

    @Override
    public String toString() {
        return "DeterministicAutomata " +
                "[totalStates=" + totalStates + ", " +
                "alphabet=" + alphabet + ", " +
                "states=" + states + ", " +
                "transitions=" + transitions + "]";
    }
}
