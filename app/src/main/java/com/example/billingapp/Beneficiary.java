package com.example.billingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class Beneficiary extends Fragment {

    Button submit;
    EditText shop,address,gst,phone;

    public Beneficiary() {
    }



    public static Beneficiary newInstance(String param1, String param2) {
        Beneficiary fragment = new Beneficiary();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_beneficiary, container, false);
        submit=view.findViewById(R.id.bene_submit);
        shop=view.findViewById(R.id.shop_name);
        address=view.findViewById(R.id.address);
        gst=view.findViewById(R.id.gst);
        phone=view.findViewById(R.id.phone_no);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long mobile= Long.parseLong(phone.getText().toString());
                ParseObject obj=new ParseObject("customer");
                obj.put("shopName",shop.getText().toString());
                obj.put("address",address.getText().toString());
                obj.put("gst",gst.getText().toString());
                obj.put("phone",mobile);
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            gst.setText(" ");
                            shop.setText(" ");
                            phone.setText("");
                            address.setText(" ");
                            Toast.makeText(getContext(), "Customer Data added..", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}