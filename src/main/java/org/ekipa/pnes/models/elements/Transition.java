package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Transition extends NetElement {
    private double rotationAngle;
    private TransitionState state;


    public Transition(String id, String name, double x, double y, List<Arc> arcs, double rotationAngle) {
        super(id, name, x, y, arcs);
        this.rotationAngle = (rotationAngle % 360);
        this.state = TransitionState.Unready;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = (rotationAngle % 360);
    }


    public Transition(String id, String name, double x, double y, double rotationAngle) {
        super(id, name, x, y);
        this.rotationAngle = (rotationAngle % 360);
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
        if (!this.state.equals(TransitionState.Running)) return false;
        this.state = TransitionState.Unready;
        return true;
    }

    public enum TransitionState {
        Unready,
        Ready,
        Running
    }
}
