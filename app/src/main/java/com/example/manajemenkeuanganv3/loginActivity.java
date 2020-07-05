package com.example.manajemenkeuanganv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity  extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activitytidakterpakai);
    }



    public void login(View view) {
        Intent intent = new Intent(loginActivity.this, homeActivity.class);
        startActivity(intent);
        finish();
    }

    public void Register(View view) {
        Intent intent = new Intent(loginActivity.this, registerActivity.class);
        startActivity(intent);
        finish();
    }

}


