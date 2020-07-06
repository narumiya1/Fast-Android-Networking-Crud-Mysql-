package com.eijunn.crudmysql.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.eijunn.crudmysql.activities.EditActivity;
import com.eijunn.crudmysql.MainActivity;
import com.eijunn.crudmysql.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    private Context mContext ;
    private ArrayList<String> array_number, array_name, array_address, array_club ;

    ProgressDialog progressDialog ;

    public PlayerAdapter(Context mContext, ArrayList<String> array_number, ArrayList<String> array_name, ArrayList<String> array_address, ArrayList<String> array_club) {
        this.mContext = mContext;
        this.array_number = array_number;
        this.array_name = array_name;
        this.array_address = array_address;
        this.array_club = array_club;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()) ;
        View itemView = layoutInflater.inflate(R.layout.item, parent, false);
        return new PlayerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_number.setText(array_number.get(position));
        holder.tv_name.setText(array_name.get(position));
        holder.tv_address.setText(array_address.get(position));
        holder.tv_club.setText(array_club.get(position));
        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class ) ;
                intent.putExtra("number", array_number.get(position)) ;
                intent.putExtra("name", array_name.get(position)) ;
                intent.putExtra("address", array_address.get(position)) ;
                intent.putExtra("club", array_club.get(position)) ;

                ((MainActivity)mContext).startActivityForResult(intent, 2);
            }
        });

        holder.cv_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder((MainActivity)mContext)
                        .setMessage("ingin menhapus data ini ?")
                        .setCancelable(false)
                        .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("menghapus ....");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                AndroidNetworking.post("http://192.168.43.38/Player/deletePlayer.php")
                                        .addBodyParameter("number",""+array_number.get(position))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();

                                                try {
                                                    Boolean status = response.getBoolean("status");
                                                    Log.d("status",""+status);
                                                    String result = response.getString("result") ;

                                                    if (status) {
                                                        if (mContext instanceof  MainActivity){
                                                            ((MainActivity)mContext).scrollRefresh();
                                                        }
                                                    }else {
                                                        Toast.makeText(mContext, ""+result, Toast.LENGTH_SHORT).show();
                                                    }

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                anError.printStackTrace();

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return array_number.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_number, tv_name, tv_address, tv_club ;
        public CardView cv_main ;

        public MyViewHolder(View itemView) {
            super(itemView);

            cv_main = itemView.findViewById(R.id.cv_main);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address=itemView.findViewById(R.id.tv_address);
            tv_club = itemView.findViewById(R.id.tv_club) ;

            progressDialog = new ProgressDialog(mContext) ;
        }
    }
}
