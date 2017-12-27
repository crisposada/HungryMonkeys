package com.dc.hungrymonkeys;

/**
 * Created by CristinaPosada on 01/12/2017.
 */

class Player implements Imprimible
{
    String Nombre;
    int posicionX;
    int posicionY;
    //Posicion 0: Manzanas, 1: Naranjas, 2: Peras.
    int [] Manzanas_Naranjas_Peras = new int [3];
    int categorias;

    public int[] getManzanas_Naranjas_Peras()
    {
        return Manzanas_Naranjas_Peras;
    }

    public int getPosicionX()
    {
        return posicionX;
    }

    public void setPosicionX(int posicionX)
    {
        this.posicionX = posicionX;
    }

    public int getPosicionY()
    {
        return posicionY;
    }

    public void setPosicionY(int posicionY)
    {
        this.posicionY = posicionY;
    }

    public Player(String Nombre,int x ,int y)
    {
        this.Nombre=Nombre;
        this.posicionX=x;
        this.posicionY=y;
    }

    public String getNombre()
    {
        return Nombre;
    }

    public void setNombre(String nombre)
    {
        Nombre = nombre;
    }

    @Override
    public String getTipo()
    {
        return this.Nombre;
    }


}
