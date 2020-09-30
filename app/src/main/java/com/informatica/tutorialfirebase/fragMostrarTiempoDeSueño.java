package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragMostrarTiempoDeSueño extends Fragment implements View.OnClickListener {

    TimePicker tmpDuracion;
    MainActivity miActividadPrincipal;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;

    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;

        VistaADevolver=inflador.inflate(R.layout.layouttiempodesuenio, container, false);

        tmpDuracion = VistaADevolver.findViewById(R.id.tmpDuracion);
        tmpDuracion.setIs24HourView(true);
        tmpDuracion.setHour(0);
        tmpDuracion.setMinute(0);

        miActividadPrincipal = (MainActivity) getActivity();

        fab = miActividadPrincipal.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);


        return VistaADevolver;
    }

    public void onClick(View VistRecibida) {

    }
}
