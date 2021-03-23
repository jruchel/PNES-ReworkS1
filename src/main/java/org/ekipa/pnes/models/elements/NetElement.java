package org.ekipa.pnes.models.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public abstract class NetElement {
    private String id;
    private String name;
    private double x, y;
    private List<Arc> arcs;

    public NetElement(String id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.arcs = new ArrayList<>();
    }

    /**
     * Szukanie wszystkich elementow, gdzie poczatek i koniec sa tymi samymi elementami, sumowanie ich wag i zbieranie w jeden
     */
    private void condenseArcs() {

    }

    @Override
    public String toString() {
        return "NetElement{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", arcs=" + arcs +
                '}';
    }
}
