package com.example.planetzeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button log_in = findViewById(R.id.login_btn1);
        log_in.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome.this, LoginView.class);
            startActivity(intent);
            finish();
        });

        Button register = findViewById(R.id.register_btn1);
        register.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome.this, Register.class);
            startActivity(intent);
            finish();
        });

    }

}
