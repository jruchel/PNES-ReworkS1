package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.ekipa.pnes.models.elements.Transition;

public class GridTransition extends GridNetElement {
    public GridTransition(Transition netElement, Rectangle shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, label, onDelete, onCreate);
    }

    public GridTransition(Transition netElement, Rectangle shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, onDelete, onCreate);
    }
}
