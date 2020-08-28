package com.informatica.tutorialfirebase;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {

    ArrayList<Recurso> ListaRecursos;
    private Context context;
    private FirebaseFirestore db;
    private String idUsuario;


    public RecursoAdapter(ArrayList<Recurso> listaRec, Context contexto, FirebaseFirestore bdd, String id){
        this.ListaRecursos = listaRec;
        this.context=contexto;
        this.db=bdd;
        idUsuario = id;
    }

    @Override
    public RecursoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recurso, parent, false);

        return new RecursoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecursoAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Recurso rec = ListaRecursos.get(itemPosition);
        holder.titulo.setText(rec.getNombre());
        holder.eliminar.setOnClickListener(v -> {
            EliminarRecurso(rec.getId());
            ListaRecursos.remove(rec);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ListaRecursos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Titulo)
        TextView titulo;
        @BindView(R.id.botonEliminar)
        Button eliminar;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void EliminarRecurso(String idRecurso){
        db.collection("usuarios")
                .document(idUsuario)
                .collection("Complementos")
                .document(idRecurso)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Borrar recurso","Complemento borrado exitosamente!");
                    }
                });

    }

    public void AgregarElemento(String nombre, String id){
        Log.d("Agregar Complemento", "Agrego complemento a la lista");
        Recurso miRec=new Recurso(id,nombre);
        //ListaRecursos.add(miRec);
        Log.d("Agregar Complemento", "La cantidad de elementos es "+ListaRecursos.size());
        this.notifyDataSetChanged();
    }
}