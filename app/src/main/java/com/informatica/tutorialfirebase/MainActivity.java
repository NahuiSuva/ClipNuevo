package com.informatica.tutorialfirebase;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
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
    String variableLocalHost = "58229"; //Variable del local host

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
                else if(menuItem.getTitle().equals("Complementos")){
                    MostrarResultadoRecursos();
                }
                else if(menuItem.getTitle().equals("Tags")){
                    MostrarResultadoTags();
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
        //obtenerEventosViaApi miTarea = new obtenerEventosViaApi();
        //miTarea.execute();
        obtenerListaRecursos();
        obtenerListaTags();
    }

    public ArrayList<Evento> obtenerListaDeEventos() {
        return  _listaEventos;
    }

    public ArrayList<Recurso> obtenerListaDeRecursos() {
        obtenerListaRecursos();
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
        fragMostrarEventosDef miFragDeResultado=new fragMostrarEventosDef();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();

    }

    private void MostrarResultadoIndefinido() {
        fragMostrarEventosIndef miFragDeResultado=new fragMostrarEventosIndef();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();

    }

    private void MostrarResultadoValoraciones() {
        fragMostrarEventosAValorar miFragDeResultado=new fragMostrarEventosAValorar();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();

    }

    private void MostrarResultadoRecursos(){
        fragMostrarRecursos miFragDeResultado=new fragMostrarRecursos();

        TransaccionesDeFragment=AdminFragments.beginTransaction();
        TransaccionesDeFragment.replace(R.id.FrameParaFragmentMostrar, miFragDeResultado);
        TransaccionesDeFragment.commit();
    }

    private void MostrarResultadoTags(){
        fragMostrarTags miFragDeResultado=new fragMostrarTags();

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

    private class obtenerEventosViaApi extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected  String doInBackground(String... params){
            HttpURLConnection miConexion = null;
            URL strApiUrl;
            float ResponseCode;
            String strResultado = "";
            try{
                strApiUrl = new URL("http://10.0.2.2:"+variableLocalHost+"/eventos/"+User.getId().toString());
                Log.d("Conexion api", "La url es: "+strApiUrl);
                miConexion = (HttpURLConnection) strApiUrl.openConnection();
                miConexion.setRequestMethod("GET");
                ResponseCode = miConexion.getResponseCode();
                Log.d("Conexion api","Codigo respuesta: "+ResponseCode);
                if(ResponseCode==200){
                    List<Evento> eventos = new ArrayList<>();
                    ListaEventosFrag.clear();

                    InputStream lector = miConexion.getInputStream();
                    InputStreamReader lectorJSon=new InputStreamReader(lector, "utf-8");
                    JsonParser ProcesadorDeJSon=new JsonParser();
                    JsonArray arrayRespuesta=ProcesadorDeJSon.parse(lectorJSon).getAsJsonArray();

                    for (int i=0; i<arrayRespuesta.size(); i++)
                    {
                        JsonObject unEvento = arrayRespuesta.get(i).getAsJsonObject();
                        Evento even = new Evento();
                        even.setId(unEvento.get("id").toString());
                        Log.d("Conexion api", "id "+i+":");
                        even.setTitulo(unEvento.get("Titulo").toString());
                        even.setFecha(unEvento.get("Fecha").toString());
                        even.setHora(unEvento.get("Hora").toString());
                        even.setLluvia(unEvento.get("Lluvia").getAsBoolean());
                        even.setIndefinido(unEvento.get("Indefinido").getAsBoolean());
                        even.setCompletado(unEvento.get("Completado").getAsBoolean());
                        even.setValorado(unEvento.get("Valorado").getAsBoolean());
                        even.setImportancia(unEvento.get("Importancia").getAsInt());
                        even.setDuracion(unEvento.get("Duracion").getAsInt());
                        ArrayList<String> complementosAux = new ArrayList<String>();
                        JsonArray jArray = (JsonArray) unEvento.get("Complementos");
                        if (jArray != null) {
                            for (int e=0;e<jArray.size();e++){
                                complementosAux.add(jArray.get(e).toString());
                            }
                        }
                        even.setComplementos(complementosAux);
                        ArrayList<String> tagsAux = new ArrayList<String>();
                        JsonArray tagsArray = (JsonArray) unEvento.get("Tags");
                        if (tagsArray != null) {
                            for (int e=0;e<tagsArray.size();e++){
                                tagsAux.add(tagsArray.get(e).toString());
                            }
                        }
                        even.setTags(tagsAux);
                        even.setIdCalendar(unEvento.get("IdCalendar").getAsLong());
                        eventos.add(even);

                        ListaEventosFrag.add(even);

                    }
                    mAdapter = new EventoAdapter(eventos, getApplicationContext(), db);

                }
            }catch(Exception error){
                Log.d("Conexion api", "Problema al conectar: "+ error.getMessage());
            } finally {
                if(miConexion!=null)
                {
                    miConexion.disconnect();
                }
            }
            return strResultado;
        }

        @Override
        protected void onPostExecute(String resultado){
            super.onPostExecute(resultado);
            Log.d("Conexion api", "Fin del traer eventos");
        }
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
                        miRecurso.setNombre(document.getString("nombre"));
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
                        unTag.setNombre(document.getString("nombre"));
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

}