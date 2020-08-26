package com.informatica.tutorialfirebase;

import java.io.Serializable;

public class Tag implements Serializable{
    public String id;
    public String nombre;

    public Tag(){}

    public Tag(String id, String nom)
    {
        this.id=id;
        this.nombre=nom;
    }

    public Tag(String nom)
    {
        this.nombre=nom;
    }


    public void setId(String id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public String getNombre() {
        return nombre;
    }
    public String getId(){return id;}

}
