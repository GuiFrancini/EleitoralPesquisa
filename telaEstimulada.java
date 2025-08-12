package com.example.eleitoralpesquisa;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class telaEstimulada extends AppCompatActivity {


    private RadioGroup radioGroupCandidatos;
    private Button btnAvancar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_estimulada);

        radioGroupCandidatos = findViewById(R.id.radioGroupCandidatos);
        btnAvancar = findViewById(R.id.btnAvancar);

        dbHelper = new DatabaseHelper(this);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int idSelecionado = radioGroupCandidatos.getCheckedRadioButtonId();

                if (idSelecionado == -1) {
                    Toast.makeText(telaEstimulada.this, "Selecione um candidato", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton candidatoSelecionado = findViewById(idSelecionado);
                String nomeCandidato = candidatoSelecionado.getText().toString();

                // Você pode salvar o nome do candidato aqui, se precisar
                boolean sucesso = dbHelper.inserirEstimulada(nomeCandidato);
                if (sucesso) {
                    Toast.makeText(telaEstimulada.this, "Candidato registrado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(telaEstimulada.this, "Erro ao registrar candidato", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(telaEstimulada.this, telaCadastro.class);
                intent.putExtra("candidatoEscolhido", nomeCandidato); // se quiser passar para a próxima tela
                startActivity(intent);
            }
        });
    }
}