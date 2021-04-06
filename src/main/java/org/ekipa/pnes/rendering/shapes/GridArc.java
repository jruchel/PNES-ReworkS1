package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import org.ekipa.pnes.models.elements.Arc;

public class GridArc extends GridNetElement {
    public GridArc(Arc netElement, Line shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, new Label(String.valueOf(netElement.getWeight())), onDelete, onCreate);
    }


}
