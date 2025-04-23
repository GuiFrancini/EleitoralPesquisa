package com.example.eleitoralpesquisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private EditText edtUsuario, edtSenha;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);

        //o click do botao
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edtUsuario.getText().toString();
                String senha = edtSenha.getText().toString();

                if (usuario.equals("admin") && senha.equals("000")) {
                    Intent intent = new Intent(MainActivity.this, telaAdmin.class);
                    startActivity(intent);
                    finish();
                }

                else if (usuario.equals("pesquisa") && senha.equals("000")) {
                    Intent intent = new Intent(MainActivity.this, telaEspontanea.class);
                    startActivity(intent);
                    finish();
                }


                else {
                    Toast.makeText(MainActivity.this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}