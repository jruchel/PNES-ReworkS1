package org.ekipa.pnes.models.exceptions;

public class ProhibitedConnectionException extends NetIntegrityException {
    public ProhibitedConnectionException(String message) {
        super(message);
    }
}
