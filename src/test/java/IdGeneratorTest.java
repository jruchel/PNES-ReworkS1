import org.ekipa.pnes.models.Place;
import org.ekipa.pnes.models.NetElement;
import org.ekipa.pnes.models.Transition;
import org.ekipa.pnes.models.token.IntegerTokenValue;
import org.ekipa.pnes.models.token.ValidationException;
import org.ekipa.pnes.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    private NetElement Rectangle1;
    private NetElement Rectangle2;
    private NetElement Rectangle3;
    private NetElement Circle1;
    private NetElement Circle2;
    private NetElement Circle3;


    @BeforeEach
    public void initialize() throws ValidationException {

        IdGenerator.reset();
        Rectangle1 = new Transition("", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle2 = new Transition("", "P", 4, 5, 20, Rectangle2, Rectangle3);
        Rectangle3 = new Transition("", "P", 4, 5, 20, Rectangle3, Rectangle1);

        Circle1 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);
        Circle2 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);
        Circle3 = new Place<IntegerTokenValue>("", "K", 2, 3, 5);


    }

    @Test
    public void doesIdWillBeChangedToCorrect() {
        Rectangle1.setId("P-5");

        String expected = "T-0";
        String actual = IdGenerator.setId(Rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesShapeHaveTheSameIdBeforeUsingSetIdAndAfter() {

        Rectangle1.setId("T-0");
        String expected = Rectangle1.getId();
        String actual = IdGenerator.setId(Rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdWillBeChangedForTheObjectWithTheSameFields() {
        Rectangle1 = new Transition("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle2 = new Transition("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle3 = new Transition("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);


        IdGenerator.setId(Rectangle1);
        IdGenerator.setId(Rectangle2);
        IdGenerator.setId(Rectangle3);

        String expected = "T-2";
        String actual = Rectangle3.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdGenerateWellForCircle() {

        String expected;
        String actual;

        IdGenerator.setId(Circle1);
        expected = "P-0";
        actual = Circle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Circle2);
        expected = "P-1";
        actual = Circle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Circle3);
        expected = "P-2";
        actual = Circle3.getId();

        assertEquals(expected, actual);

    }

    @Test
    public void doesIdGenerateWellForRectangle() {

        String expected;
        String actual;

        IdGenerator.setId(Rectangle1);
        expected = "T-0";
        actual = Rectangle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Rectangle2);
        expected = "T-1";
        actual = Rectangle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Rectangle3);
        expected = "T-2";
        actual = Rectangle3.getId();

        assertEquals(expected, actual);

    }

    @Test
    public void doesIdGenerateWellForCircleAndRectangleTogether() {

        String expected;
        String actual;

        IdGenerator.setId(Rectangle1);
        expected = "T-0";
        actual = Rectangle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Circle1);
        expected = "P-0";
        actual = Circle1.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Rectangle2);
        expected = "T-1";
        actual = Rectangle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Circle2);
        expected = "P-1";
        actual = Circle2.getId();

        assertEquals(expected, actual);

        IdGenerator.setId(Rectangle3);
        expected = "T-2";
        actual = Rectangle3.getId();

        assertEquals(expected, actual);


        IdGenerator.setId(Circle3);
        expected = "P-2";
        actual = Circle3.getId();

        assertEquals(expected, actual);


    }

}

