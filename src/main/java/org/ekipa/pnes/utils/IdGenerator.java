package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.NetObject;

import java.util.*;

public class IdGenerator {

    private static Map<String, NetElement> netElements;

    static {
        resetElements();
    }

    /**
     * Czyści mapę elementów
     */
    public static void resetElements() {
        netElements = new HashMap<>();

    }
    /**
     * Ustawia odpowiednie id w zależności od ilości wystąpień dla podanego elementu,
     * w przypadku gdy początkowe id jest zajęte licznik zwiększa się do momentu znalezienia wolnego id.
     * Element po dodaniu właściwego id zostaje zapisany do mapy, a następnie zostaje zwrócony.
     *
     * @param element {@link NetElement} sieci dla którego ma być ustawione id.
     * @return podany element z ustawionym poprawnym id
     */
    public static NetElement setElementId(NetElement element) {
        long numberOfOccupiedElements = findOccurrencesOfSameTypeOfNetElement(element);
        String id = String.format("%s%d", element.getClass().getSimpleName().charAt(0), numberOfOccupiedElements);
        while (netElements.containsKey(id)) {
            numberOfOccupiedElements++;
            id = (String.format("%s%d", element.getClass().getSimpleName().charAt(0), numberOfOccupiedElements));
        }
        element.setId(id);
        netElements.put(element.getId(), element);
        return element;
    }

    /**
     * Zlicza ilość wystąpień typu podanego elementu następnie ją zwraca
     *
     * @param element {@link NetElement} sieci dla którego ma zostać policzona ilość wszystkich wystąpień tego samego typu.
     * @return liczbę wystąpień typu podanego elementu
     */
    private static long findOccurrencesOfSameTypeOfNetElement(NetElement element) {
        Set<NetElement> netElementsSet = new HashSet<>();
        for (String key : netElements.keySet()) {
            netElementsSet.add(netElements.get(key));
        }
        return netElementsSet.stream().filter(e -> e.getClass().equals(element.getClass())).count() + 1;
    }

}
