package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class fragMostrarCalendario extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    RecyclerView listaEventosMostrar;
    ArrayList<Evento> listaTraida;
    ArrayList<Evento> listaAMostrar;
    ArrayList<Recurso> listaAMostrarRecursos;
    ArrayList<Tag> listaAMostrarTags;
    String IdUsuario;
    EventoAdapter miAdaptadorDeEventos;
    String fechaString;
    MainActivity miActividadPrincipal;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;

        miActividadPrincipal = (MainActivity) getActivity();

        fab = miActividadPrincipal.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        VistaADevolver=inflador.inflate(R.layout.layoutcalendario, container, false);
        CalendarView calendarView=(CalendarView) VistaADevolver.findViewById(R.id.Calendario);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int anio, int mes,
                                            int dia) {
                //Toast.makeText(getContext(), ""+dia, 0).show();// TODO Auto-generated method stub

                listaEventosMostrar=VistaADevolver.findViewById(R.id.lista_eventos);
                db = FirebaseFirestore.getInstance();

                fechaString = dia + "/" + (mes+1) + "/" + anio;

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
                    if(listaTraida.get(i).getIndefinido() == false)
                    {
                        if(listaTraida.get(i).getCompletado() == false) {
                            if (listaTraida.get(i).getFecha().equals(fechaString)) {
                                listaAMostrar.add(listaTraida.get(i));
                            }
                        }
                    }
                }

                listaEventosMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));

                miAdaptadorDeEventos = new EventoAdapter(listaAMostrar, miActividad, db, listaAMostrarRecursos, listaAMostrarTags, IdUsuario);

                listaEventosMostrar.setAdapter(miAdaptadorDeEventos);
            }
        });

        return VistaADevolver;
    }

    public void onClick(View VistRecibida) {

    }

    public void Refrescar()
    {
        listaAMostrar.clear();

        MainActivity miActividad=(MainActivity) getActivity();
        listaTraida = miActividad.obtenerListaDeEventos();

        miAdaptadorDeEventos.notifyDataSetChanged();
    }


}
