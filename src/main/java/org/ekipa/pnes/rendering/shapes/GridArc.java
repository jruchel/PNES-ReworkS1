package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetObject;
import org.ekipa.pnes.models.exceptions.NetIntegrityException;
import org.ekipa.pnes.models.exceptions.ProhibitedConnectionException;

public class GridArc extends GridNetElement {
    public GridArc(Arc netElement, Line shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, new Label(String.valueOf(netElement.getWeight())), onDelete, onCreate);
    }

    public GridArc(GridNetElement start, GridNetElement end, OnGridElementAction onDelete, OnGridElementAction onCreate) throws NetIntegrityException {
        super(
                new Arc("", (NetObject) start.getNetElement(), (NetObject) end.getNetElement(), 1),
                new Line(start.getPosition().getKey(), start.getPosition().getValue(), end.getPosition().getKey(), end.getPosition().getValue()),
                onCreate,
                onDelete
        );
        if (start.getClass().equals(end.getClass()) || start instanceof GridArc || end instanceof GridArc) {
            throw new ProhibitedConnectionException("");
        }
    }

}
