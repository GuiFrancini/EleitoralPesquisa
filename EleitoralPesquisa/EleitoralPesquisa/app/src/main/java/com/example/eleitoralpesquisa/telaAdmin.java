package com.example.eleitoralpesquisa;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class telaAdmin extends AppCompatActivity {

    private PieChart pieProblemas, pieEstimulada, pieEspontanea;// variaveis do tipo piechart
    private DatabaseHelper dbHelper; //variavel do tipo de banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_admin);

        dbHelper = new DatabaseHelper(this);//metodo construtor
        pieProblemas = findViewById(R.id.pieProblemas);
        pieEstimulada = findViewById(R.id.pieEstimulada);
        pieEspontanea = findViewById(R.id.pieEspontanea);


        //dbHelper.inserirProblema("Saude");
        //dbHelper.inserirProblema("saude");
        //dbHelper.inserirProblema("Falta de iluminação");
        //dbHelper.inserirProblema("Falta de água");

       // dbHelper.inserirEstimulada("Maria da Saúde");
        //dbHelper.inserirEstimulada("João da Feira");
        //dbHelper.inserirEstimulada("Maria da Saúde");

        //dbHelper.inserirEspontanea("João da Feira");
      //  dbHelper.inserirEspontanea("João da Feira");
        //dbHelper.inserirEspontanea("Maria da Saúde");

        preencherGraficoProblemas();
        preencherGraficoEstimulada();
        preencherGraficoEspontanea();

        // Botão Voltar ao Login
        Button btnVoltar = findViewById(R.id.btnVoltarLogin);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(telaAdmin.this, MainActivity.class);
                startActivity(intent);
                finish(); // finaliza tela atual para não voltar com botão voltar
            }
        });

    }

    private void preencherGraficoProblemas() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor cursor = dbHelper.getContagemProblemas();
        Log.d("DEBUG_PROBLEMAS", "Total de linhas: " + cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String problema = cursor.getString(0);
                int qtd = cursor.getInt(1);
                entries.add(new PieEntry(qtd, problema));
            } while (cursor.moveToNext());
            cursor.close();
        }
        PieDataSet dataSet = new PieDataSet(entries, "Problemas da Cidade");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieProblemas.setData(data);
        pieProblemas.invalidate();

    }
    private void preencherGraficoEstimulada() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor cursor = dbHelper.getContagemEstimulada();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String candidato = cursor.getString(0);
                int votos = cursor.getInt(1);
                entries.add(new PieEntry(votos, candidato));
            } while (cursor.moveToNext());
            cursor.close();
        }
        PieDataSet dataSet = new PieDataSet(entries, "Votos Estimulada");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieEstimulada.setData(data);
        pieEstimulada.invalidate();
    }
    private void preencherGraficoEspontanea() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor cursor = dbHelper.getContagemEspontanea();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(0);
                int qtd = cursor.getInt(1);
                entries.add(new PieEntry(qtd, nome));
            } while (cursor.moveToNext());
            cursor.close();
        }
        PieDataSet dataSet = new PieDataSet(entries, "Respostas Espontâneas");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pieEspontanea.setData(data);
        pieEspontanea.invalidate();
    }
}

