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

public class fragMostrarCalendario extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    RecyclerView listaEventosMostrar;
    ArrayList<Evento> listaTraida;
    ArrayList<Evento> listaAMostrar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;

        VistaADevolver=inflador.inflate(R.layout.layoutcalendario, container, false);

        return VistaADevolver;
    }


    public void onClick(View VistRecibida) {

    }
}
