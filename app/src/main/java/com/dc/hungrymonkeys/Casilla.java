package com.dc.hungrymonkeys;

/**
 * Created by CristinaPosada on 28/11/2017.
 */

class Casilla implements Imprimible {
    String Nombre;

    public Casilla() {
        this.Nombre = "X";
    }

    @Override
    public String getTipo() {
        return this.Nombre;
    }

}
