package com.informatica.tutorialfirebase;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgregarEditar extends AppCompatActivity {

    private FirebaseFirestore db;
    String id = ""; //Aca voy a guardar el id del documento en caso de que vaya a editar ese elemento
    String titulo;
    String fecha;
    int duracion;
    String hora;
    int importancia;
    ArrayList<String> complementos;
    ArrayList<String> tags;
    Boolean lluvia;
    Boolean indefinido;
    Boolean completado;
    ArrayList<Recurso> complementosSeleccionados;
    ArrayList<Tag> tagsSeleccionados;
    Bundle paqueteRecibidoMain;
    Bundle paqueteRecibidoAdapter;
    String IdUsuario = "";
    ArrayList<Recurso> ListaDeRecursos;
    ArrayList<Tag> ListaDeTags;
    ArrayList<String> ListaRecursosTraidos;
    ArrayList<String> ListaTagsTraidos;

    Button abrirFecha;
    Button abrirHora;
    Button abrirDuracion;

    @BindView(R.id.lblTitulo)
    TextView lblTitulo;
    @BindView(R.id.btnAgregar)
    Button btnAgregar;
    @BindView(R.id.btnEliminar)
    Button btnEliminar;
    @BindView(R.id.txtTitulo)
    EditText edtTitulo;
    @BindView(R.id.txtFecha)
    DatePicker dpFecha;
    @BindView(R.id.txtDuracion)
    TimePicker tpDuracion;
    @BindView(R.id.txtHora)
    TimePicker tpHora;
    @BindView(R.id.rdImportancia)
    RadioGroup rdImportancia;
    @BindView(R.id.radio_1)
    RadioButton rd1;
    @BindView(R.id.radio_2)
    RadioButton rd2;
    @BindView(R.id.radio_3)
    RadioButton rd3;
    @BindView(R.id.swLluvia)
    Switch swLluvia;
    @BindView(R.id.swIndefinido)
    Switch swIndefinido;

    int cont = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_editar);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        //Obtengo la instancia de la base de datos
        db = FirebaseFirestore.getInstance();

        abrirFecha = findViewById(R.id.btnFecha);
        abrirHora = findViewById(R.id.btnHora);
        abrirDuracion = findViewById(R.id.btnDuracion);

        swIndefinido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swIndefinido.isChecked()){
                    abrirHora.setEnabled(false);
                    abrirHora.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                    abrirFecha.setEnabled(false);
                    abrirFecha.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                    tpHora.setEnabled(false);
                    dpFecha.setEnabled(false);
                }else {
                    abrirHora.setEnabled(true);
                    abrirHora.setTextColor(getResources().getColor(R.color.colorBlack));

                    abrirFecha.setEnabled(true);
                    abrirFecha.setTextColor(getResources().getColor(R.color.colorBlack));

                    tpHora.setEnabled(true);
                    dpFecha.setEnabled(true);
                }
            }
        });

        tpDuracion.setIs24HourView(true);
        tpDuracion.setHour(1);
        tpDuracion.setMinute(0);

        complementos=new ArrayList<String>();
        tags=new ArrayList<String>();
        complementosSeleccionados=new ArrayList<Recurso>();
        complementosSeleccionados.clear();
        Recurso miRecurso=new Recurso();
        miRecurso.setNombre("nada");
        complementosSeleccionados.add(miRecurso);
        tagsSeleccionados=new ArrayList<Tag>();
        tagsSeleccionados.clear();
        Tag miTag = new Tag();
        miTag.setNombre("nada");
        tagsSeleccionados.add(miTag);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getInt("IndicadorActivity") == 1) {
            paqueteRecibidoMain = getIntent().getExtras();
            IdUsuario = paqueteRecibidoMain.getString("IdUsuario");

            ListaDeRecursos=new ArrayList<Recurso>();
            ListaDeRecursos= (ArrayList<Recurso>) paqueteRecibidoMain.getSerializable("ListaRecursos");

            ListaDeTags=new ArrayList<Tag>();
            ListaDeTags= (ArrayList<Tag>) paqueteRecibidoMain.getSerializable("ListaTags");
        }
        else if(bundle.getInt("IndicadorActivity") == 2)
        {
            paqueteRecibidoAdapter = getIntent().getExtras();

            ListaDeRecursos=new ArrayList<Recurso>();
            ListaDeRecursos= (ArrayList<Recurso>) paqueteRecibidoAdapter.getSerializable("ListaRecursos");

            ListaDeTags=new ArrayList<Tag>();
            ListaDeTags= (ArrayList<Tag>) paqueteRecibidoAdapter.getSerializable("ListaTags");

            paqueteRecibidoMain = getIntent().getExtras();
            IdUsuario = paqueteRecibidoAdapter.getString("IdUsuario");
        }



        //Compruebo si llegaron datos a esta activity.
        // Si llegaron voy a mostrar la pantalla de Editar. Si no llegaron muestro la de crear

        if (bundle.getInt("Cant") != 2) {
            lblTitulo.setText("Modificar Evento");
            btnAgregar.setText("EDITAR");

            id = bundle.getString("id");
            edtTitulo.setText(bundle.getString("titulo"));
            int Inicio = bundle.getString("fecha").indexOf("/");
            int Fin = bundle.getString("fecha").indexOf("/", Inicio + 1);
            int dia = Integer.parseInt(bundle.getString("fecha").substring(0, Inicio));
            int mes = Integer.parseInt(bundle.getString("fecha").substring(Inicio+1, Fin));
            int anio = Integer.parseInt(bundle.getString("fecha").substring(Fin+1, Fin+5));
            dpFecha.updateDate(anio, mes, dia);
            tpDuracion.setMinute(bundle.getInt("minutos"));
            tpDuracion.setHour(bundle.getInt("horas"));
            int InicioHora = bundle.getString("hora").indexOf(":");
            tpHora.setHour(Integer.parseInt(bundle.getString("hora").substring(0, InicioHora)));
            tpHora.setMinute(Integer.parseInt(bundle.getString("hora").substring(InicioHora+1)));
            double duracion = (bundle.getInt("duracion")/60);
            int InicioDuracion = String.valueOf(duracion).indexOf(".");
            tpDuracion.setHour(Integer.parseInt(String.valueOf(duracion).substring(0, InicioDuracion)));
            tpDuracion.setMinute(bundle.getInt("duracion")-((Integer.parseInt(String.valueOf(duracion).substring(0, InicioDuracion)))*60));
            if(bundle.getInt("importancia") == 1) {
                rd1.setChecked(true);
            }
            else if(bundle.getInt("importancia") == 2) {
                rd2.setChecked(true);
            }
            else{
                rd3.setChecked(true);
            }

            //edtComplementos.setText(bundle.getString("complementos"));
            //edtTags.setText(bundle.getString("tags"));
            swLluvia.setChecked(bundle.getBoolean("lluvia"));
            swIndefinido.setChecked(bundle.getBoolean("indefinido"));

            btnEliminar.setVisibility(View.VISIBLE);
        } else {
            lblTitulo.setText("Agregar Evento");
            btnAgregar.setText("AGREGAR");
            btnEliminar.setVisibility(View.GONE);
        }

            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                        cont = 0;
                        int cantAdv = 0;

                        if (edtTitulo.length() == 0) {
                            if (cantAdv == 0) {
                                Toast.makeText(getApplicationContext(), "Ingrese todos los campos de manera correcta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            titulo = edtTitulo.getText().toString();
                            cont++;
                        }

                        int year = dpFecha.getYear();
                        int month = dpFecha.getMonth();
                        int day = dpFecha.getDayOfMonth();
                        fecha = day + "/" + (month + 1) + "/" + year;

                        int minutoDuracion = tpDuracion.getMinute();
                        int horaDuracion =  tpDuracion.getHour();
                        duracion = (horaDuracion * 60) + minutoDuracion;
                        int minuto = tpHora.getMinute();
                        int hora1 = tpHora.getHour();
                        hora = hora1 + ":" + minuto;

                        if (rd1.isChecked() == false && rd2.isChecked() == false && rd3.isChecked() == false) {
                            if (cantAdv == 0) {
                                Toast.makeText(getApplicationContext(), "Ingrese todos los campos de manera correcta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (rd1.isChecked()) {
                                importancia = 1;
                            } else if (rd2.isChecked()) {
                                importancia = 2;
                            } else {
                                importancia = 3;
                            }
                            cont++;
                        }

                        if(complementosSeleccionados.size()==0){
                        complementos = null;
                        }
                        else{
                         for (int i=0; i<complementosSeleccionados.size();i++)
                         {
                             complementos.add(complementosSeleccionados.get(i).getNombre());
                         }
                        }
                        if(tagsSeleccionados.size()==0){
                            tags = null;
                        }
                        else{
                            for (int i=0; i<tagsSeleccionados.size();i++)
                            {
                                tags.add(tagsSeleccionados.get(i).getNombre());
                            }
                        }

                        lluvia = swLluvia.isChecked();
                        indefinido = swIndefinido.isChecked();
                        completado = false;
                        if (cont == 2) {
                            //Si no tengo un ID quiere decir que es un registro nuevo. Si tengo un ID debo actualizar el existente
                            if (id.length() > 0) {
                                ActualizarEvento(id, titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado);
                            } else {
                                AgregarEvento(titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado);

                            }
                            complementosSeleccionados.clear();
                            tagsSeleccionados.clear();

                            finish();
                        }
                }


            });
    }


    public void mostrarFecha(View vista){
    if(dpFecha.getVisibility()==View.VISIBLE){
        dpFecha.setVisibility(View.GONE);
        abrirFecha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fecha_24dp, 0, R.drawable.ic_expand_more_24dp, 0);
    }else{
        dpFecha.setVisibility(View.VISIBLE);
        abrirFecha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fecha_24dp, 0, R.drawable.ic_expand_less_24dp, 0);
    }
    }

    public void mostrarDuracion(View vista){
        if(tpDuracion.getVisibility()==View.VISIBLE){
            tpDuracion.setVisibility(View.GONE);
            abrirDuracion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_duracion_24dp, 0, R.drawable.ic_expand_more_24dp, 0);
        }else{
            tpDuracion.setVisibility(View.VISIBLE);
            abrirDuracion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_duracion_24dp, 0, R.drawable.ic_expand_less_24dp, 0);
        }
    }

    public void mostrarHora(View vista){
        if(tpHora.getVisibility()==View.VISIBLE){
            tpHora.setVisibility(View.GONE);
            abrirHora.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hora_24dp, 0, R.drawable.ic_expand_more_24dp, 0);
        }else{
            tpHora.setVisibility(View.VISIBLE);
            abrirHora.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hora_24dp, 0, R.drawable.ic_expand_less_24dp, 0);
        }
    }

    public void btnEliminar(View v) {
    AlertDialog confirmacion = new AlertDialog.Builder(AgregarEditar.this)
        .setTitle("Eliminar Evento")
        .setMessage("¿Estás seguro que deseas borrar el evento?")
        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                EliminarEvento(id);
                finish();
            }

        })
        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .show();

    }

    private void ActualizarEvento(String id, String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado) {
        Map<String, Object> evento = (new Evento(id, titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado)).toMap();

        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Eventos")
                .document(id)
                .set(evento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Evento actualizado correctamente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AgregarEvent", "Error al actualizar el evento", e);
                        Toast.makeText(getApplicationContext(), "Ocurrió un error al actualizar la información", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void AgregarEvento(String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado) {
        Map<String, Object> evento = new Evento(titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado).toMap();

        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Eventos")
                .add(evento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Evento agregado correctamente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AgregarEvent", "Error al añadir el evento", e);
                        Toast.makeText(getApplicationContext(), "Ocurrio un error y no se pudo agregar el evento", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void EliminarEvento(String id) {
        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Eventos")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Evento borrado exitosamente!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void mostrarComplementos(View vista) {
        complementosSeleccionados.clear();
        DialogInterface.OnClickListener escuchador = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int cualBotonTocado) {

            }
        };

        DialogInterface.OnMultiChoiceClickListener escuchadorOpciones = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b)
                {
                    complementosSeleccionados.add(ListaDeRecursos.get(i));
                }
            }
        };


        AlertDialog.Builder mensaje;
        mensaje = new AlertDialog.Builder(this);
        mensaje.setTitle("Complementos");
        String opciones[] = new String[ListaDeRecursos.size()];
        for(int i = 0; i < ListaDeRecursos.size(); i++)
        {
            opciones[i] = ListaDeRecursos.get(i).getNombre();
        }
        boolean[] opcionesPreSeleccionadas = new boolean[ListaDeRecursos.size()];
        for(int i = 0; i < ListaDeRecursos.size(); i++)
        {
            opcionesPreSeleccionadas[i] = false;
        }

        if(getIntent().getExtras().getInt("IndicadorActivity") == 2) {
            ListaRecursosTraidos = new ArrayList<String>();
            ListaRecursosTraidos = (ArrayList<String>) paqueteRecibidoAdapter.getSerializable("complementos");
            for (int i = 0; i < ListaRecursosTraidos.size(); i++) {
                for (int z = 0; z < ListaDeRecursos.size(); z++) {
                    String NombreRecurso = ListaDeRecursos.get(z).getNombre();
                    if (NombreRecurso.equals(ListaRecursosTraidos.get(i))) {
                        opcionesPreSeleccionadas[z] = true;
                    }

                }
            }
        }

            mensaje.setMultiChoiceItems(opciones, opcionesPreSeleccionadas, escuchadorOpciones);
            mensaje.setPositiveButton("Aceptar", escuchador);
            mensaje.setIcon(R.drawable.ic_complemento_24dp);
            mensaje.create();
            mensaje.show();

    }

    public void mostrarTags(View vista) {
        tagsSeleccionados.clear();
        DialogInterface.OnClickListener escuchador = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int cualBotonTocado) {

            }
        };

        DialogInterface.OnMultiChoiceClickListener escuchadorOpciones = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b)
                {
                    tagsSeleccionados.add(ListaDeTags.get(i));
                }
            }
        };

        AlertDialog.Builder mensaje;
        mensaje = new AlertDialog.Builder(this);
        mensaje.setTitle("Tags");
        String opciones[] = new String[ListaDeTags.size()];
        for(int i = 0; i < ListaDeTags.size(); i++)
        {
            opciones[i] = ListaDeTags.get(i).getNombre();
        }
        boolean[] opcionesPreSeleccionadas = new boolean[ListaDeTags.size()];
        for(int i = 0; i < ListaDeTags.size(); i++)
        {
            opcionesPreSeleccionadas[i] = false;
        }

        if(getIntent().getExtras().getInt("IndicadorActivity") == 2) {
            ListaTagsTraidos = new ArrayList<String>();
            ListaTagsTraidos = (ArrayList<String>) paqueteRecibidoAdapter.getSerializable("tags");
            for (int i = 0; i < ListaTagsTraidos.size(); i++) {
                for (int z = 0; z < ListaDeRecursos.size(); z++) {
                    String NombreRecurso = ListaDeRecursos.get(z).getNombre();
                    if (NombreRecurso.equals(ListaTagsTraidos.get(i))) {
                        opcionesPreSeleccionadas[z] = true;
                    }

                }
            }
        }

        mensaje.setMultiChoiceItems(opciones, opcionesPreSeleccionadas, escuchadorOpciones);
        mensaje.setPositiveButton("Aceptar", escuchador);
        mensaje.setIcon(R.drawable.ic_tag_24dp);
        mensaje.create();
        mensaje.show();
    }

}

