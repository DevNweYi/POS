package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SetupMenuActivity extends AppCompatActivity {

    TextView tvProduct,tvCategory,tvUnit,tvCustomer,tvUser,tvSupplier;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_menu);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_data_setup);

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
        tvProduct=findViewById(R.id.tvProduct);
        tvCategory=findViewById(R.id.tvCategory);
        tvUnit=findViewById(R.id.tvUnit);
        tvCustomer=findViewById(R.id.tvCustomer);
        tvUser=findViewById(R.id.tvItem);
        tvSupplier=findViewById(R.id.tvSupplier);
    }
}
