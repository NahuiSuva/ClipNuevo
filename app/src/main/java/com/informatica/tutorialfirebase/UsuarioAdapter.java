package com.informatica.tutorialfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private List<Usuario> usuarios;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public UsuarioAdapter(List<Usuario> usuarios, Context context, FirebaseFirestore firestoreDB) {
        this.usuarios = usuarios;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_usuarios, parent, false);

        return new UsuarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsuarioAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Usuario usu = usuarios.get(itemPosition);
        holder.titulo.setText(usu.getNombre());
        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", usu.getId());
            intent.putExtra("nombre", usu.getNombre());


            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Titulo)
        TextView titulo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}