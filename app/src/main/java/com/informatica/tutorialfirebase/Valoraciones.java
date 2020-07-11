package com.informatica.tutorialfirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Valoraciones implements Serializable {
    private String idEvento;
    private int estrellas;
    private Boolean fecha;
    private Boolean hora;
    private Boolean duracion;
    private Boolean complementos;
    private Boolean tags;

    public Valoraciones(){}


    public Valoraciones(String idEvento, int estrellas, Boolean fecha, Boolean hora, Boolean duracion, Boolean complementos, Boolean tags) {
        this.idEvento=idEvento;
        this.estrellas=estrellas;
        this.fecha=fecha;
        this.duracion=duracion;
        this.hora=hora;
        this.complementos=complementos;
        this.tags=tags;
    }

    public String getId(){return  idEvento;}

    public void setId(String id){this.idEvento=id;}

    public int getEstrellas(){return  estrellas;}

    public void setEstrellas(int estrellas){this.estrellas=estrellas;}

    public Boolean getFecha() {
        return fecha;
    }

    public void setFecha(Boolean fecha) {
        this.fecha = fecha;
    }

    public Boolean getDuracion() {
        return duracion;
    }

    public void setDuracion(Boolean duracion) {
        this.duracion = duracion;
    }

    public Boolean getHora() {
        return hora;
    }

    public void setHora(Boolean hora) {
        this.hora = hora;
    }

    public Boolean getComplementos() {
        return complementos;
    }

    public void setComplementos(Boolean complementos) {
        this.complementos = complementos;
    }

    public Boolean getTags() {
        return tags;
    }

    public void setTags(Boolean  tags) {
        this.tags = tags;
    }


    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("IdEvento", this.idEvento);
        result.put("Estrellas", this.estrellas);
        result.put("Fecha", this.fecha);
        result.put("Duracion", this.duracion);
        result.put("Hora", this.hora);
        result.put("Complementos", this.complementos);
        result.put("Tags", this.tags);

        return result;
    }
}