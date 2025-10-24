package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BancoHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "app.db";
    private static final int VERSAO = 1;

    public BancoHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pessoa (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pessoa");
        onCreate(db);
    }

    public void inserirPessoa(String nome) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        db.insert("pessoa", null, valores);
        db.close();
    }

    public ArrayList<Pessoa> listarPessoas() {
        ArrayList<Pessoa> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome FROM pessoa ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            Pessoa p = new Pessoa();
            p.setId(cursor.getInt(0));
            p.setNome(cursor.getString(1));
            lista.add(p);
        }

        cursor.close();
        db.close();
        return lista;
    }
}
