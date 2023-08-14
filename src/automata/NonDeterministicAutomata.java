package automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public void setStates(Map<String, Map<String, Set<State>>> NFTable) {
        this.NFtable = NFTable;
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

    public Set<State> walkInAutomata(State root, char symbol, Set<State> states) {

        Set<State> nextStates = NFtable.get(root.getInternalName()).get(Character.toString(symbol));
        if (Objects.nonNull(nextStates)) {
            states.addAll(nextStates);
        }

        Set<State> nextLambdaStates = NFtable.get(root.getInternalName()).get(SpecialSymbols.LAMBDA);
        if (Objects.nonNull(nextLambdaStates)) {
            states.addAll(nextLambdaStates);
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