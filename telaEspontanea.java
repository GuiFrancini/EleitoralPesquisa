package com.example.eleitoralpesquisa;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class telaEspontanea extends AppCompatActivity {
    private EditText edtPrefeito;
    private CheckBox ckbSaude, ckbEducacao, ckbSeguranca, ckbTransporte, ckbEmprego;
    private Button btnAvancar;

    private ArrayList<CheckBox> checkboxesSelecionados = new ArrayList<>();

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_espontanea);

        edtPrefeito = findViewById(R.id.respostaPrefeito);
        ckbSaude = findViewById(R.id.ckbSaude);
        ckbEducacao = findViewById(R.id.ckbEducacao);
        ckbSeguranca = findViewById(R.id.ckbSeguranca);
        ckbTransporte = findViewById(R.id.ckbTransporte);
        ckbEmprego = findViewById(R.id.ckbEmprego);
        btnAvancar = findViewById(R.id.btnAvancar);

        dbHelper = new DatabaseHelper(this);

        // Aplica a limitação de 3 seleções sobre todos os checkboxes
        aplicarLimitacaoCheckBox(ckbSaude);
        aplicarLimitacaoCheckBox(ckbEducacao);
        aplicarLimitacaoCheckBox(ckbSeguranca);
        aplicarLimitacaoCheckBox(ckbTransporte);
        aplicarLimitacaoCheckBox(ckbEmprego);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomePrefeito = edtPrefeito.getText().toString().trim();

                if (nomePrefeito.isEmpty()) {
                    Toast.makeText(telaEspontanea.this, "Digite o nome do candidato", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkboxesSelecionados.size() > 3) {
                    Toast.makeText(telaEspontanea.this, "Escolha no máximo 3 problemas", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> problemasSelecionados = new ArrayList<>();
                for (CheckBox cb : checkboxesSelecionados) {
                    problemasSelecionados.add(cb.getText().toString());
                }

                // 👉 Inserir no banco de dados
                boolean sucesso = dbHelper.inserirEspontanea(nomePrefeito, problemasSelecionados);

                if (sucesso) {
                    Toast.makeText(telaEspontanea.this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(telaEspontanea.this, "Erro ao salvar os dados!", Toast.LENGTH_SHORT).show();
                }
                // Enviar para a próxima tela
                Intent intent = new Intent(telaEspontanea.this, telaEstimulada.class);
                //passar os dados  para outra tela do prefeito
                // Você pode adicionar os problemas selecionados também, se quiser

                startActivity(intent);
            }
        });
    }

    private void aplicarLimitacaoCheckBox(CheckBox checkBox) { //metodo para limitar a seleçao dos checkboxs de problemas da cidade
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                if (isChecked) {
                    if (checkboxesSelecionados.size() >= 3) {
                        btn.setChecked(false);
                        Toast.makeText(telaEspontanea.this, "Por favor, escolha ate de 3 problemas.", Toast.LENGTH_SHORT).show();
                    } else {
                        checkboxesSelecionados.add(checkBox);
                    }
                } else {
                    checkboxesSelecionados.remove(checkBox);
                }
            }
        });


    }
}