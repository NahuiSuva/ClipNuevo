package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.escape.Escaper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragMostrarEventosIndef extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    RecyclerView listaEventosMostrar;
    ArrayList<Evento> listaTraida;
    ArrayList<Evento> listaAMostrar;
    ArrayList<Recurso> listaAMostrarRecursos;
    ArrayList<Tag> listaAMostrarTags;
    String IdUsuario;
    EventoAdapter miAdaptadorDeEventos;
    MainActivity miActividadPrincipal;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;

        db = FirebaseFirestore.getInstance();

        VistaADevolver=inflador.inflate(R.layout.layouteventosindefinidos, container, false);

        miActividadPrincipal = (MainActivity) getActivity();

        fab = miActividadPrincipal.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        listaEventosMostrar=VistaADevolver.findViewById(R.id.lista_eventos);

        listaAMostrar = new ArrayList<Evento>();
        listaTraida = new ArrayList<Evento>();
        listaAMostrarRecursos = new ArrayList<Recurso>();
        listaAMostrarTags = new ArrayList<Tag>();


        MainActivity miActividad=(MainActivity) getActivity();
        listaTraida = miActividad.obtenerListaDeEventos();
        listaAMostrarRecursos = miActividad.obtenerListaDeRecursos();
        listaAMostrarTags = miActividad.obtenerListaDeTags();
        IdUsuario = miActividad.obtenerIdUsuario();


        for (int i=0; i<listaTraida.size();i++)
        {
            if(listaTraida.get(i).getCompletado() == false) {

                if (listaTraida.get(i).getIndefinido() == true) {

                    if (listaTraida.get(i).getValorado() == false) {
                        listaAMostrar.add(listaTraida.get(i));
                    }
                }
            }
        }

        listaEventosMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));

        miAdaptadorDeEventos = new EventoAdapter(listaAMostrar, miActividad, db, listaAMostrarRecursos, listaAMostrarTags, IdUsuario);

        listaEventosMostrar.setAdapter(miAdaptadorDeEventos);

        return VistaADevolver;
    }

    public void onClick(View VistRecibida) {

    }

    public void Refrescar()
    {
        listaAMostrar.clear();

        MainActivity miActividad=(MainActivity) getActivity();
        listaTraida = miActividad.obtenerListaDeEventos();

        for (int i=0; i<listaTraida.size();i++)
        {
            if(listaTraida.get(i).getCompletado() == false) {

                if (listaTraida.get(i).getIndefinido() == true) {

                    if (listaTraida.get(i).getValorado() == false) {
                        listaAMostrar.add(listaTraida.get(i));
                    }
                }
            }
        }

        miAdaptadorDeEventos.notifyDataSetChanged();
    }
}
