package org.ekipa.pnes.models.elements.token;

public class ValidationException extends Exception {

    /**
     * Wyjątek wyrzuacy przy negatywnie przeprowadzonej walidacji danych
     *
     * @param message wiadomosc wyswietlana przy wyrzuceniu wyjątku
     */

    public ValidationException(String message) {
        super(message);
    }
}