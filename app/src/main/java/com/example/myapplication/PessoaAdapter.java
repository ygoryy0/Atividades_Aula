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
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    // Interface 1: Clique Normal (UPDATE/Edição)
    public interface OnItemClickListener {
        void onItemClick(int idDaPessoa, String nomeAtual);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    // Interface 2: Clique Longo (DELETE/Exclusão)
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
        return new ViewHolder(view, clickListener, longClickListener, lista);
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

        public ViewHolder(@NonNull View itemView,
                          final OnItemClickListener clickListener,
                          final OnItemLongClickListener longClickListener,
                          final ArrayList<Pessoa> lista) {
            super(itemView);
            textNome = itemView.findViewById(R.id.textNome);

            // CLIQUE NORMAL (UPDATE/Edição)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Pessoa p = lista.get(position);
                            // Chama a função de edição
                            clickListener.onItemClick(p.getId(), p.getNome());
                        }
                    }
                }
            });

            // CLIQUE LONGO (DELETE/Exclusão)
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            int idDaPessoa = lista.get(position).getId();
                            // Chama a função de exclusão
                            longClickListener.onItemLongClick(idDaPessoa);
                            return true; // É crucial retornar TRUE aqui para consumir o evento
                        }
                    }
                    return false;
                }
            });
        }
    }
}