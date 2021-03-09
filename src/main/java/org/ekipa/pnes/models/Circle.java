package org.ekipa.pnes.models;

import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.token.Token;
import org.ekipa.pnes.models.token.TokenValue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Circle<V extends TokenValue> extends NetElement {
    private int tokenCapacity;
    private List<Token<V>> tokens;

    public Circle(String id, String name, double x, double y, List<Arc> arcs, int tokenCapacity) {
        super(id, name, x, y, arcs);
        this.tokenCapacity = tokenCapacity;
        this.tokens = new ArrayList<>();
    }

    public Circle(String id, String name, double x, double y, int tokenCapacity) {
        super(id, name, x, y);
        this.tokenCapacity = tokenCapacity;
        this.tokens = new ArrayList<>();
    }

    public Circle(String id, String name, double x, double y, List<Arc> arcs, int tokenCapacity, List<Token<V>> tokens) {
        super(id, name, x, y, arcs);
        this.tokenCapacity = tokenCapacity;
        this.tokens = tokens;
    }

    public Circle(String id, String name, double x, double y, int tokenCapacity, List<Token<V>> tokens) {
        super(id, name, x, y);
        this.tokenCapacity = tokenCapacity;
        this.tokens = tokens;
    }
}
