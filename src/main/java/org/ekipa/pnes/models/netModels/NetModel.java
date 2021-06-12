package org.ekipa.pnes.models.netModels;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import lombok.Data;
import org.ekipa.pnes.models.elements.*;
import org.ekipa.pnes.models.exceptions.ImpossibleTransformationException;
import org.ekipa.pnes.utils.IdGenerator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public abstract class NetModel {
    private List<NetElement> netElements;

    public NetModel(List<NetElement> netElements) {
        this.netElements = netElements;
    }

    public NetModel() {
        this.netElements = new ArrayList<>();
    }

    @JsonIgnore
    protected NetElement getElement(String id) {
        return netElements.stream().filter(element -> element.getId().equals(id)).findFirst().orElse(null);
    }

    @JsonIgnore
    protected NetObject getObject(String id) {
        return netElements.stream()
                .filter(element -> element instanceof NetObject)
                .filter(netObject -> netObject.getId().equals(id))
                .map(netElement -> (NetObject) netElement).findFirst().orElse(null);
    }

//    private void setNetElements(List<NetElement> netElements) {
//        this.netElements = netElements;
//    }

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
     * @param cycles   ilość kroków
     * @return {@link java.util.List}<{@link org.ekipa.pnes.models.netModels.NetModel}> Lista modeli jako kroki symulacji
     */
    public static List<List<NetModel>> simulate(NetModel netModel, int cycles) throws JsonProcessingException, IllegalAccessException, InstantiationException {
        List<List<NetModel>> result = new ArrayList<>();

        result.add(netModel.wholeStep());
        for (int i = 0; i < cycles - 1; i++) {
            List<NetModel> previousCycle = result.get(i);
            NetModel lastInCycle = previousCycle.get(previousCycle.size() - 1);
            result.add(lastInCycle.wholeStep());
        }
        return result;
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
        if (!validateElement(element.getId())) return null;
        netElements.add(IdGenerator.setElementId(element));
        return element;
    }

    // TODO: dokumentacja

    /**
     * @param id
     */
    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteElement);
    }

    /**
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
     * @param netElementId id elementu wyszukiwanego przez użytkownika obiektu,
     *                     wartości jego pól są porównywane z wartościami pól obiektów
     *                      z listy w celu znalezienia poszukiwanych obiektów
     * @return listę znalezionych obiektów
     */
    protected List<NetElement> findObjects(String netElementId) {
        List<NetElement> elements = new ArrayList<>();
        List<Field> elementFields = getAllFields(getElement(netElementId));
        for (NetElement o : getNetElements()) {
            if (getAllFields(o).stream().anyMatch(field -> {
                for (Field f : elementFields) {
                    if (!f.getName().equals(field.getName())) continue;
                    boolean fAccessible = f.isAccessible();
                    boolean fieldAccessible = field.isAccessible();
                    f.setAccessible(true);
                    field.setAccessible(true);
                    try {
                        if (f.get(getElement(netElementId)).equals(field.get(o))) return true;
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
     * Odnajduje wszystkie łuki, które są połączone z obiektem sieci.
     *
     * @param netObject Obiekt sieci
     * @return Łuk
     */
    @JsonIgnore
    public Set<Arc> getArcsByNetObject(NetObject netObject) {
        return netObject.getArcs();
    }

    /**
     * Odnajduje pare obiektów sieci, z którymi połączony jest łuk
     *
     * @param arcId Id łuku
     * @return Para obiektów połączone łukiem (początek, koniec)
     */
    public Pair<NetObject, NetObject> getNetObjectsByArc(String arcId) {
        Stream<NetElement> netElementStream = netElements.stream()
                .filter(netElement -> !(netElement.getId().equals(arcId)));

        NetObject start = (NetObject) netElementStream
                .filter(netElement -> ((Arc) getElement(arcId)).getStart().getId().equals(netElement.getId()))
                .findFirst()
                .orElse(null);

        NetObject end = (NetObject) netElementStream
                .filter(netElement -> ((Arc) getElement(arcId)).getEnd().getId().equals(netElement.getId()))
                .findFirst()
                .orElse(null);

        return new Pair<>(start, end);
    }

    /**
     * Edytuje obiekt z całego modelu jeśli przejdzie walidacje.
     * Jeżeli zostaną dodane własne klasy i własne modele, ta metoda powinna zostać nadpisana.
     *
     * @param actualId Id dokładnego obiekt, który ma zostać zaktualizowany.
     * @param newId    Id obiektu, z którego ma zamienić wartości.
     * @return Zwraca zaktualizowany obiekt.
     */
    public NetElement editElement(String actualId, String newId) {
        if (!(actualId.charAt(0)==newId.charAt(0))) return getElement(actualId);
        if (!validateElement(newId)) return getElement(actualId);
        List<Field> fieldsBefore = getAllFields(getElement(actualId));
        List<String> ignoredFields = Arrays.asList("arcs", "id", "start", "end");
        for (Field f : fieldsBefore.stream().filter(f -> !ignoredFields.contains(f.getName())).collect(Collectors.toList())) {
            f.setAccessible(true);
            try {
                f.set(getElement(actualId), f.get(getElement(newId)));
            } catch (IllegalAccessException ignored) {

            }
            f.setAccessible(false);
        }
        return getElement(actualId);
    }

    /**
     * Przeprowadza walidacje dowolnych elementów w modelu sieci.
     * Metoda musi być nadpisana poprawnie, aby móc korzystać z sieci.
     *
     * @param id Id obiektu do walidacji.
     * @return Wynik walidacji.
     */
    protected abstract boolean validateElement(String id);

    /**
     * Dodaje podaje tokeny do podanego miejsca.
     *
     * @param placeId  Id miejsca do którego mają zostać dodane tokeny.
     * @param tokens Tokeny.
     */
    protected abstract void addTokens(String placeId, Object tokens);

    /**
     * Wykonuje pojedynczy krok symulacji.
     *
     * @return Model po wykonaniu kroku.
     */
    protected List<NetModel> wholeStep() throws JsonProcessingException {
        List<NetModel> currentSimulationSteps = new ArrayList<>();
        List<Transition> readyTransitions = prepareTransitions();
        currentSimulationSteps.add(this.copy());
        List<Transition> transitionsToRun = selectTransitionsToRun(readyTransitions);
        transitionsToRun = transitionsToRun.stream().peek(Transition::setRunning).collect(Collectors.toList());
        currentSimulationSteps.add(this.copy());
        transitionsToRun.forEach(this::runTransition);
        currentSimulationSteps.add(this.copy());
        getTransitionsWithState(Transition.TransitionState.Ready).forEach(Transition::setUnready);
        currentSimulationSteps.add(this.copy());
        return currentSimulationSteps;
    }

    private NetModel copy() throws JsonProcessingException {
        String json = serialize();
        return deserialize(json);
    }

    public abstract String serialize() throws JsonProcessingException;
    public abstract NetModel deserialize(String  json) throws JsonProcessingException;


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
    @JsonIgnore
    protected List<Transition> getTransitionsWithState(Transition.TransitionState state) {
        return netElements.stream()
                .filter(element -> element instanceof Transition)
                .filter(transition -> ((Transition) transition).getState().equals(state))
                .map(netElement -> (Transition) netElement)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @JsonIgnore
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
