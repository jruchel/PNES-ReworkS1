package org.ekipa.pnes.models.elements.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Range {
    private double start, end;

    /**
     * Korzystając z początku i końca przedziału ustala jego rozpiętość
     * @return Rozpiętość przedziału
     */
    public double getRangeSize() {
        return end - start;
    }
}
