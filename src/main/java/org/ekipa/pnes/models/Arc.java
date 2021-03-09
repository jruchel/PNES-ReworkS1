package org.ekipa.pnes.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Arc {
    private NetElement start;
    private NetElement end;
    private double weight;
}
