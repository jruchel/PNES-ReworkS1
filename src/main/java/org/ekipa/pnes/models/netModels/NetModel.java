package org.ekipa.pnes.models.netModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor

public abstract class NetModel {
    protected List<NetElement> netElements;
    protected List<Arc> arcList;

    /**
     * Tworzy model obecnego typu na podstawie porównania parametry sieci z innym modelem i zamienia te parametry
     * które są możliwe do zmiany, w przeciwnym razie te dane których się nie da przetłumaczyć
     * to stworzy dla nich odpowiedniki i ustawi im wartości
     *
     * @param model model {@link org.ekipa.pnes.models.netModels}
     */
    public abstract void translate(NetModel model);

    /**
     * Tworzy model obecnego typu na podstawie przekazanego modelu,jeżeli jest to możliwe
     * w przeciwnym razie wyrzuca wyjątek o niemożliwej transfomracji
     *
     * @param model model {@link org.ekipa.pnes.models.netModels}
     * @throws ImpossibleTransformationException - wyjątek informujący o niemożliwej transformacji
     */
    public abstract void transform(NetModel model) throws ImpossibleTransformationException;

    protected void deleteObject(Object object) {
        if (object instanceof Arc) {
            arcList = arcList.stream().filter(e -> !e.equals(object)).collect(Collectors.toList());
        }
        if (object instanceof NetElement) {
            netElements = netElements.stream().filter(net -> !net.equals(object)).collect(Collectors.toList());
        }
    }

    protected void addObject(Object object) {
        if (object instanceof Arc) {
            arcList.add((Arc) object);
            return;
        }
        if (object instanceof NetElement) {
            netElements.add((NetElement) object);
            return;
        }
    }

    protected List<Object> findObjects(Object object) {
        List<Object> objects = new ArrayList<>();
        Field[] objectfields = object.getClass().getDeclaredFields();
        for (Object o : getAllObjects()) {
            if (Arrays.stream(o.getClass().getDeclaredFields()).anyMatch(field -> {
                for (Field f : objectfields) {
                    f.setAccessible(true);
                    try {
                        if (f.get(object).equals(field.get(o))) return true;
                    } catch (IllegalAccessException ignored) {

                    }
                }
                return false;
            })) {
                objects.add(o);
            }
        }
        return objects;
    }

    protected List<Object> getAllObjects() {
        List<Object> objects = new ArrayList<>(arcList);
        objects.addAll(netElements);
        return objects;
    }
}
