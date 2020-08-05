package com.informatica.tutorialfirebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventoValoracionesAdapter extends RecyclerView.Adapter<EventoValoracionesAdapter.ViewHolder> {

    private List<Evento> eventos;
    private Context context;
    private FirebaseFirestore firestoreDB;
    int indicadorActivity = 3;
    String IdUsuario;
    private FirebaseFirestore db;


    public EventoValoracionesAdapter(List<Evento> eventos, Context context, FirebaseFirestore firestoreDB) {
        this.eventos = eventos;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    public EventoValoracionesAdapter(List<Evento> eventos, Context context, FirebaseFirestore firestoreDB, String IdUsuario) {
        this.eventos = eventos;
        this.context = context;
        this.firestoreDB = firestoreDB;
        this.IdUsuario = IdUsuario;
    }

    @Override
    public EventoValoracionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new EventoValoracionesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventoValoracionesAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Evento even = eventos.get(itemPosition);
       // progressBar.setVisibility(View.GONE);
        holder.titulo.setText(even.getTitulo());
        holder.fecha.setText(even.getFecha());
        //holder.duracion.setText(Integer.toString(even.getDuracion()));
        holder.completado.setChecked(true);
        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, ValoracionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            intent.putExtra("IndicadorActivity", indicadorActivity);
            intent.putExtra("Cant", 1);
            intent.putExtra("IdUsuario", IdUsuario);
            intent.putExtra("IdCalendar", even.getIdCalendar());


            context.startActivity(intent);
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
            Boolean completado = false;
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
                        Toast.makeText(context.getApplicationContext(), "Evento actualizado correctamente!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AgregarEvent", "Error al actualizar el evento", e);
                        Toast.makeText(context.getApplicationContext(), "Ocurrió un error al actualizar la información", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}