package org.ekipa.pnes.models.exceptions;

public class NetIntegrityException extends Exception {
    public NetIntegrityException() {
        super("Net integrity violation");
    }

    public NetIntegrityException(String message) {
        super(message);
    }
}
