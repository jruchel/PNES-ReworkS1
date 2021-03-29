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

    protected NetElement getElement(int index) {
        return netElements.get(index);
    }

    protected Arc getArc(int index) {
        return arcList.get(index);
    }

    public NetModel() {
        this.netElements = new ArrayList<>();
        this.arcList = new ArrayList<>();
    }

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

    protected Object addObject(Object object) {
        if (object instanceof Arc) {
            arcList.add((Arc) object);
            return object;
        }
        if (object instanceof NetElement) {
            netElements.add((NetElement) object);
            return object;
        }
        return object;
    }

    /**
     * Porównuje wartości pól wyszukiwanego elementu do wartości pól wszystkich istniejących obiektów w liście,
     * jeśli jakakolwiek wartość pola obiektu jest równa wartości pola z listy, obiekt ten jest dodawany do poprzednio
     * stworzonej listy, a następnie zwraca listę wszystkich znalezionych obiektów
     *
     * @param object wyszukiwany przez użytkownika obiekt, wartości jego pól są porównywane z wartościami pól obiektów
     *               z listy w celu znalezienia poszukiwanych obiektów
     * @return listę znalezionych obiektów
     */
    protected List<Object> findObjects(Object object) {
        List<Object> objects = new ArrayList<>();
        List<Field> objectFields = getAllFields(object);
        for (Object o : getAllObjects()) {
            if (getAllFields(o).stream().anyMatch(field -> {
                for (Field f : objectFields) {
                    if (!f.getName().equals(field.getName())) continue;
                    boolean fAccessible = f.isAccessible();
                    boolean fieldAccessible = field.isAccessible();
                    f.setAccessible(true);
                    field.setAccessible(true);
                    try {
                        if (f.get(object).equals(field.get(o))) return true;
                    } catch (Exception ignored) {
                        
                    }
                    f.setAccessible(fAccessible);
                    field.setAccessible(fieldAccessible);
                }
                return false;
            })) {
                objects.add(o);
            }
        }
        return objects;
    }

    protected Object editObject(Object actualObject, Object newObject) throws IllegalAccessException {
        if (!actualObject.getClass().equals(newObject.getClass())) return actualObject;
        List<Field> fieldsBefore = getAllFields(actualObject);
        List<String> ignoredFields = Arrays.asList("arcs", "id", "start", "end");
        for (Field f : fieldsBefore.stream().filter(f -> !ignoredFields.contains(f.getName())).collect(Collectors.toList())) {
            f.setAccessible(true);
            f.set(actualObject, f.get(newObject));
            f.setAccessible(false);
        }
        return actualObject;
    }

    private List<Field> getAllFields(Object o) {
        List<Field> fields = new ArrayList<>();
        Class clazz = o.getClass();
        while (!clazz.equals(Object.class)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    protected List<Object> getAllObjects() {
        List<Object> objects = new ArrayList<>(arcList);
        objects.addAll(netElements);
        return objects;
    }
}
