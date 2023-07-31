package automata;

public class Transition {
    private State from;
    private State to;
    private char symbol;
    private boolean isSelected = false;

    public Transition(State from, State to, char symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public State getFrom() {
        return from;
    }

    public void setFrom(State from) {
        this.from = from;
    }

    public State getTo() {
        return this.to;
    }

    public void setTo(State to) {
        this.to = to;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Transition [ from=" + this.from.getInternalName() + ", to=" + this.to.getInternalName() + ", symbol="
                + this.symbol + "]";
    }
}
