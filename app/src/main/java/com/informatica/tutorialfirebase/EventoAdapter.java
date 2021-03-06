package com.informatica.tutorialfirebase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private List<Evento> eventos;
    private Context context;
    private FirebaseFirestore firestoreDB;
    int indicadorActivity = 2;
    ArrayList<Recurso> ListaRecursos;
    ArrayList<Tag> ListaTags;
    String IdUsuario;
    private FirebaseFirestore db;
    private String OpcionElegida;

    public EventoAdapter(List<Evento> eventos, Context context, FirebaseFirestore firestoreDB) {
        this.eventos = eventos;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    public EventoAdapter(List<Evento> eventos, Context context, FirebaseFirestore firestoreDB, ArrayList<Recurso> ListaRecursos, ArrayList<Tag> ListaTags, String IdUsuario) {
        this.eventos = eventos;
        this.context = context;
        this.firestoreDB = firestoreDB;
        this.ListaRecursos = ListaRecursos;
        this.ListaTags = ListaTags;
        this.IdUsuario = IdUsuario;
    }

    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new EventoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventoAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Evento even = eventos.get(itemPosition);
       // progressBar.setVisibility(View.GONE);
        holder.titulo.setText(even.getTitulo());
        Boolean indefinidoComp;
        indefinidoComp = even.getIndefinido();
        if(indefinidoComp == true)
        {
            holder.fecha.setText("Indefinido");
            holder.asignar.setVisibility(View.VISIBLE);
            holder.asignar.setOnClickListener(v ->
            {
                AlertDialog.Builder mensaje;
                mensaje = new AlertDialog.Builder(v.getContext());
                mensaje.setTitle("Asignar fecha");
                String[] opciones = {"14/9/2020", "16/9/2020", "21/9/2020"};
                mensaje.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Asignar", "Elegí una opción");
                        OpcionElegida = opciones[which];
                    }
                });
                mensaje.setPositiveButton("Asignar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Asignar", "Tocó asignar");
                        even.setFecha(OpcionElegida);
                        even.setIndefinido(false);
                        ActualizarEvento(even.getId(), even.getTitulo(), even.getFecha(), even.getDuracion(), even.getHora(), even.getImportancia(), even.getComplementos(), even.getTags(), even.getLluvia(), even.getIndefinido(),even.getCompletado(), even.getValorado(), even.getIdCalendar());
                        EligeFechaParaIndefinido(even);
                    }
                });
                mensaje.create();
                mensaje.show();
            });
        }
        else
        {
            holder.fecha.setText(even.getFecha());
        }

        //holder.duracion.setText(Integer.toString(even.getDuracion()));
        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, AgregarEditar.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", even.getId());
            intent.putExtra("titulo", even.getTitulo());
            intent.putExtra("fecha", even.getFecha());
            intent.putExtra("hora", even.getHora());
            intent.putExtra("duracion", even.getDuracion());
            intent.putExtra("importancia", even.getImportancia());
            intent.putExtra("complementos", even.getComplementos());
            intent.putExtra("tags", even.getTags());
            intent.putExtra("lluvia", even.getLluvia());
            intent.putExtra("indefinido", even.getIndefinido());
            intent.putExtra("completado", even.getCompletado());
            intent.putExtra("valorado", even.getValorado());
            intent.putExtra("IndicadorActivity", indicadorActivity);
            intent.putExtra("Cant", 1);
            intent.putExtra("ListaRecursos", ListaRecursos);
            intent.putExtra("ListaTags", ListaTags);
            intent.putExtra("IdUsuario", IdUsuario);
            intent.putExtra("IdCalendar", even.getIdCalendar());

            ((Activity) context).startActivityForResult(intent,1);
        });

        holder.completado.setOnClickListener(v ->
        {
            String id = even.getId();
            String titulo = even.getTitulo();
            String fecha = even.getFecha();
            String hora = even.getHora();
            int duracion = even.getDuracion();
            int importancia = even.getImportancia();
            ArrayList<String> complementos = even.getComplementos();
            ArrayList<String> tags = even.getTags();
            Boolean lluvia = even.getLluvia();
            Boolean indefinido = even.getIndefinido();
            Boolean completado = true;
            Boolean valorado = false;
            long idCalendar = even.getIdCalendar();

            ActualizarEvento(id, titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado, valorado, idCalendar);

            this.eventos.remove(even);
            try {
                //Ponemos a "Dormir" el programa durante los ms que queremos
                Thread.sleep(0,5*1000);
            } catch (Exception e) {
                System.out.println(e);
            }
            this.notifyDataSetChanged();
            holder.completado.setChecked(false);
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Completado)
        CheckBox completado;
        @BindView(R.id.Titulo)
        TextView titulo;
        /*@BindView(R.id.image)
        CircleImageView imageView;*/
        @BindView(R.id.Fecha)
        TextView fecha;
        @BindView(R.id.Duracion)
        TextView duracion;
        @BindView(R.id.Asignar)
        Button asignar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void ActualizarEvento(String id, String titulo, String fecha, int duracion, String hora, int importancia, ArrayList<String> complementos, ArrayList<String> tags, Boolean lluvia, Boolean indefinido, Boolean completado, Boolean valorado, long idCalendar) {
        Map<String, Object> evento = (new Evento(id, titulo, fecha, duracion, hora, importancia, complementos, tags, lluvia, indefinido, completado, valorado, idCalendar)).toMap();

        db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .document(IdUsuario)
                .collection("Eventos")
                .document(id)
                .set(evento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(context.getApplicationContext(), "Evento actualizado correctamente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AgregarEvent", "Error al actualizar el evento", e);
                        //Toast.makeText(context.getApplicationContext(), "Ocurrió un error al actualizar la información", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void EligeFechaParaIndefinido(Evento evento){
        eventos.remove(evento);
        this.notifyDataSetChanged();
    }
}