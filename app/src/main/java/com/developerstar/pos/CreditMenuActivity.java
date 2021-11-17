package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CreditMenuActivity extends AppCompatActivity {

    TextView tvReceivableList,tvPayableList,tvCustomerRepayHistory,tvSupplierRepayHistory;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_menu);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_credit_receipts);

        tvReceivableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ReceivableListActivity.class);
                startActivity(i);
            }
        });
        tvPayableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PayableListActivity.class);
                startActivity(i);
            }
        });
        tvCustomerRepayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,CustomerRepayHistoryActivity.class);
                startActivity(i);
            }
        });
        tvSupplierRepayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SupplierRepayHistoryActivity.class);
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
        tvReceivableList=findViewById(R.id.tvReceivableList);
        tvPayableList=findViewById(R.id.tvPayableList);
        tvCustomerRepayHistory=findViewById(R.id.tvCustomerRepayHistory);
        tvSupplierRepayHistory=findViewById(R.id.tvSupplierRepayHistory);
    }
}
