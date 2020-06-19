package com.informatica.tutorialfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private List<Evento> eventos;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public EventoAdapter(List<Evento> eventos, Context context, FirebaseFirestore firestoreDB) {
        this.eventos = eventos;
        this.context = context;
        this.firestoreDB = firestoreDB;
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
        holder.fecha.setText(even.getFecha());
        holder.duracion.setText(Integer.toString(even.getDuracion()));
        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, AgregarEditar.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", even.getId());
            intent.putExtra("titulo", even.getTitulo());
            intent.putExtra("fecha", even.getFecha());
            intent.putExtra("hora", even.getDuracion());
            intent.putExtra("importancia", even.getImportancia());
            intent.putExtra("complementos", even.getComplementos());
            intent.putExtra("tags", even.getTags());
            intent.putExtra("lluvia", even.getLluvia());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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


}