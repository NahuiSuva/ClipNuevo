package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragMostrarRecursos extends Fragment implements View.OnClickListener{
    private FirebaseFirestore db;
    RecyclerView listaRecursosMostrar;
    ArrayList<Recurso> listaRecursos;
    String IdUsuario;
    Button btnAgregarComplemento;
    EditText txtNuevoComplemento;
    RecursoAdapter miAdaptadorDeRecursos;
    MainActivity miActividad;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;
        db=FirebaseFirestore.getInstance();
        VistaADevolver=inflador.inflate(R.layout.layoutrecursos, container, false);
        listaRecursos=new ArrayList<Recurso>();
        listaRecursosMostrar=VistaADevolver.findViewById(R.id.lista_recursos);
        miActividad = (MainActivity) getActivity();
        listaRecursos=miActividad.obtenerListaDeRecursos();
        IdUsuario=miActividad.obtenerIdUsuario();
        btnAgregarComplemento = VistaADevolver.findViewById(R.id.btnAgregarRecurso);
        btnAgregarComplemento.setOnClickListener(this);
        txtNuevoComplemento = VistaADevolver.findViewById(R.id.ingresoRecurso);

        listaRecursosMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));
        //RecursoAdapter miAdaptadorDeRecursos;
        miAdaptadorDeRecursos = new RecursoAdapter(listaRecursos, miActividad.getApplicationContext(), db, IdUsuario);
        listaRecursosMostrar.setAdapter(miAdaptadorDeRecursos);
        return VistaADevolver;
    }

    public void onClick(View VistRecibida) {
        Recurso miRecurso = new Recurso(txtNuevoComplemento.getText().toString());
        String Nombre = txtNuevoComplemento.getText().toString();
        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Complementos")
                .add(miRecurso)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Agregar complemento","Complemento agregado exitosamente");
                        miAdaptadorDeRecursos.AgregarElemento(Nombre, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Agregar complemento","Error al agregar complemento");
                    }
                });
    }
}