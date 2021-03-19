package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Arc {
    private NetElement start;
    private NetElement end;
    private double weight;

    public Arc(NetElement start, NetElement end, double weight) throws Exception {
        validateElements(start, end);
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public void setStart(NetElement start) throws Exception {
        validateElements(start, this.end);
        this.start = start;
    }

    public void setEnd(NetElement end) throws Exception {
        validateElements(this.start, end);
        this.end = end;
    }

    private void validateElements(NetElement start, NetElement end) throws Exception {
        if (start.getClass().equals(end.getClass()))
            throw new Exception(String.format("Start and end of an arc cannot both be %s", start.getClass().getSimpleName()));

    }
}
