package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.ProductModel;

public class PurchaseSuccessActivity extends AppCompatActivity {

    Button btnPrint,btnBackPurchase;
    Context context=this;
    List<ProductModel> lstPurchase=new ArrayList<>();
    String supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_success);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i=getIntent();
        supplier=i.getStringExtra("SupplierName");
        lstPurchase = (List<ProductModel>) i.getSerializableExtra("LstPurchaseProduct");

        btnBackPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PrintBillActivity.class);
                i.putExtra("SupplierName", supplier);
                i.putExtra("LstPurchaseProduct",(Serializable) lstPurchase);
                i.putExtra("IsSaleVoucher", false);
                startActivity(i);
                finish();
            }
        });
    }

    private void setLayoutResource(){
        btnPrint=findViewById(R.id.btnPrint);
        btnBackPurchase=findViewById(R.id.btnBackPurchase);
    }
}
