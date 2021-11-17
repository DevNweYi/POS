package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.OpenBillAdapter;
import database.DatabaseAccess;
import listener.OpenBillListener;
import model.MasterSaleModel;

public class OpenBillActivity extends AppCompatActivity implements OpenBillListener{

    ListView lvOpenBillList;
    DatabaseAccess db;
    Context context=this;
    OpenBillAdapter openBillAdapter;
    List<MasterSaleModel> lstMasterSale=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_bill);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_opened_bill);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMasterOpenBill();
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

    @Override
    public void onAddToPOSClickListener(int position){
        db.setTranOpenBillToTemp(lstMasterSale.get(position).getOpenBillId());
        Intent i=new Intent(context,SaleActivity.class);
        //i.putExtra("TotalAmount", lstMasterSale.get(position).getTotalAmount());
        i.putExtra("PayDisPercent", lstMasterSale.get(position).getPayDisPercent());
        i.putExtra("PayDisAmount", lstMasterSale.get(position).getPayDisAmount());
        //i.putExtra("TotalDisAmount", lstMasterSale.get(position).getTotalDisAmount());
        //i.putExtra("NetAmount", lstMasterSale.get(position).getNetAmount());
        i.putExtra("CustomerID", lstMasterSale.get(position).getCustomerId());
        i.putExtra("VoucherNo", lstMasterSale.get(position).getVoucherNumber());
        i.putExtra("CustomerName", lstMasterSale.get(position).getCustomerName());
        i.putExtra("FromOpenBill", true);
        startActivity(i);
    }

    @Override
    public void onDetailClickListener(int position){
        Intent i=new Intent(context,OpenBillDetailActivity.class);
        i.putExtra("OpenBillID",lstMasterSale.get(position).getOpenBillId());
        i.putExtra("VoucherNumber",lstMasterSale.get(position).getVoucherNumber());
        i.putExtra("UserName",lstMasterSale.get(position).getUserName());
        i.putExtra("CustomerName",lstMasterSale.get(position).getCustomerName());
        i.putExtra("SaleDate",lstMasterSale.get(position).getDate());
        i.putExtra("SaleTime",lstMasterSale.get(position).getTime());
        i.putExtra("TotalAmount",lstMasterSale.get(position).getTotalAmount());
        i.putExtra("PayDisPercent",lstMasterSale.get(position).getPayDisPercent());
        i.putExtra("DiscountAmount",lstMasterSale.get(position).getTotalDisAmount());
        i.putExtra("NetAmount",lstMasterSale.get(position).getNetAmount());
        i.putExtra("Remark",lstMasterSale.get(position).getOpenBillRemark());
        startActivity(i);
    }

    @Override
    public void onDeleteClickListener(int position){
        int openBillId =lstMasterSale.get(position).getOpenBillId();
        int voucherNumber =lstMasterSale.get(position).getVoucherNumber();
        showConfirmDialog(openBillId,voucherNumber);
    }

    private void getMasterOpenBill(){
        lstMasterSale = db.getMasterOpenBill();
        openBillAdapter = new OpenBillAdapter(this, lstMasterSale);
        lvOpenBillList.setAdapter(openBillAdapter);
        openBillAdapter.setOnEventListener(this);
    }

    private void showConfirmDialog(final int openBillId,final int voucherNumber){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_confirm, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final TextView tvConfirmMessage=v.findViewById(R.id.tvConfirmMessage);

        tvConfirmMessage.setText(getResources().getString(R.string.delete_confirm_message));

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                db.deleteOpenBill(openBillId);
                lstMasterSale = db.getMasterOpenBill();
                openBillAdapter.updateResults(lstMasterSale);
                Toast.makeText(context,"Voucher No. " + voucherNumber + " " + getResources().getString(R.string.deleted_open_bill),Toast.LENGTH_SHORT).show();
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        lvOpenBillList=findViewById(R.id.lvOpenBillList);
    }
}
