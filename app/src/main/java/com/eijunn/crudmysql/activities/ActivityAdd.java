package com.eijunn.crudmysql.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.eijunn.crudmysql.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

public class ActivityAdd extends AppCompatActivity {
    MaterialEditText et_number , et_name, et_address, et_club ;
    String number, name, address, club ;
    Button btn_submit ;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_number = findViewById(R.id.et_number) ;
        et_name = findViewById(R.id.et_name) ;
        et_address = findViewById(R.id.et_address) ;
        et_club = findViewById(R.id.et_club) ;

        progressDialog = new ProgressDialog(this);

        btn_submit = findViewById(R.id.btn_submit) ;
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("menambahkan data .... ");
                progressDialog.setCancelable(false);
                progressDialog.show();

                number = et_number.getText().toString();
                name = et_name.getText().toString();
                address = et_address.getText().toString();
                club = et_club.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validateData() ;
                    }
                },1000);
            }
        });

    }

    private void validateData() {
        if ( number.equals("") || name.equals("") || address.equals("") || club.equals("") ) {
            progressDialog.dismiss();
            Toast.makeText(ActivityAdd.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        } else {
            sendData() ;
        }
    }

    private void sendData() {
        AndroidNetworking.post("http://192.168.43.38/Player/addData.php")
                .addBodyParameter("number",""+number)
                .addBodyParameter("name",""+name)
                .addBodyParameter("address",""+address)
                .addBodyParameter("club",""+club)
                .setPriority(Priority.MEDIUM)
                .setTag("add data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cek tambah",""+response);

                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result") ;
                            Toast.makeText(ActivityAdd.this, ""+pesan, Toast.LENGTH_SHORT).show();

                            Log.d("status",""+status) ;

                            if (status) {
                                new AlertDialog.Builder(ActivityAdd.this)
                                        .setMessage("berhasil menambahkan data...")
                                        .setCancelable(false)
                                        .setPositiveButton("kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = getIntent() ;
                                                setResult(RESULT_OK);
                                                ActivityAdd.this.finish();
                                            }
                                        })
                                        .show();
                            }else {
                                new AlertDialog.Builder(ActivityAdd.this)
                                        .setMessage("gagal menambahkan data...")
                                        .setPositiveButton("kembali",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = getIntent();
                                                setResult(RESULT_CANCELED);
                                                ActivityAdd.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show() ;
                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error tambah data",""+anError.getErrorBody()) ;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_back){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
