package com.example.billingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class business extends Fragment {

    Button srj,dt;
    public business() {

    }

    public static business newInstance(String param1, String param2) {
        business fragment = new business();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business, container, false);
        srj=view.findViewById(R.id.srj);
        dt=view.findViewById(R.id.dt);
        srj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putString("db","srj");
                getParentFragmentManager().setFragmentResult("db_data",b);
                Log.d("db",b.getString("db").trim());
                Toast.makeText(getContext(), "DB set successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putString("db","dt");
                getParentFragmentManager().setFragmentResult("db_data",b);
            }
        });
        return view;
    }
}