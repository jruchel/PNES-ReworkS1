package org.ekipa.pnes.rendering.shapes;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.NetObject;
import org.ekipa.pnes.rendering.exceptions.NoCenterException;


@Setter
@Getter
public abstract class GridNetElement {
    private NetElement netElement;
    private Shape shape;
    private Label label;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onDelete;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onCreate;

    public GridNetElement(NetElement netElement, Shape shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this.netElement = netElement;
        this.shape = shape;
        this.label = label;
        if (label != null) this.label.setLabelFor(shape);
        this.onCreate = onCreate;
        this.onDelete = onDelete;
        this.onCreate.run(this);
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(2);
    }

    public GridNetElement(NetElement netElement, Shape shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this(netElement, shape, null, onDelete, onCreate);
    }

    public Pair<Double, Double> getPosition() throws NoCenterException {
        try {
            return new Pair<>(((NetObject) netElement).getX(), ((NetObject) netElement).getY());
        } catch (ClassCastException exception) {
            throw new NoCenterException(String.format("Obiekt typu %s nie posiada środka", netElement.getClass().getSimpleName()));
        } catch (Exception exception) {
            throw new NoCenterException(exception.getMessage());
        }
    }

    public void setMouseClicked(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseClicked(event);
    }

    public void setMouseEntered(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseEntered(event);
    }

    public void setMouseExited(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseExited(event);
    }

    public String getId() {
        return netElement.getId();
    }

    public String getName() {
        return netElement.getName();
    }

    public void setName(String name) {
        netElement.setName(name);
    }

    public void delete() {
        onDelete.run(this);
    }
}
