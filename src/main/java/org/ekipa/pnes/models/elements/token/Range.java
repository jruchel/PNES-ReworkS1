package org.ekipa.pnes.models.elements.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Range {
    private double start, end;

    public double getRangeSize() {
        return end - start;
    }
}
