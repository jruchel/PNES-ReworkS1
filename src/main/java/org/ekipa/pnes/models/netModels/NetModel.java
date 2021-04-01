package org.ekipa.pnes.models.netModels;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
     * @param model model {@link org.ekipa.pnes.models.netModels.NetModel}
     */
    public abstract void translate(NetModel model);

    /**
     * Tworzy model obecnego typu na podstawie przekazanego modelu,jeżeli jest to możliwe
     * w przeciwnym razie wyrzuca wyjątek o niemożliwej transfomracji
     *
     * @param model model {@link org.ekipa.pnes.models.netModels.NetModel}
     * @throws ImpossibleTransformationException - wyjątek informujący o niemożliwej transformacji
     */
    public abstract void transform(NetModel model) throws ImpossibleTransformationException;

    /**
     * Wykonuje podaną ilość kroków symulacji dla podanej sieci
     *
     * @param netModel sieć do symulowania
     * @param steps    ilość kroków
     * @return {@link java.util.List}<{@link org.ekipa.pnes.models.netModels.NetModel}> Lista modeli jako kroki symulacji
     */
    public static List<NetModel> simulate(NetModel netModel, int steps) {
        List<NetModel> netModels = new ArrayList<>();
        netModels.add(netModel.nextStep());
        for (int i = 0; i < steps - 1; i++) {
            netModels.add(netModels.get(i).nextStep());
        }
        return netModels;
    }

    /**
     * Usuwa podany obiekt z całego modelu sieci.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param object Obiekt do usunięcia
     */
    protected void deleteObject(Object object) {
        if (object instanceof Arc) {
            arcList = arcList.stream().filter(e -> !e.equals(object)).collect(Collectors.toList());
        }
        if (object instanceof NetElement) {
            netElements = netElements.stream().filter(net -> !net.equals(object)).collect(Collectors.toList());
        }
    }

    /**
     * Dodaje podany obiekt do modelu sieci jeśli przejdzie walidacje.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param object Obiekt do dodania
     * @return Dodany obiekt
     */
    protected Object addObject(Object object) {
        if (!validateObject(object)) return object;
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

    /**
     * Edytuje obiekt z całego modelu jeśli przejdzie walidacje.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param actualObject Dokładny obiekt, który ma zostać zaktualizowany.
     * @param newObject    Obiekt, z którego ma zamienić wartości.
     * @return Zwraca zaktualizowany obiekt.
     */
    protected Object editObject(Object actualObject, Object newObject) {
        if (!actualObject.getClass().equals(newObject.getClass())) return actualObject;
        if (!validateObject(newObject)) return actualObject;
        List<Field> fieldsBefore = getAllFields(actualObject);
        List<String> ignoredFields = Arrays.asList("arcs", "id", "start", "end");
        for (Field f : fieldsBefore.stream().filter(f -> !ignoredFields.contains(f.getName())).collect(Collectors.toList())) {
            f.setAccessible(true);
            try {
                f.set(actualObject, f.get(newObject));
            } catch (IllegalAccessException ignored) {

            }
            f.setAccessible(false);
        }
        return actualObject;
    }

    /**
     * Przeprowadza walidacje dowolnych elementów w modelu sieci.
     * Metoda musi być nadpisana poprawnie, aby móc korzystać z sieci.
     *
     * @param o Obiekt do walidacji.
     * @return Wynik walidacji.
     */
    protected abstract boolean validateObject(Object o);

    /**
     * Dodaje podaje tokeny do podanego miejsca.
     *
     * @param place  Miejsce do którego mają zostać dodane tokeny.
     * @param tokens Tokeny.
     */
    protected abstract void addTokens(Place place, Object tokens);

    /**
     * Wykonuje pojedynczy krok symulacji.
     *
     * @return Model po wykonaniu kroku.
     */
    protected NetModel nextStep() {
        List<Transition> readyTransitions = prepareTransitions();
        List<Transition> transitionsToRun = selectTransitionsToRun(readyTransitions);
        transitionsToRun.forEach(this::runTransition);
        return this;
    }

    /**
     * Uruchamia podaną tranzycję.
     *
     * @param transition Tranzycja do uruchomienia.
     * @return Czy uruchomiono.
     */
    protected abstract boolean runTransition(Transition transition);

    /**
     * Odnajduje te tranzycje, które mogą zostać przygotowane, następnie ustawia je jako gotowe.
     *
     * @return {@link java.util.List}<{@link org.ekipa.pnes.models.elements.Transition}> Lista gotowych tranzycji.
     */
    protected abstract List<Transition> prepareTransitions();

    /**
     * Wybiera te tranzycje spośród gotowych, które mają zostać uruchomione.
     *
     * @param transitions {@link java.util.List}<{@link org.ekipa.pnes.models.elements.Transition}>Lista tranzycji do wybrania.
     * @return {@link java.util.List}<{@link org.ekipa.pnes.models.elements.Transition}> Lista tranzycji do uruchomienia.
     */
    protected abstract List<Transition> selectTransitionsToRun(List<Transition> transitions);

    /**
     * Zwraca te tranzycje sieci, które znajdują się w podanym stanie.
     *
     * @param state {@link org.ekipa.pnes.models.elements.Transition.TransitionState}
     * @return {@link java.util.List}<{@link org.ekipa.pnes.models.elements.Transition}> lista tranzycji.
     */
    protected List<Transition> getTransitionsWithState(Transition.TransitionState state) {
        return netElements.stream()
                .filter(element -> element instanceof Transition)
                .filter(transition -> ((Transition) transition).getState().equals(state))
                .map(netElement -> {
                    if (netElement instanceof Transition) return (Transition) netElement;
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca wszystkie obiekty w sieci.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @return {@link java.util.List}<{@link java.lang.Object}> Lista obiektów w sieci.
     */
    protected List<Object> getAllObjects() {
        List<Object> objects = new ArrayList<>(arcList);
        objects.addAll(netElements);
        return objects;
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
}
