package com.informatica.tutorialfirebase;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.lista_eventos)
    RecyclerView ListaEventos;
    RecyclerView ListaUsuarios;
    private ArrayList<Recurso> ListaRecursos;
    private ArrayList<Tag> ListaTags;
    private ArrayList<String> complementosSeleccionados;
    private ArrayList<Evento> ListaEventosFrag;
    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private EventoAdapter mAdapter;
    private UsuarioAdapter uAdapter;
    Bundle paqueteRecibido;
    Usuario User;
    int Cant = 0;
    private ArrayList<Evento> _listaEventos;
    int indicadorActivity = 1;
    String frag = "";

    private fragMostrarEventosDef miFragDeResultadoDef;
    private fragMostrarEventosIndef miFragDeResultadoIndef;
    private fragMostrarEventosAValorar miFragDeResultado;

    FragmentManager AdminFragments;
    FragmentTransaction TransaccionesDeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdminFragments=getFragmentManager();

        MostrarCalendario();

        ListaEventosFrag = new ArrayList<Evento>();

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar1);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Clip");
        }

        NavigationView nav_View = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar1,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        toggle.syncState();
        nav_View.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), menuItem.getTitle() + " seleccionado", Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                if(menuItem.getTitle().equals("Lista definida"))
                {
                    SeIngresoElDatoDefinido(ListaEventosFrag);
                }
                else if(menuItem.getTitle().equals("Lista indefinida"))
                {
                    SeIngresoElDatoIndefinido(ListaEventosFrag);
                }
                else if(menuItem.getTitle().equals("Calendario"))
                {
                    MostrarCalendario();
                }
                else if(menuItem.getTitle().equals("Valoraciones"))
                {
                    SeIngresoElDatoValoraciones(ListaEventosFrag);
                }
                return true;
            }
        });

        User = new Usuario();
        ListaRecursos = new ArrayList<Recurso>();
        ListaTags = new ArrayList<com.informatica.tutorialfirebase.Tag>();

        paqueteRecibido = getIntent().getExtras();
        User.setId(paqueteRecibido.getString("IdUsuario"));
        User.setNombre(paqueteRecibido.getString("Nombre"));
        Long CalId = paqueteRecibido.getLong("CalId");
        String Mail = paqueteRecibido.getString("Mail");

        //ComprobarUsuarios();


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirigo hacia la pantalla para agregar un nuevo evento
                Cant = 2;
                Bundle bundle = new Bundle();
                bundle.putString("IdUsuario", User.getId());
                bundle.putInt("Cant", Cant);
                bundle.putSerializable("ListaRecursos", ListaRecursos);
                bundle.putSerializable("ListaTags", ListaTags);
                bundle.putLong("CalId", CalId);
                bundle.putString("Mail", Mail);
                indicadorActivity = 1;
                bundle.putSerializable("IndicadorActivity", indicadorActivity);
                Intent intent = new Intent(MainActivity.this, AgregarEditar.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //ListaEventos.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        obtenerListaEventos();
        obtenerListaRecursos();
        obtenerListaTags();
    }

    public ArrayList<Evento> obtenerListaDeEventos() {
        return  _listaEventos;
    }

    public ArrayList<Recurso> obtenerListaDeRecursos() {
        return ListaRecursos;
    }

    public ArrayList<Tag> obtenerListaDeTags() {
        return ListaTags;
    }

    public String obtenerIdUsuario() {
        return User.getId();
    }

    public void SeIngresoElDatoDefinido(ArrayList<Evento> listaEventos) {
        Log.d("Frag", "Se ingresó dato en el Fragment");

        _listaEventos = listaEventos;

        MostrarResultadoDefinido();
    }

    public void SeIngresoElDatoIndefinido(ArrayList<Evento> listaEventos) {
        Log.d("Frag", "Se ingresó dato en el Fragment");

        _listaEventos = listaEventos;

        MostrarResultadoIndefinido();
    }

    public void SeIngresoElDatoValoraciones(ArrayList<Evento> listaEventos) {
        Log.d("Frag", "Se ingresó dato en el Fragment");

        _listaEventos = listaEventos;

        MostrarResultadoValoraciones();
    }

    private void MostrarResultadoDefinido() {
        miFragDeResultadoDef=new fragMostrarEventosDef();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultadoDef);
        TransaccionesDeFragment.commit();

        frag = "def";
    }

    private void MostrarResultadoIndefinido() {
        miFragDeResultadoIndef=new fragMostrarEventosIndef();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultadoIndef);
        TransaccionesDeFragment.commit();

        frag = "indef";
    }

    private void MostrarResultadoValoraciones() {
        miFragDeResultado=new fragMostrarEventosAValorar();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();

    }

    private void MostrarCalendario() {
        fragMostrarCalendario miFragDeResultado=new fragMostrarCalendario();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();

    }

    private void obtenerListaEventos() {
        db.collection("usuarios").document(User.getId()).collection("Eventos").addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                List<Evento> eventos = new ArrayList<>();
                ListaEventosFrag.clear();

                if (e!=null){
                }
                else {
                    for (DocumentSnapshot document : snapshots) {
                        Evento even = document.toObject(Evento.class);
                        even.setId(document.getId());
                        even.setTitulo(document.getString("Titulo"));
                        even.setFecha(document.getString("Fecha"));
                        even.setHora(document.getString("Hora"));
                        even.setLluvia(document.getBoolean("Lluvia"));
                        even.setIndefinido(document.getBoolean("Indefinido"));
                        even.setCompletado(document.getBoolean("Completado"));
                        even.setValorado(document.getBoolean("Valorado"));
                        even.setImportancia(((Long) document.getLong("Importancia")).intValue());
                        even.setDuracion(((Long) document.getLong("Duracion")).intValue());
                        even.setComplementos((ArrayList<String>) document.get("Complementos"));
                        even.setTags((ArrayList<String>) document.get("Tags"));
                        even.setIdCalendar(document.getLong("IdCalendar"));
                        eventos.add(even);

                        ListaEventosFrag.add(even);
                    }
                }
                mAdapter = new EventoAdapter(eventos, getApplicationContext(), db);
                //ListaEventos.setAdapter(mAdapter);
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
                    ListaRecursos.clear();
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
                    ListaTags.clear();
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

    private void ComprobarUsuarios() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.d("ActivityResult", "Llego " + requestCode + " " + resultCode);

            if(frag.equals("def"))
            {
                if(resultCode == Activity.RESULT_OK){
                    miFragDeResultadoDef.Refrescar();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                    miFragDeResultadoDef.Refrescar();
                }
            }
            else
            {
                if(resultCode == Activity.RESULT_OK){
                    miFragDeResultadoIndef.Refrescar();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                    miFragDeResultadoIndef.Refrescar();
                }
            }

        }
        else if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK){
                miFragDeResultado.Refrescar();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                miFragDeResultado.Refrescar();
            }
        }

    }//onActivityResult

}