package com.informatica.tutorialfirebase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    ArrayList<Tag> ListaTags;
    private Context context;
    private FirebaseFirestore db;
    private String idUsuario;


    public TagAdapter(ArrayList<Tag> listaTags, Context contexto, FirebaseFirestore bdd, String id){
        this.ListaTags = listaTags;
        this.context=contexto;
        this.db=bdd;
        idUsuario = id;
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tag, parent, false);

        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Tag tag = ListaTags.get(itemPosition);
        holder.titulo.setText(tag.getNombre());
        holder.eliminar.setOnClickListener(v -> {
            EliminarTag(tag.getId());
            ListaTags.remove(tag);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ListaTags.size();
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

    private void EliminarTag(String idTag){
        db.collection("usuarios")
                .document(idUsuario)
                .collection("Tags")
                .document(idTag)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Borrar tag","Tag borrado exitosamente!");
                    }
                });
    }

    public void AgregarElemento(String nombre, String id){
        Log.d("Agregar Complemento", "Agrego complemento a la lista");
        Tag miTag=new Tag(id,nombre);
        //ListaTags.add(miTag);
        Log.d("Agregar Complemento", "La cantidad de elementos es "+ListaTags.size());
        this.notifyDataSetChanged();
    }
}