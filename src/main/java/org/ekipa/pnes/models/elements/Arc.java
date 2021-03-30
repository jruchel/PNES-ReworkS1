package org.ekipa.pnes.models.elements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Arc {
    private String id;
    private NetElement start;
    private NetElement end;
    private double weight;

    public Arc() {

    }

    public Arc(String id, NetElement start, NetElement end, double weight) throws Exception {
        validateElements(start, end);
        this.id = id;
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.start.addArc(this);
        this.end.addArc(this);
    }


    public Arc(NetElement start, NetElement end, double weight) throws Exception {
        this("", start, end, weight);
    }

    public void setStart(NetElement start) throws Exception {
        validateElements(start, this.end);
        this.start = start;
    }

    public void setEnd(NetElement end) throws Exception {
        validateElements(this.start, end);
        this.end = end;
    }

    /**
     * Sprawdza czy elementy są tego samego typu, jeżeli tak to
     * zostanie wyrzucony wyjątek
     *
     * @param start element początkowy łuk rozpoczyna się w 1 elemencie
     *              {@link org.ekipa.pnes.models.elements.Transition} lub {@link org.ekipa.pnes.models.elements.Place}
     * @param end   element końcowy {@link org.ekipa.pnes.models.elements.Transition} lub
     *              {@link org.ekipa.pnes.models.elements.Place}
     * @throws Exception wypisuje komunikat ze elementy sa tego samego typu
     */
    private void validateElements(NetElement start, NetElement end) throws Exception {
        if (start.getClass().equals(end.getClass()))
            throw new Exception(String.format("Start and end of an arc cannot both be %s", start.getClass().getSimpleName()));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc = (Arc) o;
        if (Double.compare(arc.getWeight(), getWeight()) != 0) return false;
        if (getId() != null ? !getId().equals(arc.getId()) : arc.getId() != null) return false;
        if (getStart() != null ? !getStart().equals(arc.getStart()) : arc.getStart() != null) return false;
        return getEnd() != null ? getEnd().equals(arc.getEnd()) : arc.getEnd() == null;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "id='" + id + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", weight=" + weight +
                '}';
    }
}
