package com.example.eleitoralpesquisa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PesquisaEleitoral.db"; //criando o db pesquisa eleitoral
    private static final int DATABASE_VERSION = 1;

    // Tabela Cadastro
    public static final String TABLE_CADASTRO = "Cadastro";
    public static final String COL_ID = "id";
    public static final String COL_NOME = "nome";
    public static final String COL_TELEFONE = "telefone";
    public static final String COL_CIDADE = "cidade";
    public static final String COL_DATAHORA = "datahora";
    public static final String COL_LOCALIZACAO = "localizacao";

    // Tabela Estimulada
    public static final String TABLE_ESTIMULADA = "Estimulada";
    public static final String COL_ID_ESTIMULADA = "id";
    public static final String COL_CANDIDATO_ESCOLHIDO = "candidato";

    // Tabela Espontanea
    public static final String TABLE_ESPONTANEA = "Espontanea";
    public static final String COL_ID_ESPONTANEA = "id";
    public static final String COL_CANDIDATO_DIGITADO = "candidato";

    // Tabela Problemas
    public static final String TABLE_PROBLEMAS = "Problemas";
    public static final String COL_ID_PROBLEMA = "id";
    public static final String COL_PROBLEMA = "problema";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CADASTRO + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME + " TEXT, " +
                COL_TELEFONE + " TEXT, " +
                COL_CIDADE + " TEXT, " +
                COL_DATAHORA + " TEXT, " +
                COL_LOCALIZACAO + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ESTIMULADA + " (" +
                COL_ID_ESTIMULADA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CANDIDATO_ESCOLHIDO + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ESPONTANEA + " (" +
                COL_ID_ESPONTANEA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CANDIDATO_DIGITADO + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_PROBLEMAS + " (" +
                COL_ID_PROBLEMA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROBLEMA + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CADASTRO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTIMULADA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESPONTANEA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROBLEMAS);
        onCreate(db);
    }
    // Inserção de dados
    public boolean inserirCadastro(String nome, String telefone, String cidade, String dataHora, String localizacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NOME, nome);
            values.put(COL_TELEFONE, telefone);
            values.put(COL_CIDADE, cidade);
            values.put(COL_DATAHORA, dataHora);
            values.put(COL_LOCALIZACAO, localizacao);

            long resultado = db.insert(TABLE_CADASTRO, null, values);
            return resultado != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    // Inserir dados da tela estimulada
    public boolean inserirEstimulada(String nomeCandidato) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_CANDIDATO_ESCOLHIDO, nomeCandidato); // Certifique-se de que a coluna se chama 'candidato' na tabela
            long resultado = db.insert(TABLE_ESTIMULADA, null, values);
            return resultado != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    // Inserir dados da tela espontânea
    public boolean inserirEspontanea(String nomePrefeito, List<String> problemas) {
        SQLiteDatabase db = this.getWritableDatabase();
        long idEspontanea = -1;

        try {
            // 1. Inserir o candidato digitado na tabela Espontanea
            ContentValues candidatoValues = new ContentValues();
            candidatoValues.put(COL_CANDIDATO_DIGITADO, nomePrefeito);
            idEspontanea = db.insert(TABLE_ESPONTANEA, null, candidatoValues);

            if (idEspontanea == -1) {
                return false; // falha ao inserir o candidato
            }

            // 2. Inserir cada problema na tabela Problemas
            for (String problema : problemas) {
                ContentValues problemaValues = new ContentValues();
                problemaValues.put(COL_PROBLEMA, problema);
                long result = db.insert(TABLE_PROBLEMAS, null, problemaValues);

                if (result == -1) {
                    return false; // falha ao inserir algum problema
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


    // Consultas de contagem para gráficos
    public Cursor getContagemProblemas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_PROBLEMA + ", COUNT(*) as total FROM " + TABLE_PROBLEMAS +
                " GROUP BY " + COL_PROBLEMA + " ORDER BY total DESC";
        return db.rawQuery(query, null);
    }
    public Cursor getContagemEstimulada() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_CANDIDATO_ESCOLHIDO + ", COUNT(*) as total FROM " + TABLE_ESTIMULADA +
                " GROUP BY " + COL_CANDIDATO_ESCOLHIDO + " ORDER BY total DESC";
        return db.rawQuery(query, null);
    }
    public Cursor getContagemEspontanea() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_CANDIDATO_DIGITADO + ", COUNT(*) as total FROM " + TABLE_ESPONTANEA +
                " GROUP BY " + COL_CANDIDATO_DIGITADO + " ORDER BY total DESC";
        return db.rawQuery(query, null);
    }
}
