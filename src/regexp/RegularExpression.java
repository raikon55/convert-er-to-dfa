package regexp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.SpecialSymbols;

public class RegularExpression {

    private String expression;
    private char[] bytesExpression;
    private Set<Character> alphabet = new HashSet<>();
    private static List<Character> exclusiveTokens = List.of('(', ')',
            SpecialSymbols.cLAMBDA,
            SpecialSymbols.cUNION,
            SpecialSymbols.cCONCAT,
            SpecialSymbols.cSTAR);

    public RegularExpression(String expression) {
        this.expression = expression;
        this.bytesExpression = this.simplifyExpression().toCharArray();
    }

    public Set<Character> getAlphabet() {
        return this.alphabet;
    }

    public String getExpression() {
        return this.expression;
    }

    public char[] getBytesExpression() {
        return this.bytesExpression;
    }

    public boolean isValid() {
        if ((this.expression.length() == 0) || (!this.isParenthesisBalanced()) || (this.expression.endsWith("+"))) {
            return false;
        }

        char firstCharacter = this.bytesExpression[0];
        if ((firstCharacter == SpecialSymbols.cUNION) || (firstCharacter == SpecialSymbols.cSTAR)) {
            return false;
        }

        for (int i = 0; i < this.expression.length(); i++) {
            char actualChar = this.bytesExpression[i];
            char previousChar = i > 0 ? this.bytesExpression[i - 1] : actualChar;

            switch (actualChar) {
                case ')':
                case '*':
                    if (previousChar == '(' || previousChar == SpecialSymbols.cUNION) {
                        return false;
                    }
                    break;
                case '!':
                    if (previousChar != '(' && previousChar != SpecialSymbols.cUNION
                            && previousChar != SpecialSymbols.cLAMBDA) {
                        return false;
                    }

                    if (i == (expression.length() - 1)) {
                        break;
                    }

                    char nextChar = this.bytesExpression[i + 1];
                    if (nextChar != ')' && nextChar != SpecialSymbols.cUNION && nextChar != SpecialSymbols.cSTAR) {
                        return false;
                    }
            }

            if (!exclusiveTokens.contains(actualChar)) {
                this.updateAlphabet(actualChar);
            }
        }
        return true;
    }

    public String simplifyExpression() {
        StringBuilder builder = new StringBuilder();
        char[] bytesExpression = this.expression.toCharArray();

        for (int i = 0; i < bytesExpression.length; i++) {
            char currentSymbol = bytesExpression[i];
            boolean isValidConcat = (i < (bytesExpression.length - 1) && !exclusiveTokens.contains(currentSymbol)
                    && !exclusiveTokens.contains(bytesExpression[i + 1]));

            builder.append(currentSymbol);

            if (isValidConcat) {
                builder.append(SpecialSymbols.CONCAT);
            }
        }

        return builder.toString();
    }

    private void updateAlphabet(char symbol) {
        if (!this.alphabet.contains(symbol)) {
            this.alphabet.add(symbol);
        }
    }

    private boolean isParenthesisBalanced() {
        int count = 0;
        for (char symbol : this.bytesExpression) {
            if (symbol == '(') {
                count++;
            } else if (symbol == ')') {
                count--;
            }
        }
        return !((count < 0) || (count != 0));
    }
}
