package automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexp.RegularExpression;
import utils.AutomataFactory;
import utils.ExpressionParser;
import utils.SpecialSymbols;
import utils.TreeNode;

public class NonDeterministicAutomata {
    private int totalStates = 0;

    private Set<Character> alphabet;
    private State initial = new State(0, false);
    private List<Transition> transitions = new ArrayList<>();
    private Map<String, Map<String, Set<State>>> NFtable = new HashMap<>();

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public State getAutomata() {
        return this.initial;
    }

    public Map<String, Map<String, Set<State>>> getStates() {
        return this.NFtable;
    }

    public void createAutomataFromRegularExpression(RegularExpression regExp) {
        if (!regExp.isValid()) {
            return;
        }
        this.alphabet = regExp.getAlphabet();

        Map<String, TreeNode> subExpressions = new HashMap<>();
        final AutomataFactory factory = new AutomataFactory();
        String formattedExpression = ExpressionParser.parseExpression(regExp.getExpression());
        TreeNode root = ExpressionParser.buildAST(formattedExpression, subExpressions);
        this.initial = ExpressionParser.evaluateAST(root, factory);
        this.NFtable = factory.getStates();

        factory.lambdaClosure(this);
    }

    public Set<State> walkInAutomata(State root, char symbol) {
        Set<State> states = new HashSet<>();

        for (Transition transition : root.getTransitions()) {
            if (transition.isSelected()) {
                return states;
            }
            transition.setSelected(true);
            if (transition.getSymbol() == SpecialSymbols.cLAMBDA) {
                states.addAll(this.walkInAutomata(transition.getTo(), SpecialSymbols.cLAMBDA));
            }

            if (transition.getSymbol() == symbol) {
                states.add(transition.getTo());
            }
        }

        return states;
    }

    @Override
    public String toString() {
        return "NonDeterministicAutomata " +
                "[totalStates=" + this.totalStates + ", " +
                "alphabet=" + this.alphabet + ", " +
                "states=" + this.initial + ", " +
                "transitions=" + this.transitions + "]";
    }
}