package com.example.manajemenkeuanganv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.manajemenkeuanganv3.R;
import com.example.manajemenkeuanganv3.user.loginActivity;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(getApplicationContext(), loginActivity.class);
                startActivity(home);
                finish();

            }
        },4000);
    }
}
