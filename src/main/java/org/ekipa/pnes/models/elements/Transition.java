package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Transition extends NetElement {
    private double rotationAngle;
    private NetElement start;
    private NetElement end;

    public Transition(String id, String name, double x, double y, List<Arc> arcs, double rotationAngle, NetElement start, NetElement end) {
        super(id, name, x, y, arcs);
        this.rotationAngle = (rotationAngle % 360);
        this.start = start;
        this.end = end;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = (rotationAngle % 360);
    }


    public Transition(String id, String name, double x, double y, double rotationAngle, NetElement start, NetElement end) {
        super(id, name, x, y);
        this.rotationAngle = (rotationAngle % 360);
        this.start = start;
        this.end = end;
    }

}
