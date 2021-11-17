package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataSetupActivity extends AppCompatActivity {

    TextView tvProduct,tvCategory,tvUnit,tvCustomer,tvUser,tvSupplier;
    Button btnNext;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_setup);
        getSupportActionBar().hide();
        setLayoutResource();

        tvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductListActivity.class);
                startActivity(i);
            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,CategoryActivity.class);
                startActivity(i);
            }
        });
        tvUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, UnitActivity.class);
                startActivity(i);
            }
        });
        tvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, CustomerListActivity.class);
                startActivity(i);
            }
        });
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, UserActivity.class);
                startActivity(i);
            }
        });
        tvSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, SupplierListActivity.class);
                startActivity(i);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setLayoutResource(){
        tvProduct=findViewById(R.id.tvProduct);
        tvCategory=findViewById(R.id.tvCategory);
        tvUnit=findViewById(R.id.tvUnit);
        tvCustomer=findViewById(R.id.tvCustomer);
        tvUser=findViewById(R.id.tvItem);
        tvSupplier=findViewById(R.id.tvSupplier);
        btnNext=findViewById(R.id.btnNext);
    }
}
