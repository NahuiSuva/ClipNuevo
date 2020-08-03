package com.informatica.tutorialfirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Evento implements Serializable {
    private String id;
    private String titulo;
    private String fecha;
    private int duracion;
    private String hora;
    private int importancia;
    private ArrayList<String> complementos;
    private ArrayList<String> tags;
    private Boolean lluvia;
    private Boolean indefinido;
    private Boolean completado;
    private Boolean valorado;
    private long idCalendar;

    // En caso de tener foto, podés utilizar este constructor
    /*public Alumno(String nombre, String division, int calificacion, String foto) {
        this.nombre = nombre;
        this.division = division;
        this.calificacion = calificacion;
        this.foto = foto;
    }*/
    public Evento(){}


    public Evento(String id, String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado, Boolean valorado, long idCalendar) {
        this.id=id;
        this.titulo=titulo;
        this.fecha=fecha;
        this.duracion=duracion;
        this.hora=hora;
        this.importancia=importancia;
        this.complementos=complementos;
        this.tags=tags;
        this.lluvia=lluvia;
        this.indefinido=indefinido;
        this.completado = completado;
        this.valorado = valorado;
        this.idCalendar = idCalendar;
    }
    public Evento(String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado, Boolean valorado, long idCalendar) {
        this.titulo=titulo;
        this.fecha=fecha;
        this.duracion=duracion;
        this.hora=hora;
        this.importancia=importancia;
        this.complementos=complementos;
        this.tags=tags;
        this.lluvia=lluvia;
        this.indefinido=indefinido;
        this.completado = completado;
        this.valorado = valorado;
        this.idCalendar = idCalendar;
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
        this.complementos = complementos;
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
        this.lluvia = lluvia;
    }

    public Boolean getIndefinido() {
        return indefinido;
    }

    public void setIndefinido(Boolean indefinido) {
        this.indefinido = indefinido;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public Boolean getValorado() {
        return valorado;
    }

    public void setValorado(Boolean valorado) {
        this.valorado = valorado;
    }

    public long getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(long idCalendar) {
        this.idCalendar = idCalendar;
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
        result.put("Indefinido", this.indefinido);
        result.put("Completado", this.completado);
        result.put("Valorado", this.valorado);
        result.put("IdCalendar", this.idCalendar);

        return result;
    }
}