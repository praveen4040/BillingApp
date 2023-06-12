package com.example.billingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class AmountCalculate extends Fragment {


    Button btn;
    EditText total,rate,purity,wpk,weight;


    public AmountCalculate() {
    }
    public static AmountCalculate newInstance(String param1, String param2) {
        AmountCalculate fragment = new AmountCalculate();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_amount_calculate, container, false);

        btn=view.findViewById(R.id.amt_submit);
        total=view.findViewById(R.id.tot_amt);
        rate=view.findViewById(R.id.pro_rate);
        wpk=view.findViewById(R.id.wpk);
        purity=view.findViewById(R.id.purity);
        weight=view.findViewById(R.id.weight);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int billId=0;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("calculatedBill");
                try {
                    billId=query.count();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                double tot=Double.parseDouble(total.getText().toString());
                double rt=Double.parseDouble(rate.getText().toString());
                double pure=Double.parseDouble(purity.getText().toString());
                double wgt=((tot/rt)*100)/pure;
                weight.setText(String.valueOf(wgt));
                double calculate_rate=tot/1.03;
                double wpkg=calculate_rate/wgt;
                wpk.setText(String.valueOf(wpkg));
                Bundle b=new Bundle();
                b.putDouble("weight",wgt);
                b.putDouble("rate",wpkg);
                getParentFragmentManager().setFragmentResult("data1",b);
                ParseObject table=new ParseObject("calculatedBill");
                table.put("billId",++billId);
                table.put("totalAmt",tot);
                table.put("pRate",rt);
                table.put("purity",pure);
                table.put("weight",wgt);
                table.put("cpk",wpkg);
                table.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(getContext(), "Data saved Successfully", Toast.LENGTH_SHORT).show();
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