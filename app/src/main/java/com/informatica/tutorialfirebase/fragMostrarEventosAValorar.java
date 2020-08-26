package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragMostrarEventosAValorar extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    RecyclerView listaEventosMostrar;
    ArrayList<Evento> listaTraida;
    ArrayList<Evento> listaAMostrar;
    String IdUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        View VistaADevolver=inflador.inflate(R.layout.layouteventosavalorar, container, false);


        listaEventosMostrar=VistaADevolver.findViewById(R.id.lista_eventos);

        listaAMostrar = new ArrayList<Evento>();
        listaTraida = new ArrayList<Evento>();


        MainActivity miActividad=(MainActivity) getActivity();
        listaTraida = miActividad.obtenerListaDeEventos();
        IdUsuario = miActividad.obtenerIdUsuario();


        for (int i=0; i<listaTraida.size();i++)
        {
            if(listaTraida.get(i).getCompletado() == true) {

                if (listaTraida.get(i).getValorado() == false) {
                    listaAMostrar.add(listaTraida.get(i));
                }
            }
        }

        listaEventosMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));

        EventoValoracionesAdapter miAdaptadorDeEventos;
        miAdaptadorDeEventos = new EventoValoracionesAdapter(listaAMostrar, miActividad.getApplicationContext(), db, IdUsuario);

        listaEventosMostrar.setAdapter(miAdaptadorDeEventos);

        return VistaADevolver;
    }


    public void onClick(View VistRecibida) {

    }
}
