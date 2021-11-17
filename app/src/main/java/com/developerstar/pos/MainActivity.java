package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import database.DatabaseAccess;

public class MainActivity extends AppCompatActivity {

    Button btnPOS,btnDataSetup,btnSetting,btnReport,btnSale,btnPurchase,btnOpenBill,btnPurchaseList,btnCreditList,btnStock;
    TextView tvShopName,tvLoginUser;
    Context context=this;
    DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        btnPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SaleActivity.class);
                startActivity(i);
            }
        });
        btnDataSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SetupMenuActivity.class);
                startActivity(i);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SettingActivity.class);
                startActivity(i);
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ReportMenuActivity.class);
                startActivity(i);
            }
        });
        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SaleListActivity.class);
                startActivity(i);
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PurchaseActivity.class);
                startActivity(i);
            }
        });
        btnOpenBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,OpenBillActivity.class);
                startActivity(i);
            }
        });
        btnPurchaseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PurchaseListActivity.class);
                startActivity(i);
            }
        });
        btnCreditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,CreditMenuActivity.class);
                startActivity(i);
            }
        });
        btnStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductMenuActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        tvShopName.setText(db.getShopName());
        tvLoginUser.setText(LoginActivity.UserName);
    }

    private void setLayoutResource(){
        btnPOS=findViewById(R.id.btnPOS);
        btnDataSetup=findViewById(R.id.btnDataSetup);
        btnSetting=findViewById(R.id.btnSetting);
        btnReport=findViewById(R.id.btnReport);
        btnSale=findViewById(R.id.btnSale);
        btnPurchase=findViewById(R.id.btnPurchase);
        btnOpenBill=findViewById(R.id.btnOpenBill);
        btnPurchaseList=findViewById(R.id.btnPurchaseList);
        btnCreditList=findViewById(R.id.btnCreditList);
        btnStock=findViewById(R.id.btnStock);
        tvShopName=findViewById(R.id.tvShopName);
        tvLoginUser=findViewById(R.id.tvLoginUser);
    }
}
