package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IdGenerator {

    private static List<NetElement> elementsList;
    private static List<Arc> arcList;

    static {
        reset();
    }


    /**
     * Czyści listę elementów
     */
    public static void resetElements() {
        elementsList = new ArrayList<>();

    }

    /**
     * Czysci liste łuków
     */
    public static void resetArcs() {
        arcList = new ArrayList<>();
    }

    public static void reset() {
        resetArcs();
        resetElements();
    }

    /**
     * Sprawdza czy lista posiada podany element, jeśli tak zwraca ten element, jeśli nie ustawia podanemu elementowi id,
     * następnie dodaje ten element do listy i zwraca go
     *
     * @param element {@link org.ekipa.pnes.models.elements.NetElement}, może nim Place lub Transition
     * @return podany element z ustawionym poprawnym id
     */
    public static NetElement setElementId(NetElement element) {
        if (elementsList.contains(element)) return element;
        element.setId(String.format("%s%d", element.getClass().getSimpleName().charAt(0), findOccurrencesOfSameTypeOfNetElement(element)));
        elementsList.add(element);
        return element;
    }

    /**
     * Zlicza ilość wystąpień typu podanego elementu następnie ją zwraca
     *
     * @param element {@link org.ekipa.pnes.models.elements.NetElement}, może nim Place lub Transition
     * @return liczbę wystąpień typu podanego elementu
     */
    private static long findOccurrencesOfSameTypeOfNetElement(NetElement element) {
        return elementsList.stream().filter(e -> e.getClass().equals(element.getClass())).count();
    }

    public static Arc setArcId(Arc arc) {
        if (arcList.contains(arc)) return arc;
        arc.setId(String.format("%s%d", arc.getClass().getSimpleName().charAt(0), arcList.size()));
        arcList.add(arc);
        return arc;

    }
}
