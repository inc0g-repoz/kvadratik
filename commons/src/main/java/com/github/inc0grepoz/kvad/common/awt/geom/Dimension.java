package com.github.inc0grepoz.kvad.common.awt.geom;

import java.awt.geom.Dimension2D;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Dimension extends Dimension2D {

    public double width, height;

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

}
