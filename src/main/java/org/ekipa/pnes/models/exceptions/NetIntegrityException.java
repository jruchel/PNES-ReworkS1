package org.ekipa.pnes.models.exceptions;

/**
 * Wyjątek używany przez klasę {@link org.ekipa.pnes.models.exceptions.ProhibitedConnectionException} do wysyłania komunikatu o próbie
 * wykonania zniekształcenia danych.
 */

public class NetIntegrityException extends Exception {
    public NetIntegrityException() {
        super("Net integrity violation");
    }

    public NetIntegrityException(String message) {
        super(message);
    }
}
