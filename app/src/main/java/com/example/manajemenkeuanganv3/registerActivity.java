package com.example.manajemenkeuanganv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class registerActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_registergaterpakai);
    }

    public void Register2(View view) {
        Intent intent = new Intent(registerActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }

}


