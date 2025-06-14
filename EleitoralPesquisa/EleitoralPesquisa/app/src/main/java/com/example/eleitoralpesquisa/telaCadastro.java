package com.example.eleitoralpesquisa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class telaCadastro extends AppCompatActivity {
    private EditText edtNome, edtTelefone, edtCidade;
    private TextView txtDataHora, txtEndereco;
    private Button btnConcluir;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private static final int REQUEST_LOCATION = 1;


    private String localizacaoFormatada = "Localização não encontrada";
    private String dataHoraAtual = "";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCidade = findViewById(R.id.edtCidade);
        txtDataHora = findViewById(R.id.txtDataHora);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnConcluir = findViewById(R.id.btnConcluir);

        dbHelper = new DatabaseHelper(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        txtDataHora.setText("Data e Hora: " + dataHoraAtual);

        // Verificar e pedir permissão
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            pegarLocalizacao();
        }



        // Preencher data e hora
        String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        txtDataHora.setText("Data e Hora: " + dataHora);

        // Localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        pegarLocalizacao();

        btnConcluir.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String telefone = edtTelefone.getText().toString().trim();
            String cidade = edtCidade.getText().toString().trim();

            if (nome.isEmpty() || telefone.isEmpty() || cidade.isEmpty()) {
                Toast.makeText(telaCadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean sucesso = dbHelper.inserirCadastro(nome, telefone, cidade, dataHoraAtual, localizacaoFormatada);

            if (sucesso) {
                Toast.makeText(telaCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(telaCadastro.this, telaRedis.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(telaCadastro.this, "Erro ao salvar no banco de dados", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void pegarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissão não concedida, não prossegue
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        Toast.makeText(this, "Latitude: " + lat + "\nLongitude: " + lon, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Localização não encontrada", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pegarLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}