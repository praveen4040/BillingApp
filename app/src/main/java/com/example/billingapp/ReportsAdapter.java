package com.example.billingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {
    private ArrayList<Report> reports;
    private Context context;

    public ReportsAdapter(ArrayList<Report> reports,Context context){
        this.reports=reports;
        this.context=context;
    }

    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(ReportsAdapter.ViewHolder holder, int position) {
        Report mac = reports.get(position);
        holder.Invoice.setText(String.valueOf(mac.getId()));
        holder.shopName.setText(mac.getShopName());
        holder.Date.setText(mac.getDate());
        holder.Tot.setText(String.valueOf(mac.getTot()));
    }

    @Override
    public int getItemCount() {
        Log.d("size", String.valueOf(reports.size()));
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Invoice;
        private TextView Date;
        private TextView shopName;
        private TextView Tot;

        public ViewHolder(View inflate) {
            super(inflate);
            Invoice = inflate.findViewById(R.id.inv_id);
            Date = inflate.findViewById(R.id.date);
            shopName = inflate.findViewById(R.id.shopName);
            Tot=inflate.findViewById(R.id.tot);

//            inflate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String date=Date.getText().toString();
//                    String shopName=shopName.getText().toString();
//                    String id=Invoice.getText().toString();
//                    String tot=Tot.getText().toString();
//                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(inflate.getContext(),display.class);
//                    i.putExtra("Mname",name);
//                    i.putExtra("thres",thres);
//                    i.putExtra("loc",loc);
//                    inflate.getContext().startActivity(i);
//                }
//            });
        }
    }
}
