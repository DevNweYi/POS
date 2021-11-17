package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.ProductModel;

public class ProductMenuActivity extends AppCompatActivity {

    TextView tvProductList,tvProductNew,tvTrackProduct,tvProductBalance,tvAdjustProductList,tvTrackProductQty;
    Context context=this;
    DatabaseAccess db;
    List<ProductModel> lstProduct=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_menu);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_product);

        lstProduct = db.getTrackProduct("",0);
        tvTrackProductQty.setText(String.valueOf(lstProduct.size()));

        tvProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductListActivity.class);
                startActivity(i);
            }
        });
        tvProductNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductSetupActivity.class);
                startActivity(i);
            }
        });
        tvProductBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductBalanceActivity.class);
                startActivity(i);
            }
        });
        tvTrackProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductTrackActivity.class);
                startActivity(i);
            }
        });
        tvAdjustProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AdjustmentListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLayoutResource(){
        tvProductList=findViewById(R.id.tvProductList);
        tvProductNew=findViewById(R.id.tvProductNew);
        tvTrackProduct=findViewById(R.id.tvTrackProduct);
        tvProductBalance=findViewById(R.id.tvProductBalance);
        tvAdjustProductList=findViewById(R.id.tvAdjustProductList);
        tvTrackProductQty=findViewById(R.id.tvTrackProductQty);
    }
}
