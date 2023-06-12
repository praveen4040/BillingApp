package com.example.billingapp;

import androidx.appcompat.app.AppCompatActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button login,close;
    TextView uname,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        login=findViewById(R.id.login);
        close=findViewById(R.id.close);
        uname=findViewById(R.id.u_name);
        pwd=findViewById(R.id.p_name);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=uname.getText().toString();
                String pass=pwd.getText().toString();
                if(user.equals(" ") || pass.equals(" "))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Info");
                    builder.setMessage("Please fill all the fields.. ");
                    builder.setCancelable(true);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}