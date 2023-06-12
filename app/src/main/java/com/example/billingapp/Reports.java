package com.example.billingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Reports extends Fragment {

    private String table_name;

    public Reports() {

    }
    public static Reports newInstance(String param1, String param2) {
        Reports fragment = new Reports();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_reports, container, false);
        RecyclerView reviews=view.findViewById(R.id.reports);
        Button btn=view.findViewById(R.id.get_bills);
        getParentFragmentManager().setFragmentResultListener("db_data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                table_name=result.getString("db").trim();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Report> arr=new ArrayList<Report>();
                ParseQuery<ParseObject> query=ParseQuery.getQuery(table_name);
                query.whereExists("invoice");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        Log.d("object-size", String.valueOf(objects.size()));
                        for(ParseObject item:objects)
                        {
                            int id=item.getInt("invoice");
                            String shopName=item.getString("shopName");
                            String date=item.getString("date");
                            Double tot=item.getDouble("tot");
                            arr.add(new Report(id,tot,date,shopName));
                        }
                        ReportsAdapter rac=new ReportsAdapter(arr,getContext());
                        reviews.setLayoutManager(new LinearLayoutManager(getContext()));
                        reviews.setAdapter(rac);
                    }
                });
            }
        });
        return view;
    }
}