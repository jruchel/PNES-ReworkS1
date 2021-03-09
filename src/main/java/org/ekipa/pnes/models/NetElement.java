package org.ekipa.pnes.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class NetElement {
    private String id;
    private String name;
    private double x, y;
    private List<Arc> arcs;

    public NetElement(String id, String name, double x, double y) {
        this.arcs = new ArrayList<>();
    }

}
