package com.eijunn.crudmysql;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.eijunn.crudmysql.activities.ActivityAdd;
import com.eijunn.crudmysql.adapter.PlayerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv_main;
    ArrayList<String> array_number, array_name, array_address, array_club;
    ProgressDialog progressDialog;

    PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_main);
        rv_main = findViewById(R.id.rv_main);
        progressDialog = new ProgressDialog(this);

        rv_main.hasFixedSize();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        rv_main.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        scrollRefresh();
    }

    public void scrollRefresh() {
        progressDialog.setMessage("mengambil data .... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1200);
    }

    private void intiallizeArray() {
        array_number = new ArrayList<String>();
        array_name = new ArrayList<String>();
        array_address = new ArrayList<String>();
        array_club = new ArrayList<String>();

        array_number.clear();
        array_name.clear();
        array_address.clear();
        array_club.clear();

    }

    public void getData() {
        intiallizeArray();

        AndroidNetworking.get("http://192.168.43.38/Player/getData.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon", "" + ja);
                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jsonObject = ja.getJSONObject(i);

                                    array_number.add(jsonObject.getString("number"));
                                    array_name.add(jsonObject.getString("name"));
                                    array_address.add(jsonObject.getString("address"));
                                    array_club.add(jsonObject.getString("club"));
                                }

                                playerAdapter = new PlayerAdapter(MainActivity.this, array_number, array_name, array_address, array_club);
                                rv_main.setAdapter(playerAdapter);

                            } else {
                                Toast.makeText(MainActivity.this, "gagal mengambil data", Toast.LENGTH_SHORT).show();
                                playerAdapter  = new PlayerAdapter(MainActivity.this, array_number, array_name, array_address, array_club);
                                rv_main.setAdapter(playerAdapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, ActivityAdd.class);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scrollRefresh();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                scrollRefresh();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        }

    }
}