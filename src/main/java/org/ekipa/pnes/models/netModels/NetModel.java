package org.ekipa.pnes.models.netModels;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.*;

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

    protected NetElement getElement(String id) {
        return netElements.stream().filter(element -> element.getId().equals(id)).findFirst().orElse(null);
    }

    protected NetObject getObject(String id) {
        return netElements.stream()
                .filter(element -> element instanceof NetObject)
                .filter(netObject -> netObject.getId().equals(id))
                .map(netElement -> (NetObject) netElement).findFirst().orElse(null);
    }


    public NetModel() {
        this.netElements = new ArrayList<>();
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
     * @param element Obiekt do usunięcia
     */
    public void deleteElement(NetElement element) {
        netElements = netElements.stream().filter(net -> !net.equals(element)).collect(Collectors.toList());
    }

    /**
     * Dodaje podany obiekt do modelu sieci jeśli przejdzie walidacje.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param element Obiekt do dodania
     * @return Dodany obiekt
     */
    public NetElement addElement(NetElement element) {
        if (!validateElement(element)) return element;
        netElements.add(element);
        return element;
    }

    // TODO: dokumentacja
    /**
     *
     * @param id
     */
    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteElement);
    }

    /**
     *
     * @param name
     */
    public void deleteByName(String name) {
        netElements.stream().filter(net -> net.getName().equals(name)).forEach(this::deleteElement);
    }

    /**
     * Porównuje wartości pól wyszukiwanego elementu do wartości pól wszystkich istniejących obiektów w liście,
     * jeśli jakakolwiek wartość pola obiektu jest równa wartości pola z listy, obiekt ten jest dodawany do poprzednio
     * stworzonej listy, a następnie zwraca listę wszystkich znalezionych obiektów
     *
     * @param element wyszukiwany przez użytkownika obiekt, wartości jego pól są porównywane z wartościami pól obiektów
     *                z listy w celu znalezienia poszukiwanych obiektów
     * @return listę znalezionych obiektów
     */
    protected List<NetElement> findObjects(NetElement element) {
        List<NetElement> elements = new ArrayList<>();
        List<Field> elementFields = getAllFields(element);
        for (NetElement o : getNetElements()) {
            if (getAllFields(o).stream().anyMatch(field -> {
                for (Field f : elementFields) {
                    if (!f.getName().equals(field.getName())) continue;
                    boolean fAccessible = f.isAccessible();
                    boolean fieldAccessible = field.isAccessible();
                    f.setAccessible(true);
                    field.setAccessible(true);
                    try {
                        if (f.get(element).equals(field.get(o))) return true;
                    } catch (Exception ignored) {

                    }
                    f.setAccessible(fAccessible);
                    field.setAccessible(fieldAccessible);
                }
                return false;
            })) {
                elements.add(o);
            }
        }
        return elements;
    }

    /**
     * Edytuje obiekt z całego modelu jeśli przejdzie walidacje.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param actualObject Dokładny obiekt, który ma zostać zaktualizowany.
     * @param newObject    Obiekt, z którego ma zamienić wartości.
     * @return Zwraca zaktualizowany obiekt.
     */
    public NetElement editElement(NetElement actualObject, NetElement newObject) {
        if (!actualObject.getClass().equals(newObject.getClass())) return actualObject;
        if (!validateElement(newObject)) return actualObject;
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
    protected abstract boolean validateElement(NetElement o);

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
                .map(netElement -> (Transition) netElement)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
