package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.PosAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.TranSaleModel;

public class OpenBillDetailActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    TextView tvSalePerson,tvCustomer,tvDateTime,tvTotalAmt,tvRemark,tvDiscount,tvNetAmt,tvDiscountLabel;
    ListView lvSaleItem;
    PosAdapter posAdapter;
    String userName,customerName,saleDate,saleTime,remark;
    int totalAmount,openBillId,voucherNumber,payDisPercent,discountAmount,netAmount;
    List<TranSaleModel> lstTranSale=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_bill_detail);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();

        Intent i=getIntent();
        voucherNumber=i.getIntExtra("VoucherNumber",0);
        setTitle("#" + voucherNumber);
        openBillId=i.getIntExtra("OpenBillID",0);
        userName=i.getStringExtra("UserName");
        customerName=i.getStringExtra("CustomerName");
        saleDate=i.getStringExtra("SaleDate");
        saleTime=i.getStringExtra("SaleTime");
        remark=i.getStringExtra("Remark");
        totalAmount=i.getIntExtra("TotalAmount",0);
        payDisPercent=i.getIntExtra("PayDisPercent",0);
        discountAmount=i.getIntExtra("DiscountAmount",0);
        netAmount=i.getIntExtra("NetAmount",0);

        fillSaleData();
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

    private void fillSaleData(){
        tvSalePerson.setText(getResources().getString(R.string.sale_person) + ": " + userName);
        tvCustomer.setText(getResources().getString(R.string.customer) + ": " + customerName);
        tvDateTime.setText(getResources().getString(R.string.date_time) + ": " + saleDate + " " + saleTime);
        tvRemark.setText(remark);
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvDiscount.setText(String.valueOf(systemInfo.df.format(discountAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));
        if(payDisPercent!=0)tvDiscountLabel.setText(getResources().getString(R.string.discount)+"(" + payDisPercent + "%)");

        lstTranSale = db.getTranOpenBillByMaster(openBillId);
        posAdapter = new PosAdapter(this, lstTranSale);
        lvSaleItem.setAdapter(posAdapter);
    }

    private void setLayoutResource(){
        tvSalePerson=findViewById(R.id.tvSalePerson);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvCustomer=findViewById(R.id.tvCustomer);
        tvDateTime=findViewById(R.id.tvDateTime);
        tvRemark=findViewById(R.id.tvRemark);
        lvSaleItem=findViewById(R.id.lvSaleItem);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvNetAmt=findViewById(R.id.tvNetAmt);
        tvDiscountLabel=findViewById(R.id.tvDiscountLabel);

    }
}
