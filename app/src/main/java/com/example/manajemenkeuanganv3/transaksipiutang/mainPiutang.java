package com.example.manajemenkeuanganv3.transaksipiutang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.manajemenkeuanganv3.helper.Config;
import com.example.manajemenkeuanganv3.R;
import com.example.manajemenkeuanganv3.model.User;
import com.example.manajemenkeuanganv3.volley.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhuandian.rippleview.RippleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class mainPiutang extends AppCompatActivity {
    TextView textPiutang, textHutang, textAset;
    ListView list_piutang;
    ArrayList<HashMap<String, String>> aruspiutang;
    SwipeRefreshLayout swipe_refresh;

    public static String id1, status1, jumlah1, nama1, tanggal1, tanggal21, username1, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_piutang);

        textPiutang=findViewById(R.id.textPiutang);
        textHutang=findViewById(R.id.textHutang);
        textAset=findViewById(R.id.textAset);
        list_piutang=findViewById(R.id.recyclerview11);
        aruspiutang=new ArrayList<>();
        swipe_refresh=findViewById(R.id.swipe_refresh1);
        User user = SharedPrefManager.getInstance(this).getUser();
        username = user.getName();

        getSupportActionBar().setTitle("Piutang");

        FloatingActionButton fab = findViewById(R.id.buttonAdd1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainPiutang.this, tambahPiutang.class);
                startActivity(i);
            }
        });
        readData();
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swipe_refresh.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        recreate();
                    }
                }, 5000);
            }
        });
    }

    private void readData() {
        aruspiutang.clear();
        list_piutang.setAdapter(null);
        AndroidNetworking.post(Config.Host2+"read.php")
                .addBodyParameter("username1", username)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);

                            textPiutang.setText(rupiah.format(response.getDouble("piutang")));
                            textHutang.setText(rupiah.format(response.getDouble("hutang")));
                            textAset.setText(rupiah.format(response.getDouble("aset")));

                            JSONArray jsonArray = response.getJSONArray("hasil1");
                            for (int i=0; i <jsonArray.length();i++){
                                JSONObject jsonObject;
                                jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id1", jsonObject.getString("id1"));
                                map.put("status1", jsonObject.getString("status1"));
                                map.put("jumlah1", jsonObject.getString("jumlah1"));
                                map.put("nama1", jsonObject.getString("nama1"));
                                map.put("username1", jsonObject.getString("username1"));
                                map.put("tanggal1", jsonObject.getString("tanggal1"));
                                map.put("tanggal21", jsonObject.getString("tanggal21"));


                                aruspiutang.add(map);
                            }
                            adapterRead();
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

    private void adapterRead() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruspiutang, R.layout.listpiutang,
                new String[]{"id1", "status1", "jumlah1","nama1", "tanggal1","tanggal21", "username1"},
                new int[]{R.id.text_transaksi_id1, R.id.text_status1, R.id.text_jumlah1, R.id.text_nama1, R.id.text_tanggal1,R.id.text_tanggal21, R.id.text_transaksiUsername1}
        );
        list_piutang.setAdapter(simpleAdapter);
        list_piutang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long iD) {
                id1 = ((TextView)view.findViewById(R.id.text_transaksi_id1)).getText().toString();
                status1 = ((TextView)view.findViewById(R.id.text_status1)).getText().toString();
                jumlah1 = ((TextView)view.findViewById(R.id.text_jumlah1)).getText().toString();
                nama1 = ((TextView)view.findViewById(R.id.text_nama1)).getText().toString();
                username1= ((TextView)view.findViewById(R.id.text_transaksiUsername1)).getText().toString();
                tanggal1 = ((TextView)view.findViewById(R.id.text_tanggal1)).getText().toString();
                tanggal21 = ((TextView)view.findViewById(R.id.text_tanggal21)).getText().toString();

                ListMenu();
            }
        });
    }

    private void ListMenu(){
        final Dialog dialog = new Dialog(mainPiutang.this);
        dialog.setContentView(R.layout.listmenu2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RippleView rip_hapus = dialog.findViewById(R.id.rip_hapus2);
        RippleView rip_edit = dialog.findViewById(R.id.rip_edit2);
        rip_hapus.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                hapusdata();
            }
        });
        rip_edit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                startActivity(new Intent(mainPiutang.this, editActivity2.class));
            }
        });

        dialog.show();
    }

    private void hapusdata() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah Anda Yakin Menghapus Data Ini?");
        builder.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        AndroidNetworking.post(Config.Host2+"delete.php")
                                .addBodyParameter("id1", id1)
                                .setPriority(Priority.MEDIUM)
                                .setTag("Delete Data")
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // do anything with response
                                        try {
                                            if(response.getString("response").equals("succes")){

                                                Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
                                                readData();
                                            }else {
                                                Toast.makeText(mainPiutang.this,response.getString("response"),Toast.LENGTH_LONG).show();
                                            }
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        Log.d("ErrorHapusData",""+error.getErrorDetail());
                                    }
                                });
                    }
                });
        builder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
