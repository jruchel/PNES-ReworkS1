package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Place<V> extends NetElement {
    private int tokenCapacity;
    private V tokens;

    public Place() {

    }

    public Place(String id, String name, double x, double y, int tokenCapacity) {
        this(id, name, x, y, tokenCapacity, null);
    }

    public Place(String id, String name, double x, double y, int tokenCapacity, V tokens) {
        super(id, name, x, y);
        this.tokenCapacity = tokenCapacity;
        this.tokens = tokens;
    }
}
