package com.example.manajemenkeuanganv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.manajemenkeuanganv3.helper.Config;
import com.example.manajemenkeuanganv3.model.User;
import com.example.manajemenkeuanganv3.transaksikeuangan.mainKeuangan;
import com.example.manajemenkeuanganv3.transaksipiutang.mainPiutang;
import com.example.manajemenkeuanganv3.volley.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class homeActivity extends AppCompatActivity {
    TextView textBalance,textBalancep,welcome;
    Button inout, piutang;
    ArrayList<HashMap<String, String>> arusuang, arusPiutang;
    public static String id, uname;
    SwipeRefreshLayout swipeRefreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataBalance();
                readBalanceP();
            }
        });

        textBalance=findViewById(R.id.textBalance);
        textBalancep=findViewById(R.id.textBalanceP);
        arusuang= new ArrayList<>();
        arusPiutang= new ArrayList<>();
        User user = SharedPrefManager.getInstance(this).getUser();
        welcome=findViewById(R.id.ucapan);
        uname=user.getName();
        welcome.setText("Selamat Datang, "+uname);
        inout=findViewById(R.id.inout);
        piutang=findViewById(R.id.piutang);
        piutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mainPiutang.class));
            }
        });
        inout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mainKeuangan.class));
            }
        });

        onLoadingData();
    }

    private void onLoadingData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                readDataBalance();
                readBalanceP();
            }
        });
    }

    private void readDataBalance(){
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(Config.Host+"read2.php")
                .addBodyParameter("username", uname)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);
                            textBalance.setText(rupiah.format(response.getDouble("masuk")-response.getDouble("keluar")));
                            swipeRefreshLayout.setRefreshing(false);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("ReadData", "onError: " + error);
                    }
                });
    }

    private  void readBalanceP(){
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(Config.Host2+"read.php")
                .addBodyParameter("username1", uname)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);
                            textBalancep.setText(rupiah.format(response.getDouble("aset")));

                            swipeRefreshLayout.setRefreshing(false);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("ReadData", "onError: " + error);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(getApplicationContext(), aboutActivity.class));
            return true;
        }else if(id == R.id.action_logout){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
        return super.onOptionsItemSelected(item);
    }
}
