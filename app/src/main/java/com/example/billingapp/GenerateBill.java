package com.example.billingapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GenerateBill extends Fragment{


    private static final int PERMISSION_REQUEST_CODE = 200;
    EditText type,shop,bamt,aamt,sgst,cgst,wgt,rate,stringp,date,inv_id;
    ImageView bill,shop_list,cal,idImage;
    Button submit,find,update,delete;
    String table_name,address,gst;
    int billId=0;
    public GenerateBill() {

    }
    public static GenerateBill newInstance() {
        GenerateBill fragment = new GenerateBill();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_generate_bill, container, false);


        inv_id=view.findViewById(R.id.invoice_id);
        bill=view.findViewById(R.id.bill_type);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        type=view.findViewById(R.id.type);
        shop_list=view.findViewById(R.id.shop_list);
        aamt=view.findViewById(R.id.aamt);
        bamt=view.findViewById(R.id.bamt);
        wgt=view.findViewById(R.id.wgt);
        stringp=view.findViewById(R.id.stringp);
        sgst=view.findViewById(R.id.sgst);
        cgst=view.findViewById(R.id.cgst);
        idImage=view.findViewById(R.id.idImage);
        shop=view.findViewById(R.id.shop);
        rate=view.findViewById(R.id.rate);
        date=view.findViewById(R.id.date);
        cal=view.findViewById(R.id.calendar);
        Double bt=0.0;
        getParentFragmentManager().setFragmentResultListener("db_data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                table_name=result.getString("db").trim();
                Log.d("database",table_name);
            }
        });

        idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery(table_name);
                query1.whereExists("invoice");
                query1.orderByDescending("invoice").setLimit(1);
                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        billId=object.getInt("invoice");
                        inv_id.setText(String.valueOf(++billId));
                    }
                });

            }
        });

        getParentFragmentManager().setFragmentResultListener("data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                date.setText(result.getString("date"));
            }
        });

        getParentFragmentManager().setFragmentResultListener("data1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Double wt=result.getDouble("weight");
                Double wgpk=result.getDouble("rate");
                wgt.setText(String.format("%.2f",wt));
                rate.setText(String.format("%.2f",wgpk));
                Double bt=wt*wgpk;
                bamt.setText(String.valueOf(bt));
                Double gst=1.5*bt/100;
                sgst.setText(String.format("%.2f",gst));
                cgst.setText(String.format("%.2f",gst));
                Double total_amount=bt+2*(gst);
                String total=String.format("%.2f",total_amount);
                aamt.setText(total);
                Log.d("total", total);
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.setTargetFragment(GenerateBill.this,0);
                datepicker.show(fm,"date picker");
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(getContext(),bill);
                popupMenu.getMenu().add("Credit Bill");
                popupMenu.getMenu().add("Cash Bill");


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                         String bill_type=menuItem.getTitle().toString();
                         type.setText(bill_type);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        find=view.findViewById(R.id.amt_find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int billId=Integer.parseInt(inv_id.getText().toString());
                ParseQuery<ParseObject> q=ParseQuery.getQuery(table_name);
                q.whereEqualTo("invoice",billId);
                q.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(e==null)
                        {
                            type.setText(object.getString("bill_type"));
                            date.setText(object.getString("date"));
                            shop.setText(object.getString("shopName"));
                            stringp.setText(object.getString("desc"));
                            wgt.setText(String.valueOf(object.getDouble("weight")));
                            rate.setText(String.valueOf(object.getDouble("rate")));
                            bamt.setText(String.valueOf(object.getDouble("amount")));
                            cgst.setText(String.valueOf(object.getDouble("cgst")));
                            sgst.setText(String.valueOf(object.getDouble("sgst")));
                            aamt.setText(String.valueOf(object.getDouble("tot")));

                        }
                    }
                });
            }
        });
        update=view.findViewById(R.id.amt_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double inv=Double.parseDouble(inv_id.getText().toString());
                ParseQuery<ParseObject> q=ParseQuery.getQuery(table_name);
                q.whereEqualTo("invoice",inv);
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
                                        object.put("invoice",Integer.parseInt(inv_id.getText().toString()));
                                        object.put("date",date.getText().toString());
                                        object.put("bill_type",type.getText().toString());
                                        object.put("shopName",shop.getText().toString());
                                        object.put("desc",stringp.getText().toString());
                                        object.put("weight",Double.parseDouble(wgt.getText().toString()));
                                        object.put("rate",Double.parseDouble(rate.getText().toString()));
                                        object.put("amount",Double.parseDouble(bamt.getText().toString()));
                                        object.put("cgst",Double.parseDouble(cgst.getText().toString()));
                                        object.put("sgst",Double.parseDouble(sgst.getText().toString()));
                                        object.put("tot",Double.parseDouble(aamt.getText().toString()));


                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    createPdf(table_name);
                                                    Toast.makeText(getContext(), "Bill Updated successfully", Toast.LENGTH_SHORT).show();
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

        delete=view.findViewById(R.id.amt_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> q=ParseQuery.getQuery(table_name);
                q.whereEqualTo("invoice",Double.parseDouble(inv_id.getText().toString()));
                q.whereEqualTo("date",date.getText().toString());
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

        PopupMenu shopList=new PopupMenu(getContext(),shop_list);
        ParseQuery<ParseObject> query= ParseQuery.getQuery("customer");
        query.whereExists("shopName");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    for(ParseObject item:objects)
                    {
                        shopList.getMenu().add(item.getString("shopName"));
                        Log.d("shops",item.getString("shopName"));
                    }
                }
                else{
                    Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        shopList.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String selected_shop=menuItem.getTitle().toString();
                shop.setText(selected_shop);
                ParseQuery<ParseObject> query=ParseQuery.getQuery("customer");
                query.whereEqualTo("shopName",selected_shop);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(e==null){
                            address =object.getString("address");
                            gst=object.getString("gst");
                        }
                    }
                });

                return true;
            }
        });


        shop_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopList.show();
            }
        });

        submit=view.findViewById(R.id.amt_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseObject obj=new ParseObject(table_name);
                obj.put("invoice",Integer.parseInt(inv_id.getText().toString()));
                obj.put("date",date.getText().toString());
                obj.put("bill_type",type.getText().toString());
                obj.put("shopName",shop.getText().toString());
                obj.put("desc",stringp.getText().toString());
                obj.put("weight",Double.parseDouble(wgt.getText().toString()));
                obj.put("rate",Double.parseDouble(rate.getText().toString()));
                obj.put("amount",Double.parseDouble(bamt.getText().toString()));
                obj.put("cgst",Double.parseDouble(cgst.getText().toString()));
                obj.put("sgst",Double.parseDouble(sgst.getText().toString()));
                obj.put("tot",Double.parseDouble(aamt.getText().toString()));
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                            Toast.makeText(getContext(), "Bill saved Successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                createPdf(table_name);
            }
        });
        return view;
    }



    private void createPdf(String db)
    {
        if (checkPermission()) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        String directory_path;
        String  bank,branch,ac,ifcs,name,gs;
        Bitmap icon;
        if(db=="srj")
        {
            directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/srj/";
             name="SRI RAMAJAYAM JEWELLERS";
             bank="State Bank of India";
             branch="Ammapet,Salem";
             ac="31910930827";
             ifcs="SBIN0012772";
             gs="33AVPPB5229E1ZU";
        }
        else{
            directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/dt/";
            name="DHANALAKSHMI TRADERS";
            bank="Karur Vysya Bank";
            branch="Salem Main Branch";
            ac="1186135000015963";
            ifcs="KVBL0001186";
            gs="33GFNPD9269E1Z5";
        }

        PdfDocument mypdf=new PdfDocument();
        Paint paint=new Paint();
        PdfDocument.PageInfo myinfo=new PdfDocument.PageInfo.Builder(700,1000,1).create();
        PdfDocument.Page mypage=mypdf.startPage(myinfo);
        Canvas c=mypage.getCanvas();
        icon= BitmapFactory.decodeResource(getResources(), R.drawable.bill_icon1);
        c.drawLine(40,60,650,60,paint);
        c.drawLine(650,60,650,950,paint);
        c.drawLine(40,950,650,950,paint);
        c.drawLine(40,60,40,950,paint);
        c.drawBitmap(icon,50,70,paint);
        Paint title=new Paint();
        title.setColor(Color.BLACK);
        title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        title.setTextSize(30f);

        c.drawText(name,230,100,title);
        c.drawText(type.getText().toString(),350,180,paint);
        title.setTextSize(15f);
        c.drawText("Fancy Silver Chains manufactures and order suppliers".toUpperCase(Locale.ROOT),200,120,title);
        paint.setColor(Color.BLACK);
        paint.setTextSize(15f);
        c.drawText("289,Puthumariamman Kovil Street,Anna nagar,4th cross",220,140,paint);
        c.drawText("Ponnammapet,Salem-636001",300,160,paint);
        c.drawText("Cell: 9789702462",520,60,title);
        c.drawText(gs,500,180,paint);
        c.drawLine(50,200,650,200,paint);
        title.setTextSize(15f);
        c.drawText("Invoice Id: ",70,220,title);
        c.drawText(inv_id.getText().toString(),200,220,paint);
        c.drawText("To: ",70,240,title);
        String ad[]=address.split(",");
        int j=240;
        c.drawText("Date",370,j,paint);
        c.drawText(date.getText().toString(),500,j,paint);
        for(int i=0;i<ad.length;i++)
        {
            c.drawText(ad[i],200,j,paint);
            j+=20;
        }
        c.drawText("Buyer GSTIN: ",370,j,paint);
        c.drawText(gst,500,j,paint);
        c.drawLine(50,j+20,650,j+20,paint);
        c.drawText("Particulars",70,j+40,paint);
        c.drawText("HSN CODE",190,j+40,paint);
        c.drawText("Weight (kg)",300,j+40,paint);
        c.drawText("Rate",400,j+40,paint);
        c.drawText("Amount",500,j+40,paint);
        c.drawLine(50,j+60,650,j+60,paint);
        c.drawText("LEG CHAIN",70,j+80,paint);
        c.drawText("7113",190,j+80,paint);
        c.drawText(wgt.getText().toString(),300,j+80,paint);
        c.drawText(rate.getText().toString(),400,j+80,paint);
        c.drawText(bamt.getText().toString(),500,j+80,paint);
        c.drawLine(50,j+100,650,j+100,paint);

        c.drawText("Bank Details",70,580,title);
        c.drawText("Bank Name: ",70,600,paint);
        c.drawText(bank,200,600,paint);
        c.drawText("Bank Branch: ",70,620,paint);
        c.drawText(branch,200,620,paint);
        c.drawText("A/c No:  ",70,640,paint);
        c.drawText(ac,200,640,paint);
        c.drawText("IFSC Code:  ",70,660,paint);
        c.drawText(ifcs,200,660,paint);
        c.drawText("Total Rs: ",450,600,paint);
        Double bmt=Double.parseDouble(bamt.getText().toString());
        c.drawText(String.format("%.2f",bmt),550,600,paint);
        c.drawText("CGST : ",450,620,paint);
        c.drawText(cgst.getText().toString(),550,620,paint);
        c.drawText("SGST : ",450,640,paint);
        c.drawText(sgst.getText().toString(),550,640,paint);
        c.drawText("G.Total Rs: ",450,660,paint);
        c.drawText(aamt.getText().toString(),550,660,paint);

        c.drawText("For "+name,400,700,paint);
        c.drawText("Authorised Signatory",450,760,paint);


        mypdf.finishPage(mypage);
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+inv_id.getText().toString()+".pdf";
        File filePath = new File(targetPdf);
        try {
            mypdf.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }



        mypdf.close();

    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{"WRITE_EXTERNAL_STORAGE", "READ_EXTERNAL_STORAGE"}, PERMISSION_REQUEST_CODE);
    }


}