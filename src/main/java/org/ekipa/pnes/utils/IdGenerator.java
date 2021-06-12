package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.NetObject;

import java.util.ArrayList;
import java.util.List;

public class IdGenerator {

    private static List<NetElement> elementsList;
    private static List<String> idList;

    static {
        reset();
    }


    /**
     * Czyści listę elementów
     */
    public static void resetElements() {
        elementsList = new ArrayList<>();
        idList = new ArrayList<>();

    }


    public static void reset() {
        resetElements();
    }

    /**
     * Sprawdza czy lista posiada podany element, jeśli tak zwraca ten element, jeśli nie ustawia podanemu elementowi id,
     * następnie dodaje ten element do listy i zwraca go
     *
     * @param element {@link NetObject}, może nim Place lub Transition
     * @return podany element z ustawionym poprawnym id
     */
    public static NetElement setElementId(NetElement element) {
        if (!idList.contains(element.getId())) {
            element.setId(String.format("%s%d", element.getClass().getSimpleName().charAt(0), findOccurrencesOfSameTypeOfNetElement(element)));
            idList.add(element.getId());
            elementsList.add(element);
        }
        return element;
    }

    /**
     * Zlicza ilość wystąpień typu podanego elementu następnie ją zwraca
     *
     * @param element {@link NetObject}, może nim Place lub Transition
     * @return liczbę wystąpień typu podanego elementu
     */
    private static long findOccurrencesOfSameTypeOfNetElement(NetElement element) {
        return elementsList.stream().filter(e -> e.getClass().equals(element.getClass())).count()+1;
    }

}
