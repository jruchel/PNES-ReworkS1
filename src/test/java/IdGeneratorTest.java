import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.models.elements.token.IntegerTokenValue;
import org.ekipa.pnes.models.elements.token.ValidationException;
import org.ekipa.pnes.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    private NetElement rectangle1;
    private NetElement rectangle2;
    private NetElement rectangle3;
    private NetElement circle1;
    private NetElement circle2;
    private NetElement circle3;


    @BeforeEach
    public void initialize() throws ValidationException {

        IdGenerator.resetElements();
        rectangle1 = new Transition("", "P", 2, 3, 20);
        rectangle2 = new Transition("", "P", 4, 5, 20);
        rectangle3 = new Transition("", "P", 4, 5, 20);

        circle1 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);
        circle2 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);
        circle3 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);


    }

    @Test
    public void doesIdWillBeChangedToCorrect() {
        rectangle1.setId("P5");

        String expected = "T0";
        String actual = IdGenerator.setElementId(rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesShapeHaveTheSameIdBeforeUsingSetIdAndAfter() {

        rectangle1.setId("T0");
        String expected = rectangle1.getId();
        String actual = IdGenerator.setElementId(rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdWillBeChangedForTheObjectWithTheSameFields() {
        rectangle1 = new Transition("P0", "P", 2, 3, 20);
        rectangle2 = new Transition("P0", "P", 2, 3, 20);
        rectangle3 = new Transition("P0", "P", 2, 3, 20);


        IdGenerator.setElementId(rectangle1);
        IdGenerator.setElementId(rectangle2);
        IdGenerator.setElementId(rectangle3);

        String expected = "T2";
        String actual = rectangle3.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdGenerateWellForCircle() {

        String expected;
        String actual;

        IdGenerator.setElementId(circle1);
        expected = "P0";
        actual = circle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(circle2);
        expected = "P1";
        actual = circle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(circle3);
        expected = "P2";
        actual = circle3.getId();

        assertEquals(expected, actual);

    }

    @Test
    public void doesIdGenerateWellForRectangle() {

        String expected;
        String actual;

        IdGenerator.setElementId(rectangle1);
        expected = "T0";
        actual = rectangle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(rectangle2);
        expected = "T1";
        actual = rectangle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(rectangle3);
        expected = "T2";
        actual = rectangle3.getId();

        assertEquals(expected, actual);

    }

    @Test
    public void doesIdGenerateWellForCircleAndRectangleTogether() {

        String expected;
        String actual;

        IdGenerator.setElementId(rectangle1);
        expected = "T0";
        actual = rectangle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(circle1);
        expected = "P0";
        actual = circle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(rectangle2);
        expected = "T1";
        actual = rectangle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(circle2);
        expected = "P1";
        actual = circle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setElementId(rectangle3);
        expected = "T2";
        actual = rectangle3.getId();

        assertEquals(expected, actual);


        IdGenerator.setElementId(circle3);
        expected = "P2";
        actual = circle3.getId();

        assertEquals(expected, actual);


    }

}

