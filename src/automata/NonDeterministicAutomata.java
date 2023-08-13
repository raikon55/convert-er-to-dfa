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
import utils.TreeNode;

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
        this.alphabet = regExp.getAlphabet();

        Map<String, TreeNode> subExpressions = new HashMap<>();
        final AutomataFactory factory = new AutomataFactory();
        String formattedExpression = ExpressionParser.parseExpression(regExp.getExpression());
        TreeNode root = ExpressionParser.buildAST(formattedExpression, subExpressions);
        State result = ExpressionParser.evaluateAST(root, factory);
        // factory.create(result, result);
        factory.showAutomata();
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