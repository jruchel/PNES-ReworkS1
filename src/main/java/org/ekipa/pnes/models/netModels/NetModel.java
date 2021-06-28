package org.ekipa.pnes.models.netModels;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.ekipa.pnes.models.elements.*;
import org.ekipa.pnes.models.exceptions.ImpossibleTransformationException;
import org.ekipa.pnes.utils.IdGenerator;
import org.ekipa.pnes.utils.Pair;

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

    protected NetElement getElement(String id) {
        return netElements.stream().filter(element -> element.getId().equals(id)).findFirst().orElse(null);
    }

    protected NetObject getObject(String id) {
        return netElements.stream()
                .filter(element -> element instanceof NetObject)
                .filter(netObject -> netObject.getId().equals(id))
                .map(netElement -> (NetObject) netElement).findFirst().orElse(null);
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
        if (!validateElement(element)) return null;
        netElements.add(IdGenerator.setElementId(element));
        return element;
    }

    /**
     * Usuwanie elementu sieci po id {@link org.ekipa.pnes.models.elements.NetElement}
     *
     * @param id id elementu do usunięcia
     */
    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteElement);
    }

    /**
     * Usuwanie elemtu sieci po nazwie {@link org.ekipa.pnes.models.elements.NetElement}
     *
     * @param name nazwa elementu do usunięcia
     */
    public void deleteByName(String name) {
        netElements.stream().filter(net -> net.getName().equals(name)).forEach(this::deleteElement);
    }

    /**
     * Odnajduje wszystkie łuki, które są połączone z obiektem sieci.
     *
     * @param netObject Obiekt sieci
     * @return Łuk
     */
    public Set<Arc> getArcsByNetObject(NetObject netObject) {
        return netObject.getArcs();
    }

    /**
     * Odnajduje wszystkie łuki po id, które są połączone z obiektem sieci.
     *
     * @param id Obiektu sieci
     * @return Łuk
     */

    public Set<Arc> getArcsByNetObjectId(String id) {
        return getObject(id).getArcs();
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
        if (!(actualId.charAt(0) == newId.charAt(0))) return getElement(actualId);
        if (!validateElement(getElement(newId))) return getElement(actualId);
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
     * @param o Id obiektu do walidacji.
     * @return Wynik walidacji.
     */
    protected abstract boolean validateElement(NetElement o);

    /**
     * Dodaje podaje tokeny do podanego miejsca.
     *
     * @param placeId Id miejsca do którego mają zostać dodane tokeny.
     * @param tokens  Tokeny.
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

    /**
     * Tworzy kopie obecnego stanu tej klasy {@link org.ekipa.pnes.models.netModels.NetModel} bez referencji
     * @return zwraca skopiowany element
     */

    private NetModel copy() throws JsonProcessingException {
        String json = serialize(this);
        return deserialize(json);
    }

    /**
     * Serializuje dany obiekt do JSON'a w postaci stringa
     * @param netModel Model sieci do serializowania
     * @return zwraca serializowany obiekt
     */

    public abstract String serialize(NetModel netModel) throws JsonProcessingException;

    /**
     * Deserializuje dany obiekt w postaci JSON'a
     * @param json Obiekt {@link org.ekipa.pnes.models.netModels.NetModel} w postaci JSON'a
     * @return zwraca deserializowany obiekt
     */

    public abstract NetModel deserialize(String json) throws JsonProcessingException;


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

    @JsonIgnore
    private List<Transition> getAllTransitions(){
        return netElements.stream()
                .filter(element -> element instanceof Transition)
                .map(netElement -> (Transition) netElement)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
