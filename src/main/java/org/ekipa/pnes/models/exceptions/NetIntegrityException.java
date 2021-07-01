package org.ekipa.pnes.models.exceptions;

/**
 * Wyjątek jest wyrzucany przy braku spójności sieci.
 */

public class NetIntegrityException extends Exception {
    public NetIntegrityException() {
        super("Net integrity violation");
    }

    public NetIntegrityException(String message) {
        super(message);
    }
}
