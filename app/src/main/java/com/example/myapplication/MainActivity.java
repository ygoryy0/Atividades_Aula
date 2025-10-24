package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editNome;
    Button btnSalvar;
    RecyclerView recyclerView;
    BancoHelper helper;
    PessoaAdapter adapter;
    ArrayList<Pessoa> listaPessoas;

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
                carregarLista();
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarLista() {
        listaPessoas = helper.listarPessoas();
        adapter = new PessoaAdapter(listaPessoas);
        recyclerView.setAdapter(adapter);
    }
}
