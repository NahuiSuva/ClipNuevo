package com.informatica.tutorialfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lista_eventos)
    RecyclerView ListaEventos;
    RecyclerView ListaUsuarios;
    private ArrayList<Recurso> ListaRecursos;
    private ArrayList<Tag> ListaTags;
    private ArrayList<String> complementosSeleccionados;
    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private EventoAdapter mAdapter;
    private UsuarioAdapter uAdapter;
    Bundle paqueteRecibido;
    Usuario User;

    //Esto es el push

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User = new Usuario();
        ListaRecursos = new ArrayList<Recurso>();
        ListaTags = new ArrayList<com.informatica.tutorialfirebase.Tag>();

        paqueteRecibido = getIntent().getExtras();
        User.setId(paqueteRecibido.getString("IdUsuario"));
        User.setNombre(paqueteRecibido.getString("Nombre"));

        //ComprobarUsuarios();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirigo hacia la pantalla para agregar un nuevo evento
                Bundle bundle = new Bundle();
                bundle.putString("IdUsuario", User.getId());
                bundle.putInt("Cant", 2);
                bundle.putSerializable("ListaRecursos", ListaRecursos);
                bundle.putSerializable("ListaTags", ListaTags);
                Intent intent = new Intent(MainActivity.this, AgregarEditar.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        ListaEventos.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        //obtenerListaEventos();
        obtenerListaRecursos();
        obtenerListaTags();
    }
    private void obtenerListaEventos() {
        db.collection("usuarios").document("lorPOE2730QHl1Fh5Nhc").collection("Eventos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                List<Evento> eventos = new ArrayList<>();

                if (e!=null){
                }
                else {
                    for (DocumentSnapshot document : snapshots) {
                        Evento even = document.toObject(Evento.class);
                        even.setId(document.getId());
                        eventos.add(even);
                    }
                }
                mAdapter = new EventoAdapter(eventos, getApplicationContext(), db);
                ListaEventos.setAdapter(mAdapter);
            }
        });
    }

    private void obtenerListaRecursos(){
        db.collection("usuarios").document(User.getId()).collection("Complementos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                }
                else {
                    for (DocumentSnapshot document : snapshots) {
                        Recurso unRecurso = document.toObject(Recurso.class);
                        Recurso miRecurso=new Recurso();
                        miRecurso.setId(document.getId());
                        miRecurso.setNombre(document.getString("Nombre"));
                        ListaRecursos.add(miRecurso);
                    }
                }
            }
        });
    }

    private void obtenerListaTags(){
        db.collection("usuarios").document(User.getId()).collection("Tags").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                }
                else {
                    for (DocumentSnapshot document : snapshots) {
                        com.informatica.tutorialfirebase.Tag unTag = document.toObject(com.informatica.tutorialfirebase.Tag.class);
                        unTag.setId(document.getId());
                        unTag.setNombre(document.getString("Nombre"));
                        ListaTags.add(unTag);
                    }
                }
            }
        });
    }

    private void ComprobarUsuarios()
    {
        db.collection("usuarios").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                List<Usuario> usuarios = new ArrayList<>();

                if (e!=null){
                    Map<String, Object> usuario = new Usuario(User.getId(), User.getNombre()).toMap();

                    db.collection("usuarios")
                            .add(usuario)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Usuario agregado correctamente!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("AgregarUsuario", "Error al añadir el usuario", e);
                                    Toast.makeText(getApplicationContext(), "Ocurrio un error y no se pudo agregar el usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    for (DocumentSnapshot document : snapshots) {
                        Usuario usu = document.toObject(Usuario.class);
                        usu.setId(document.getId());
                        usuarios.add(usu);
                    }
                }
                uAdapter = new UsuarioAdapter(usuarios, getApplicationContext(), db);
                ListaUsuarios.setAdapter(uAdapter);
            }
        });
    }

}