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

// A MainActivity implementa a interface de clique longo para receber o evento de exclusão
public class MainActivity extends AppCompatActivity implements PessoaAdapter.OnItemLongClickListener {

    EditText editNome;
    Button btnSalvar;
    RecyclerView recyclerView;
    BancoHelper helper;
    PessoaAdapter adapter;
    // IMPORTANTE: Inicializar a lista vazia para evitar NullPointerException
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

        // Inicializa o adapter e carrega os dados pela primeira vez
        carregarLista();

        btnSalvar.setOnClickListener(v -> {
            String nome = editNome.getText().toString().trim();
            if (!nome.isEmpty()) {
                helper.inserirPessoa(nome);
                editNome.setText("");

                // Chamada modificada para atualizar a lista após inserção
                atualizarLista();

                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método chamado APENAS na criação da Activity
    private void carregarLista() {
        listaPessoas.addAll(helper.listarPessoas());

        adapter = new PessoaAdapter(listaPessoas);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // NOVO MÉTODO: Chamado para atualizar os dados do RecyclerView após INSERT ou DELETE
    private void atualizarLista() {
        // 1. Limpa a lista de dados atual (no Adapter)
        listaPessoas.clear();

        // 2. Adiciona todos os dados lidos novamente do banco
        listaPessoas.addAll(helper.listarPessoas());

        // 3. Notifica o Adapter que os dados mudaram para redesenhar
        adapter.notifyDataSetChanged();
    }

    // Implementação do método de clique longo
    @Override
    public void onItemLongClick(final int idDaPessoa) {
        // Busca o nome para exibir na caixa de diálogo
        String nomeParaExcluir = "";
        for (Pessoa p : listaPessoas) {
            if (p.getId() == idDaPessoa) {
                nomeParaExcluir = p.getNome();
                break;
            }
        }

        // Constrói o AlertDialog (Pop-up de confirmação)
        new AlertDialog.Builder(this)
                .setTitle("Excluir " + nomeParaExcluir + "?")
                .setMessage("Tem certeza que deseja excluir este nome?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Chama o método que executa a exclusão no banco e atualiza o RecyclerView
                        excluirRegistro(idDaPessoa);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Método auxiliar para realizar a exclusão e atualização
    private void excluirRegistro(int id) {
        helper.excluirPessoa(id);

        // Atualiza a lista de forma eficiente
        atualizarLista();

        Toast.makeText(this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
    }
}