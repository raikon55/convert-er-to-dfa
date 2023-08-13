package utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    public static TreeNode buildAST(String expression, Map<String, TreeNode> subExpressions) {
        char control = 'A';
        expression = evaluateParenthesis(expression, control, subExpressions);

        String[] tokens = expression.split(" ");

        List<TreeNode> tokenStack = new ArrayList<>();
        Deque<TreeNode> expressionStack = new ArrayDeque<>();

        if (!subExpressions.isEmpty()) {
            expandVariables(subExpressions);
        }

        for (String token : tokens) {
            if (subExpressions.containsKey(token)) {
                tokenStack.add(subExpressions.get(token));
            } else {
                final TreeNode node = new TreeNode(token);
                tokenStack.add(node);
            }
        }

        while (!tokenStack.isEmpty()) {
            TreeNode node = tokenStack.remove(0);

            if (subExpressions.containsKey(node.value)) {
                node = subExpressions.get(node.value);
            }

            if (isOperator(node.value)) {
                if (node.value.equals(SpecialSymbols.STAR)) {
                    node.left = expressionStack.pop();
                    if (subExpressions.containsKey(node.left.value)) {
                        tokenStack.add(subExpressions.get(node.left.value));
                    }
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

    public static State evaluateAST(TreeNode root, AutomataFactory automataFactory) {
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
            final TreeNode node = buildAST(subExpression, subExpressions);
            subExpressions.put(Character.toString(control), node);

            expression = expression.replace("(" + subExpression + ")", Character.toString(control++));
            match = parenthesisPattern.matcher(expression);
        }

        return expression;
    }

    private static void expandVariables(Map<String, TreeNode> expressions) {
        expressions.forEach((key, value) -> {
            TreeNode root = expressions.get(key);

            if (expressions.containsKey(root.value)) {
                root = expressions.get(root.value);
            }

            if (Objects.nonNull(root.left) &&
                    expressions.containsKey(root.left.value)) {
                root.left = expressions.get(root.left.value);
            }

            if (Objects.nonNull(root.right) &&
                    expressions.containsKey(root.right.value)) {
                root.right = expressions.get(root.right.value);
            }

            expressions.put(key, root);
        });
    }

    public static String parseExpression(String expression) {
        StringBuilder builder = new StringBuilder();
        char[] c = expression.toCharArray();
        for (int i = 0; i < expression.length(); i++) {
            String character = Character.toString(c[i]);
            String nextCharacter = (i < (expression.length() - 1)) ? Character.toString(c[i + 1]) : "!";

            if (nextCharacter.equals(SpecialSymbols.LAMBDA)) {
                builder.append(character + " ");
            } else if (character.equals("(") || character.equals(")")) {
                builder.append(character + " ");
            } else if ((((!isOperator(character) && !isOperator(nextCharacter)) ||
                    ((character.equals(SpecialSymbols.STAR) && !isOperator(nextCharacter)))) &&
                    !nextCharacter.equals(")"))) {
                builder.append(character + " | ");
            } else {
                builder.append(character + " ");
            }
        }

        return builder.toString();
    }

}