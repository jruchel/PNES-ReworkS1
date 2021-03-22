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

    /**Sprawdza czy elementy są tego samego typu, jeżeli tak to
     * zostanie wyrzucony wyjątek
     *
     * @param start element początkowy łuk rozpoczyna się w 1 elemencie
     *{@link org.ekipa.pnes.models.elements.Transition} lub {@link org.ekipa.pnes.models.elements.Place}
     * @param end element końcowy {@link org.ekipa.pnes.models.elements.Transition} lub
     * {@link org.ekipa.pnes.models.elements.Place}
     *
     * @throws Exception wypisuje komunikat ze elementy sa tego samego typu
     */
    private void validateElements(NetElement start, NetElement end) throws Exception {
        if (start.getClass().equals(end.getClass()))
            throw new Exception(String.format("Start and end of an arc cannot both be %s", start.getClass().getSimpleName()));

    }
}
