package org.ekipa.pnes.models.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public abstract class NetElement {
    private String id;
    private String name;
    private double x, y;
    private List<Arc> arcs;

    public NetElement(String id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.arcs = new ArrayList<>();
    }

    /**
     * Szukanie wszystkich elementow, gdzie poczatek i koniec sa tymi samymi elementami, sumowanie ich wag i zbieranie w jeden
     */
    private void condenseArcs() {

    }

    @Override
    public String toString() {
        return "NetElement{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", arcs=" + arcs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetElement that = (NetElement) o;
        if (Double.compare(that.getX(), getX()) != 0) return false;
        if (Double.compare(that.getY(), getY()) != 0) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getArcs().equals(that.getArcs());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        temp = Double.doubleToLongBits(getX());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getArcs().hashCode();
        return result;
    }
}
