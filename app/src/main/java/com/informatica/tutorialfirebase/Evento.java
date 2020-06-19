package com.informatica.tutorialfirebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Evento {
    private String id;
    private String titulo;
    private String fecha;
    private int duracion;
    private String hora;
    private int importancia;
    private ArrayList<String> complementos;
    private ArrayList<String> tags;
    private Boolean lluvia;

    // En caso de tener foto, podés utilizar este constructor
    /*public Alumno(String nombre, String division, int calificacion, String foto) {
        this.nombre = nombre;
        this.division = division;
        this.calificacion = calificacion;
        this.foto = foto;
    }*/
    public Evento(){}


    public Evento(String id, String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia) {
        this.id=id;
        this.titulo=titulo;
        this.fecha=fecha;
        this.duracion=duracion;
        this.hora=hora;
        this.importancia=importancia;
        this.complementos=complementos;
        this.tags=tags;
        this.lluvia=lluvia;
    }
    public Evento(String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia) {
        this.titulo=titulo;
        this.fecha=fecha;
        this.duracion=duracion;
        this.hora=hora;
        this.importancia=importancia;
        this.complementos=complementos;
        this.tags=tags;
        this.lluvia=lluvia;
    }

    public String getId(){return  id;}

    public void setId(String id){this.id=id;}

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getImportancia() {
        return importancia;
    }

    public void setImportancia(int importancia) {
        this.importancia = importancia;
    }

    public ArrayList<String> getComplementos() {
        return complementos;
    }

    public void setComplementos(ArrayList<String>  complementos) {
        this.duracion = duracion;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String>  tags) {
        this.tags = tags;
    }

    public Boolean getLluvia() {
        return lluvia;
    }

    public void setLluvia(Boolean lluvia) {
        this.duracion = duracion;
    }


    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("Titulo", this.titulo);
        result.put("Fecha", this.fecha);
        result.put("Duracion", this.duracion);
        result.put("Hora", this.hora);
        result.put("Importancia", this.importancia);
        result.put("Complementos", this.complementos);
        result.put("Tags", this.tags);
        result.put("Lluvia", this.lluvia);

        return result;
    }
}