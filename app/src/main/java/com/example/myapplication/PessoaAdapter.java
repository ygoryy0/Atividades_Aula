package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.ViewHolder> {

    private ArrayList<Pessoa> lista;
    private OnItemLongClickListener longClickListener;

    // Interface para comunicação do clique longo
    public interface OnItemLongClickListener {
        void onItemLongClick(int idDaPessoa);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public PessoaAdapter(ArrayList<Pessoa> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view, longClickListener, lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pessoa p = lista.get(position);
        holder.textNome.setText(p.getNome());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNome;

        public ViewHolder(@NonNull View itemView, final OnItemLongClickListener listener, final ArrayList<Pessoa> lista) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);

            // Configura o CLIQUE LONGO (Pressionar e Segurar)
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Envia o ID da Pessoa para a MainActivity
                            int idDaPessoa = lista.get(position).getId();
                            listener.onItemLongClick(idDaPessoa);
                            return true; // Consome o evento
                        }
                    }
                    return false;
                }
            });
        }
    }
}