package com.informatica.tutorialfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class ValoracionActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    Switch swFecha;
    Switch swHora;
    Switch swDuracion;
    Switch swComplementos;
    Switch swTags;

    String idEvento = "";
    int estrellas = 0;
    Boolean fecha = false;
    Boolean hora = false;
    Boolean duracion = false;
    Boolean complementos = false;
    Boolean tags = false;
    String IdUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion);
        db = FirebaseFirestore.getInstance();

        IdUsuario = getIntent().getExtras().getString("IdUsuario");

        final SmileRating rating = findViewById(R.id.smile_rating);
        swFecha = findViewById(R.id.swFechaV);
        swHora = findViewById(R.id.swHoraV);
        swDuracion = findViewById(R.id.swDuracionV);
        swComplementos = findViewById(R.id.swComplementosV);
        swTags = findViewById(R.id.swTagsV);

        rating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {

                // Retrieve the value of the bar dinamically
                // level is from 1 to 5
                // Will return 0 if NONE selected
                estrellas = rating.getRating();

                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
            }
        });
    }

    public void enviarValoracion(View vista){

        idEvento = getIntent().getExtras().getString("id");
        fecha = swFecha.isChecked();
        hora = swHora.isChecked();
        duracion = swDuracion.isChecked();
        complementos = swComplementos.isChecked();
        tags = swTags.isChecked();

        AgregarValoracion(idEvento, estrellas, fecha, hora, duracion, complementos, tags);

        Bundle bundle = getIntent().getExtras();

        ActualizarEvento(idEvento, bundle.getString("titulo"), bundle.getString("fecha"), bundle.getInt("duracion"), bundle.getString("hora"), bundle.getInt("importancia"), bundle.getStringArrayList("complementos"), bundle.getStringArrayList("tags"), bundle.getBoolean("lluvia"), bundle.getBoolean("indefinido"), bundle.getBoolean("completado"), true, bundle.getLong("IdCalendar"));

        finish();
    }

    private void AgregarValoracion(String idEvento, int estrellas, Boolean fecha, Boolean hora, Boolean duracion, Boolean complementos, Boolean tags) {
        Map<String, Object> valoracion = new Valoraciones(idEvento, estrellas, fecha, duracion, hora, complementos, tags).toMap();

        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Valoraciones")
                .add(valoracion)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Valoracion agregada correctamente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AgregarValoracion", "Error al añadir la valoracion", e);
                        Toast.makeText(getApplicationContext(), "Ocurrio un error y no se pudo agregar la valoracion", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ActualizarEvento(String id, String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado, Boolean valorado, long idCalendar) {
        Map<String, Object> evento = (new Evento(id, titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado, valorado, idCalendar)).toMap();

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

}
