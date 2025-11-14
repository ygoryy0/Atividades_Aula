package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// A MainActivity implementa ambas as interfaces
public class MainActivity extends AppCompatActivity
        implements PessoaAdapter.OnItemLongClickListener, PessoaAdapter.OnItemClickListener {

    EditText editNome;
    Button btnSalvar;
    RecyclerView recyclerView;
    BancoHelper helper;
    PessoaAdapter adapter;
    ArrayList<Pessoa> listaPessoas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        btnSalvar = findViewById(R.id.btnSalvar);
        recyclerView = findViewById(R.id.recyclerView);

        helper = new BancoHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carregarLista();

        btnSalvar.setOnClickListener(v -> {
            String nome = editNome.getText().toString().trim();
            if (!nome.isEmpty()) {
                helper.inserirPessoa(nome);
                editNome.setText("");
                atualizarLista();
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarLista() {
        // Usa addAll para evitar recriar o objeto listaPessoas
        listaPessoas.addAll(helper.listarPessoas());

        adapter = new PessoaAdapter(listaPessoas);

        // Configura os DOIS Listeners
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    private void atualizarLista() {
        // Correção de performance: Limpa e recarrega os dados, notificando o adapter
        listaPessoas.clear();
        listaPessoas.addAll(helper.listarPessoas());
        adapter.notifyDataSetChanged();
    }

    // ------------------------------------------
    // LÓGICA DE EXCLUSÃO (DELETE - Clique Longo)
    // ------------------------------------------

    @Override
    public void onItemLongClick(final int idDaPessoa) {
        String nomeParaExcluir = "";
        for (Pessoa p : listaPessoas) {
            if (p.getId() == idDaPessoa) {
                nomeParaExcluir = p.getNome();
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Excluir " + nomeParaExcluir + "?")
                .setMessage("Tem certeza que deseja excluir este nome?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        excluirRegistro(idDaPessoa);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirRegistro(int id) {
        helper.excluirPessoa(id);
        atualizarLista();
        Toast.makeText(this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
    }

    // ------------------------------------------
    // LÓGICA DE EDIÇÃO (UPDATE - Clique Normal)
    // ------------------------------------------

    @Override
    public void onItemClick(final int idDaPessoa, String nomeAtual) {

        // Cria um EditText para o diálogo
        final EditText input = new EditText(this);
        input.setText(nomeAtual);
        input.setHint("Digite o novo nome");

        new AlertDialog.Builder(this)
                .setTitle("Editar Nome")
                .setView(input)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String novoNome = input.getText().toString().trim();
                        if (!novoNome.isEmpty()) {
                            // Chama o método de atualização do BancoHelper
                            helper.atualizarPessoa(idDaPessoa, novoNome);
                            atualizarLista();
                            Toast.makeText(MainActivity.this, "Nome atualizado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "O nome não pode ser vazio.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}