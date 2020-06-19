package com.informatica.tutorialfirebase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nombre;

    // En caso de tener foto, podés utilizar este constructor
    /*public Alumno(String nombre, String division, int calificacion, String foto) {
        this.nombre = nombre;
        this.division = division;
        this.calificacion = calificacion;
        this.foto = foto;
    }*/
    public Usuario(){}


    public Usuario(String id, String nombre) {
        this.id=id;
        this.nombre=nombre;
    }
    public Usuario(String nombre) {
        this.nombre=nombre;
    }

    public String getId(){return  id;}

    public void setId(String id){this.id=id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("IdUsuario", this.id);
        result.put("Nombre", this.nombre);

        return result;
    }
}