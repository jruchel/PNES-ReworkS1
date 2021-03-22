package org.ekipa.pnes.models.netModels;

/**
 * Wyjątek używany przez klasę {@link org.ekipa.pnes.models.netModels.NetModel} do wysyłania komunikatu o próbie
 * wykonania transofrmacji pomiędzy dwoma modelami nietransformowalnymi
 */
public class ImpossibleTransformationException extends Exception{
    public ImpossibleTransformationException(String message) {
        super(message);
    }

}
