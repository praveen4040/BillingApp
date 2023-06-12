package com.example.billingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class Beneficiary extends Fragment {

    Button submit,find,update,delete;
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

        find=view.findViewById(R.id.bene_find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=shop.getText().toString();
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("customer");
                query1.whereEqualTo("shopName",name);
                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        address.setText(object.getString("address"));
                        gst.setText(object.getString("gst"));
                        phone.setText(object.getString("phone"));
                    }
                });
            }
        });
        update=view.findViewById(R.id.bene_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=shop.getText().toString();
                ParseQuery<ParseObject> q=ParseQuery.getQuery("customer");
                q.whereEqualTo("shopName",name);
                q.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(e==null)
                        {
                            String objectId=object.getObjectId().toString();
                            q.getInBackground(objectId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if(e==null)
                                    {
                                        long mobile= Long.parseLong(phone.getText().toString());
                                        object.put("shopName",shop.getText().toString());
                                        object.put("address",address.getText().toString());
                                        object.put("gst",gst.getText().toString());
                                        object.put("phone",mobile);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    gst.setText(" ");
                                                    shop.setText(" ");
                                                    phone.setText("");
                                                    address.setText(" ");
                                                    Toast.makeText(getContext(), "Data Updated successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        delete=view.findViewById(R.id.bene_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> q=ParseQuery.getQuery("customer");
                q.whereEqualTo("shopName",shop.getText().toString());
                q.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null)
                        {
                            objects.get(0).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Toast.makeText(getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


        return view;
    }
}