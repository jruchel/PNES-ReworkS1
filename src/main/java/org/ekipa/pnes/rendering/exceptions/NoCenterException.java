package org.ekipa.pnes.rendering.exceptions;

import org.ekipa.pnes.models.exceptions.ProhibitedConnectionException;

public class NoCenterException extends ProhibitedConnectionException {
    public NoCenterException(String message){
        super(message);
    }
}
