package com.example.manajemenkeuanganv3.transaksipiutang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.manajemenkeuanganv3.helper.Config;
import com.example.manajemenkeuanganv3.helper.CurrentDate;
import com.example.manajemenkeuanganv3.R;
import com.example.manajemenkeuanganv3.transaksikeuangan.mainKeuangan;
import com.zhuandian.rippleview.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class editActivity2 extends AppCompatActivity {
    RadioGroup radio_status1;
    RadioButton radio_hutang1, radio_piutang1;
    EditText edit_jumlah1, edit_nama1, edit_tanggal1;
    Button btn_simpan1;
    RippleView rip_simpan1;
    String status, tanggal1, username1;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        radio_status1        = findViewById(R.id.radio_status2);
        radio_hutang1        = findViewById(R.id.radio_hutang2);
        radio_piutang1       = findViewById(R.id.radio_piutang2);
        edit_jumlah1         = findViewById(R.id.edit_jumlah2);
        edit_nama1           = findViewById(R.id.edit_nama2);
        edit_tanggal1        = findViewById(R.id.edit_tanggal2);
        btn_simpan1          = findViewById(R.id.btn_simpan2);
        rip_simpan1          = findViewById(R.id.rip_simpan2);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        status = mainPiutang.status1;
        username1 = mainPiutang.username1;
        switch (status){
            case "HUTANG": radio_hutang1.setChecked(true);
                break;
            case "PIUTANG": radio_piutang1.setChecked(true);
                break;
        }

        radio_status1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_hutang2: status = "HUTANG";
                        break;
                    case  R.id.radio_piutang2: status = "PIUTANG";
                        break;
                }
            }
        });

        edit_jumlah1.setText(mainPiutang.jumlah1);
        edit_nama1.setText(mainPiutang.nama1);
        tanggal1 = mainPiutang.tanggal21;
        edit_tanggal1.setText(mainPiutang.tanggal1);
        edit_tanggal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(editActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");

                        tanggal1 = year + "-" + numberFormat.format(month +1) + "-" + numberFormat.format(dayOfMonth);
                        Log.e(" tanggal1", tanggal1);

                        edit_tanggal1.setText(numberFormat.format(dayOfMonth) + "/" + numberFormat.format(month+1) +
                                "/"  + year);

                    }
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        rip_simpan1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                AndroidNetworking.post(Config.Host2+"update.php")
                        .addBodyParameter("id1", mainPiutang.id1)
                        .addBodyParameter("status1", status)
                        .addBodyParameter("jumlah1", edit_jumlah1.getText().toString())
                        .addBodyParameter("nama1", edit_nama1.getText().toString())
                        .addBodyParameter("tanggal1", tanggal1)
                        .addBodyParameter("username1", username1)
                        .setPriority(Priority.MEDIUM)
                        .setTag("Update Data")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    if(response.getString("response").equals("succes")){

                                        Toast.makeText(editActivity2.this, "Perubahan Berehasil Disimpan",
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), mainKeuangan.class));
                                    }else {
                                        Toast.makeText(editActivity2.this,response.getString("response"),Toast.LENGTH_LONG).show();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("ErrorEditData",""+error.getErrorDetail());
                            }
                        });
            }
        });
    }
}
