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

public class fragMostrarTags extends Fragment implements View.OnClickListener{
    private FirebaseFirestore db;
    RecyclerView listaTagsMostrar;
    ArrayList<Tag> listaTags;
    String IdUsuario;
    Button btnAgregarComplemento;
    EditText txtNuevoTag;
    TagAdapter miAdaptadorDeTags;
    MainActivity miActividadPrincipal;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflador, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View VistaADevolver;
        db=FirebaseFirestore.getInstance();
        VistaADevolver=inflador.inflate(R.layout.layouttags, container, false);
        listaTags=new ArrayList<Tag>();
        listaTagsMostrar=VistaADevolver.findViewById(R.id.lista_tags);

        miActividadPrincipal = (MainActivity) getActivity();

        fab = miActividadPrincipal.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        MainActivity miActividad = (MainActivity) getActivity();
        listaTags=miActividad.obtenerListaDeTags();
        IdUsuario=miActividad.obtenerIdUsuario();
        btnAgregarComplemento = VistaADevolver.findViewById(R.id.btnAgregarTag);
        btnAgregarComplemento.setOnClickListener(this);
        txtNuevoTag = VistaADevolver.findViewById(R.id.ingresoTag);

        listaTagsMostrar.setLayoutManager(new LinearLayoutManager(miActividad.getApplicationContext()));
        miAdaptadorDeTags = new TagAdapter(listaTags, miActividad.getApplicationContext(), db, IdUsuario);
        listaTagsMostrar.setAdapter(miAdaptadorDeTags);
        return VistaADevolver;
    }

    public void onClick(View VistRecibida) {
        Tag miTag = new Tag(txtNuevoTag.getText().toString());
        String Nombre = txtNuevoTag.getText().toString();
        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Tags")
                .add(miTag)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Agregar complemento","Complemento agregado exitosamente");
                        miAdaptadorDeTags.AgregarElemento(Nombre, documentReference.getId());
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