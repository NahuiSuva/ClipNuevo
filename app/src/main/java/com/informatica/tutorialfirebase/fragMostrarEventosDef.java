package com.informatica.tutorialfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragMostrarEventosDef extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    RecyclerView listaEventosMostrar;
    ArrayList<Evento> listaTraida;
    ArrayList<Evento> listaAMostrar;
    ArrayList<Recurso> listaAMostrarRecursos;
    ArrayList<Tag> listaAMostrarTags;
    String IdUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;

        db = FirebaseFirestore.getInstance();

        VistaADevolver=inflador.inflate(R.layout.layouteventosdefinidos, container, false);


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

                if (listaTraida.get(i).getIndefinido() == false) {

                    if (listaTraida.get(i).getValorado() == false) {
                        listaAMostrar.add(listaTraida.get(i));
                    }
                }
            }
        }

        listaEventosMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));

        EventoAdapter miAdaptadorDeEventos;
        miAdaptadorDeEventos = new EventoAdapter(listaAMostrar, miActividad.getApplicationContext(), db, listaAMostrarRecursos, listaAMostrarTags, IdUsuario);

        listaEventosMostrar.setAdapter(miAdaptadorDeEventos);

        return VistaADevolver;
    }


    public void onClick(View VistRecibida) {

    }
}
