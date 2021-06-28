package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.NetObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdGenerator {

    private static List<NetElement> elementsList;
    private static Map<String, NetElement> idMap;

    static {
        reset();
    }


    /**
     * Czyści listę elementów
     */
    public static void resetElements() {
        elementsList = new ArrayList<>();
        idMap = new HashMap<>();

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
        long numberOfOccupiedElements = findOccurrencesOfSameTypeOfNetElement(element);
        String id = String.format("%s%d", element.getClass().getSimpleName().charAt(0), numberOfOccupiedElements);
            while (idMap.containsKey(id)) {
                numberOfOccupiedElements++;
                id = (String.format("%s%d", element.getClass().getSimpleName().charAt(0), numberOfOccupiedElements));
            }
            element.setId(id);
            idMap.put(element.getId(), element);
            elementsList.add(element);
        return element;
    }

    /**
     * Zlicza ilość wystąpień typu podanego elementu następnie ją zwraca
     *
     * @param element {@link NetObject}, może nim Place lub Transition
     * @return liczbę wystąpień typu podanego elementu
     */
    private static long findOccurrencesOfSameTypeOfNetElement(NetElement element) {
        return elementsList.stream().filter(e -> e.getClass().equals(element.getClass())).count() + 1;
    }

}
