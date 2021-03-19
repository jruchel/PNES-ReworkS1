package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.elements.NetElement;

import java.util.ArrayList;
import java.util.List;

public class IdGenerator {

    private static List<NetElement> elementsList;

    static {
        elementsList = new ArrayList<>();
    }

    public static void reset() {
        elementsList = new ArrayList<>();
    }

    public static NetElement setId(NetElement element) {
        if (elementsList.contains(element)) return element;
        element.setId(String.format("%s%d", element.getClass().getSimpleName().charAt(0), findOccurrencesOfSameType(element)));
        elementsList.add(element);
        return element;
    }

    private static long findOccurrencesOfSameType(NetElement element) {
        return elementsList.stream().filter(e -> e.getClass().equals(element.getClass())).count();
    }
}
