package com.dc.hungrymonkeys;

/**
 * Created by CristinaPosada on 28/11/2017.
 */

class Fruit implements Imprimible
{
    String Nombre;
    Integer Fila;
    Integer Columna;
    int Valor;


    public Integer getValor() {
        return Valor;
    }

    public void setValor(Integer valor) {
        Valor = valor;
    }

    public Integer getFila() {
        return Fila;
    }

    public void setFila(Integer fila) {
        Fila = fila;
    }

    public Integer getColumna() {
        return Columna;
    }

    public void setColumna(Integer columna) {
        Columna = columna;
    }

    public Fruit(String Nombre,Integer fila,Integer columna)
    {
        this.Nombre=Nombre;
        this.Fila=fila;
        this.Columna=columna;
        this.Valor= Matrix.VALOR_0_0;
    }

    public String getNombre()
    {
        return Nombre;
    }

    public void setNombre(String nombre)
    {
        Nombre = nombre;
    }


    public String getTipo()
    {
        return this.Nombre;
    }

}
