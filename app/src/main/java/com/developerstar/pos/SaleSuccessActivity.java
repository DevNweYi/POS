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

import model.TranSaleModel;

public class SaleSuccessActivity extends AppCompatActivity {

    Button btnPrint,btnBackPos;
    Context context=this;
    int voucherNo,discount,totalAmount,netAmount,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount,customerId;
    List<TranSaleModel> lstSale=new ArrayList<>();
    String customer,payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_success);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i=getIntent();
        totalAmount=i.getIntExtra("TotalAmount",0);
        discount=i.getIntExtra("Discount",0);
        netAmount=i.getIntExtra("NetAmount",0);
        lastDebtAmount=i.getIntExtra("LastDebtAmount",0);
        paidAmount=i.getIntExtra("PaidAmount",0);
        changeAmount=i.getIntExtra("ChangeAmount",0);
        debtAmount=i.getIntExtra("DebtAmount",0);
        isDebtAmount=i.getIntExtra("IsDebtAmount",0);
        voucherNo=i.getIntExtra("VoucherNo",0);
        customerId=i.getIntExtra("CustomerID",0);
        customer=i.getStringExtra("CustomerName");
        payType=i.getStringExtra("PayType");
        lstSale = (List<TranSaleModel>) i.getSerializableExtra("LstTranSale");

        btnBackPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PrintBillActivity.class);
                i.putExtra("TotalAmount", totalAmount);
                i.putExtra("Discount",discount );
                i.putExtra("NetAmount", netAmount);
                i.putExtra("LastDebtAmount", lastDebtAmount);
                i.putExtra("PaidAmount", paidAmount);
                i.putExtra("ChangeAmount", changeAmount);
                i.putExtra("DebtAmount", debtAmount);
                i.putExtra("IsDebtAmount", isDebtAmount);
                i.putExtra("PayType",payType);
                i.putExtra("VoucherNo", voucherNo);
                i.putExtra("CustomerID", customerId);
                i.putExtra("CustomerName", customer);
                i.putExtra("LstSaleProduct",(Serializable) lstSale);
                i.putExtra("IsSaleVoucher", true);
                startActivity(i);
                finish();
            }
        });
    }

    private void setLayoutResource(){
        btnPrint=findViewById(R.id.btnPrint);
        btnBackPos=findViewById(R.id.btnBackPos);
    }
}
