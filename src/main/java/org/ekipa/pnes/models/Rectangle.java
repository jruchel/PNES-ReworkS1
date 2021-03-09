package org.ekipa.pnes.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rectangle extends NetElement {
    private double rotationAngle;
    private NetElement start;
    private NetElement end;

    public Rectangle(String id, String name, double x, double y, List<Arc> arcs, double rotationAngle, NetElement start, NetElement end) {
        super(id, name, x, y, arcs);
        this.rotationAngle = (rotationAngle % 360);
        this.start = start;
        this.end = end;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = (rotationAngle % 360);
    }


    public Rectangle(String id, String name, double x, double y, double rotationAngle, NetElement start, NetElement end) {
        super(id, name, x, y);
        this.rotationAngle = rotationAngle;
        this.start = start;
        this.end = end;
    }

}
