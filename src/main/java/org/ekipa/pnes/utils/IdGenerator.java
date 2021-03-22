package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.NetElement;

import java.util.ArrayList;
import java.util.List;

public class IdGenerator {

    private static List<NetElement> elementsList;

    static {
        elementsList = new ArrayList<>();
    }

    /**
     * Czyści listę elementów
     */
    public static void reset() {
        elementsList = new ArrayList<>();
    }

    /**
     * Sprawdza czy lista posiada podany element, jeśli tak zwraca ten element, jeśli nie ustawia podanemu elementowi id,
     * następnie dodaje ten element do listy i zwraca go
     * @param element pobiera element{@link org.ekipa.pnes.models.elements.NetElement}
     * @return Zwraca podany element
     */
    public static NetElement setId(NetElement element) {
        if (elementsList.contains(element)) return element;
        element.setId(String.format("%s%d", element.getClass().getSimpleName().charAt(0), findOccurrencesOfSameType(element)));
        elementsList.add(element);
        return element;
    }

    /**
     * Zlicza ilość wystąpień typu podanego elementu następnie zwraca policzoną liczbę wystąpień
     * @param element Pobiera element {@link org.ekipa.pnes.models.elements.NetElement}
     * @return Zwraca liczbę wystąpień typu podanego elementu
     */
    private static long findOccurrencesOfSameType(NetElement element) {
        return elementsList.stream().filter(e -> e.getClass().equals(element.getClass())).count();
    }
}
