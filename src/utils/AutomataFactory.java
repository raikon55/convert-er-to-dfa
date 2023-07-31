package utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import automata.State;
import automata.Transition;

public class AutomataFactory {
    private int states = 0;
    private Deque<State> statesStack = new ArrayDeque<>();
    private Map<String, Map<String, Set<State>>> NFtable = new HashMap<>();

    public State getStates() {
        return this.statesStack.pop();
    }

    public State createTransition(char input) {
        final State from = new State(this.states++, false);
        final State to = new State(this.states++, true);

        final Transition transition = new Transition(from, to, input);

        from.getTransitions().add(0, transition);

        Map<String, Set<State>> mapOftransitions = new HashMap<>();
        mapOftransitions.put(Character.toString(input), Set.of(to));
        this.NFtable.put(from.getInternalName(), mapOftransitions);
        this.NFtable.put(to.getInternalName(), new HashMap<>());

        return from;
    }

    public void create(State firstAutomata, State secondAutomata) {
        Set<State> finals = new HashSet<>();
        finals = this.getFinalState(firstAutomata, finals);

        for (State state : finals) {
            Transition lambdaTransition = new Transition(state, secondAutomata, SpecialSymbols.cLAMBDA);
            state.getTransitions().add(lambdaTransition);
            this.updateTable(state, secondAutomata, SpecialSymbols.LAMBDA);
        }
    }

    public Set<State> getFinalState(State automata, Set<State> finals) {
        if (finals.contains(automata)) {
            return finals;
        }

        if (automata.isAccept()) {
            finals.add(automata);
        }

        this.NFtable.get(automata.getInternalName()).forEach((key, states) -> {
            for (State state : states) {
                this.getFinalState(state, finals);
            }
        });

        return finals;
    }

    public Set<State> walkInAutomata(State root, char symbol) {
        Set<State> states = new HashSet<>();

        for (Transition transition : root.getTransitions()) {
            if (transition.getSymbol() == SpecialSymbols.cLAMBDA) {
                states.addAll(this.walkInAutomata(transition.getTo(), symbol));
            }

            if (transition.getSymbol() == symbol) {
                states.add(transition.getTo());
            }
        }

        return states;
    }

    public State concat(final State from, final State to) {
        final State lastState = from.getTransitions().get(0).getTo();

        Transition lambdaTransition = new Transition(lastState, to, SpecialSymbols.cLAMBDA);
        lastState.getTransitions().add(lambdaTransition);

        final var mapOftransitions = this.NFtable.get(lastState.getInternalName());
        Set<State> transitions = mapOftransitions.getOrDefault(SpecialSymbols.LAMBDA, new HashSet<>());
        transitions.add(to);
        mapOftransitions.put(SpecialSymbols.LAMBDA, transitions);

        return from;
    }

    public State union(final State firstSymbol, final State secondSymbol) {
        final State initial = new State(this.states++, false);
        final State last = new State(this.states++, true);

        final Transition firstSymbolTransition = new Transition(firstSymbol.getTransitions().get(0).getTo(), last,
                SpecialSymbols.cLAMBDA);
        final Transition secondSymbolTransition = new Transition(secondSymbol.getTransitions().get(0).getTo(), last,
                SpecialSymbols.cLAMBDA);
        final Transition lastTrasition = new Transition(initial, firstSymbol, SpecialSymbols.cLAMBDA);
        final Transition middleTransition = new Transition(initial, secondSymbol, SpecialSymbols.cLAMBDA);

        firstSymbol.getTransitions().get(0).getTo().getTransitions().add(firstSymbolTransition);
        secondSymbol.getTransitions().get(0).getTo().getTransitions().add(secondSymbolTransition);
        initial.getTransitions().add(lastTrasition);
        initial.getTransitions().add(middleTransition);

        this.NFtable.put(initial.getInternalName(), new HashMap<>());
        this.NFtable.put(last.getInternalName(), new HashMap<>());

        this.updateTable(firstSymbol.getTransitions().get(0).getTo(), last, SpecialSymbols.LAMBDA);
        this.updateTable(secondSymbol.getTransitions().get(0).getTo(), last, SpecialSymbols.LAMBDA);
        this.updateTable(initial, firstSymbol, SpecialSymbols.LAMBDA);
        this.updateTable(initial, secondSymbol, SpecialSymbols.LAMBDA);

        return initial;
    }

    public State star(final State initialState) {
        final State initial = new State(this.states++, false);
        final State lastState = initialState.getTransitions().get(0).getTo();
        final State last = new State(this.states++, true);

        final Transition lastTrasition = new Transition(lastState, last, SpecialSymbols.cLAMBDA);
        final Transition middleTransition = new Transition(lastState, initialState, SpecialSymbols.cLAMBDA);
        final Transition initialTransition = new Transition(initial, initialState, SpecialSymbols.cLAMBDA);

        lastState.getTransitions().add(lastTrasition);
        lastState.getTransitions().add(middleTransition);
        initial.getTransitions().add(initialTransition);

        this.NFtable.put(initial.getInternalName(), new HashMap<>());
        this.NFtable.put(last.getInternalName(), new HashMap<>());

        this.updateTable(lastState, last, SpecialSymbols.LAMBDA);
        this.updateTable(lastState, initialState, SpecialSymbols.LAMBDA);
        this.updateTable(initial, initialState, SpecialSymbols.LAMBDA);

        return initial;
    }

    public void showAutomata() {
        this.NFtable.forEach((from, transitions) -> {
            System.out.println("State=" + from + ":");
            transitions.forEach((symbol, to) -> {
                System.out.println("\tSymbol=" + symbol + ":");
                to.forEach(t -> System.out.println("\t\t" + t));
            });
        });
    }

    private void updateTable(final State from, final State to, String symbol) {
        final var mapOftransitions = this.NFtable.get(from.getInternalName());
        Set<State> transitions = mapOftransitions.getOrDefault(SpecialSymbols.LAMBDA, new HashSet<>());
        transitions.add(to);
        mapOftransitions.put(symbol, transitions);
    }
}
