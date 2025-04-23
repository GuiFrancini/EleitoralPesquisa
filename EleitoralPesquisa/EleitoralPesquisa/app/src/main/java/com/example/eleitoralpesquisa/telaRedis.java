package com.example.eleitoralpesquisa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class telaRedis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_redis);

        // Delay de 4 segundos (9611 milissegundos)
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(telaRedis.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza a tela atual
        }, 5500);

    }
}