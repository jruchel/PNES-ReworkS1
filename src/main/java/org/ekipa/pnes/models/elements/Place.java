package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Place<V> extends NetElement {
    private int tokenCapacity;
    private V token;

    public Place(String id, String name, double x, double y, int tokenCapacity) {
        this(id, name, x, y, tokenCapacity, null);
    }

    public Place(String id, String name, double x, double y, int tokenCapacity, V token) {
        super(id, name, x, y);
        this.tokenCapacity = tokenCapacity;
        this.token = token;
    }
}
