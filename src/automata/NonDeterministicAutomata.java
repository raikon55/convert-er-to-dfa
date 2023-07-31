package automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import regexp.RegularExpression;
import utils.AutomataFactory;

public class NonDeterministicAutomata {
    private int totalStates = 0;
    private int stateIndex = 0;

    private Set<Character> alphabet;
    private Set<State> states = new HashSet<>();
    private List<Transition> transitions = new ArrayList<>();

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public void createAutomataFromRegularExpression(RegularExpression regExp) {
        if (!regExp.isValid()) {
            return;
        }

        final AutomataFactory factory = new AutomataFactory();

        this.alphabet = regExp.getAlphabet();
    }

    @Override
    public String toString() {
        return "NonDeterministicAutomata " +
                "[totalStates=" + totalStates + ", " +
                "alphabet=" + alphabet + ", " +
                "states=" + states + ", " +
                "transitions=" + transitions + "]";
    }
}