package utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import automata.State;

public class ExpressionParser {

    private static boolean isOperator(String token) {
        return token.equals(SpecialSymbols.CONCAT) || token.equals(SpecialSymbols.UNION)
                || token.equals(SpecialSymbols.STAR);
    }

    private static TreeNode buildAST(String expression) {
        char control = 'A';
        Map<String, TreeNode> subExpressions = new HashMap<>();
        expression = evaluateParenthesis(expression, control, subExpressions);

        String[] tokens = expression.split(" ");

        List<TreeNode> tokenStack = new ArrayList<>();
        Deque<TreeNode> expressionStack = new ArrayDeque<>();

        for (String token : tokens) {
            final TreeNode node = new TreeNode(token);
            tokenStack.add(node);
        }

        while (!tokenStack.isEmpty()) {
            final TreeNode node = tokenStack.remove(0);

            if (isOperator(node.value)) {
                if (node.value.equals(SpecialSymbols.STAR)) {
                    node.left = expressionStack.pop();
                    expressionStack.push(node);
                } else {
                    final TreeNode lastStacked = expressionStack.pop();
                    node.right = tokenStack.remove(0);

                    if (Objects.nonNull(lastStacked.right) && node.value.equals(SpecialSymbols.CONCAT)) {
                        node.left = lastStacked.right;
                        lastStacked.right = node;
                        expressionStack.push(lastStacked);
                    } else {
                        node.left = lastStacked;
                        expressionStack.push(node);
                    }
                }
            } else {
                expressionStack.push(node);
            }
        }

        return expressionStack.pop();
    }

    private static State evaluateAST(TreeNode root, AutomataFactory automataFactory) {
        State result;

        if (Objects.isNull(root)) {
            return new State(0, false);
        }

        if (root.value.matches("\\w+") || root.value.equals(SpecialSymbols.LAMBDA)) {
            return automataFactory.createTransition(root.value.toCharArray()[0]);
        }

        State leftValue = evaluateAST(root.left, automataFactory);
        State rightValue = evaluateAST(root.right, automataFactory);

        switch (root.value) {
            case "+":
                result = automataFactory.union(leftValue, rightValue);
                break;
            case "|":
                result = automataFactory.concat(leftValue, rightValue);
                break;
            case "*":
                result = automataFactory.star(leftValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + root.value);
        }

        return result;
    }

    private static String evaluateParenthesis(String expression, char control, Map<String, TreeNode> subExpressions) {
        Pattern parenthesisPattern = Pattern
                .compile("\\(\\s\\w+((\\s[\\+\\|\\*-](\\s\\w+)?)?)+\\s\\)");
        Matcher match = parenthesisPattern.matcher(expression);

        while (match.find()) {
            String subExpression = match.group().trim();
            subExpression = subExpression.substring(1, subExpression.length() - 1);
            final TreeNode node = buildAST(subExpression);
            subExpressions.put(Character.toString(control), node);

            expression = expression.replace("(" + subExpression + ")", Character.toString(control++));
            match = parenthesisPattern.matcher(expression);
        }

        return expression;
    }

    public static void main(String[] args) {
        // String expression = "a | ( b + ( c + ( ( ( ( ( ( a ) ) ) ) ) + ( a + b ) ) +
        // b ) * | b ) * | b * | b + !";
        String expression = "a | b + c + a + a + b * | b * | b * | b + !";
        // String expression = "a + b";
        AutomataFactory automataFactory = new AutomataFactory();
        TreeNode root = buildAST(expression);
        System.out.println(root);
        State result = evaluateAST(root, automataFactory);
        // automataFactory.create(result, result);
        automataFactory.showAutomata();
    }
}