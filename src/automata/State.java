package automata;

import java.util.ArrayList;
import java.util.List;

public class State {
    private int id;
    private String name = null;
    private boolean accept = false;
    private boolean initial = false;
    private boolean selected = false;
    private String internalName = null;
    private List<Transition> transitions = new ArrayList<>();

    public State(int id, boolean isFinal) {
        this.id = id;
        this.internalName = "q" + id;
        this.accept = isFinal;
    }

    public int getId() {
        return this.id;
    }

    public boolean getInitial(){
        return this.initial;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public boolean isAccept() {
        return accept;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setInitial(boolean initial){
        this.initial = initial;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<Transition> getTransitions() {
        return this.transitions;
    }

    public void setTransition(Transition t){
        this.transitions.add(t);
    }

    public void setTransitions(List<Transition> transactions) {
        this.transitions = transactions;
    }

    @Override
    public String toString() {
        return "[ name=" + this.internalName + ", trasitions=" + this.transitions + ", initial=" + this.initial + ", final=" + this.accept + " ]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.internalName == null) ? 0 : this.internalName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (this.id != other.id)
            return false;
        if (this.internalName == null) {
            if (other.internalName != null)
                return false;
        } else if (!this.internalName.equals(other.internalName))
            return false;
        return true;
    }

}
