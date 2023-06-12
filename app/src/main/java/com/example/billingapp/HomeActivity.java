package com.example.billingapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.business_slt:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new business()).commit();
                        break;
                    case R.id.benAdd:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new Beneficiary()).commit();
                        break;
                    case R.id.genBill:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new GenerateBill()).commit();
                        break;
                    case R.id.reports:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new Reports()).commit();
                        break;
                    case R.id.amtCalculate:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new AmountCalculate()).commit();
                        break;

                }

                return false;
            }
        });




    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
}