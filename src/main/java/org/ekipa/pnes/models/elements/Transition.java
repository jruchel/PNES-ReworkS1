package org.ekipa.pnes.models.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transition extends NetObject {
    private TransitionState state;

    public Transition(){

    }

    public Transition(String id, String name, double x, double y) {
        super(id, name, x, y);
        this.state = TransitionState.Unready;
    }

    public TransitionState getState() {
        return state;
    }

    private void setState(TransitionState state) {
        this.state = state;
    }

    /**
     * Ustawia stan tranzycji na gotowy jesli został poprawnie ustawiony lub już taki był zwraca true, jeśli
     * nie, false.
     *
     * @return Gdy stan tranzycji jest {@link org.ekipa.pnes.models.elements.Transition.TransitionState#Ready} - true
     * w przeciwnym wypadku - false
     */
    public boolean setReady() {
        if (this.state.equals(TransitionState.Ready)) return true;
        if (!this.state.equals(TransitionState.Unready)) return false;
        this.state = TransitionState.Ready;
        return true;
    }

    /**
     * Ustawia stan tranzycji na odpalony jesli został poprawnie ustawiony lub już taki był zwraca true, jeśli
     * nie, false.
     *
     * @return Gdy stan tranzycji jest {@link org.ekipa.pnes.models.elements.Transition.TransitionState#Running} - true
     * w przeciwnym wypadku - false
     */
    public boolean setRunning() {
        if (this.state.equals(TransitionState.Running)) return true;
        if (!this.state.equals(TransitionState.Ready)) return false;
        this.state = TransitionState.Running;
        return true;
    }

    /**
     * Ustawia stan tranzycji na niegotowy jesli został poprawnie ustawiony lub już taki był zwraca true, jeśli
     * nie, false.
     *
     * @return Gdy stan tranzycji jest {@link org.ekipa.pnes.models.elements.Transition.TransitionState#Unready} - true
     * w przeciwnym wypadku - false
     */
    public boolean setUnready() {
        if (this.state.equals(TransitionState.Unready)) return true;
        this.state = TransitionState.Unready;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return super.getId().equals(that.getId()) &&
                super.getName().equals(that.getName()) &&
                super.getX() == that.getX() &&
                super.getY() == that.getY() &&
                getState().equals(that.getState());
    }

    public enum TransitionState {
        @JsonProperty("Unready")
        Unready,
        @JsonProperty("Ready")
        Ready,
        @JsonProperty("Running")
        Running
    }

}
