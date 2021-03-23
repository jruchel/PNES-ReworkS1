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

    public NetModel(){
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

    /**
     * Porównuje wartości pól wyszukiwanego elementu do wartości pól wszystkich istniejących obiektów w liście,
     * jeśli jakakolwiek wartość pola obiektu jest równa wartości pola z listy, obiekt ten jest dodawany do poprzednio
     * stworzonej listy, a następnie zwraca listę wszystkich znalezionych obiektów
     * @param object wyszukiwany przez użytkownika obiekt, wartości jego pól są porównywane z wartościami pól obiektów
     * z listy w celu znalezienia poszukiwanych obiektów
     * @return listę znalezionych obiektów
     */
    //TODO publiczna do testów, po zakończeniu zmienić na protected
    public List<Object> findObjects(Object object) {
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
