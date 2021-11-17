package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.AdjustmentTranAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.TranAdjustmentModel;

public class AdjustmentListDetailActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    TextView tvUser,tvDateTime,tvTotalAmt,tvRemark;
    ListView lvAdjustmentItem;
    AdjustmentTranAdapter adjustmentTranAdapter;
    String userName,adjustDate,adjustTime,remark;
    int totalAmount,masterAdjustmentId;
    List<TranAdjustmentModel> lstTranAdjustment=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_list_detail);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();

        Intent i=getIntent();
        masterAdjustmentId=i.getIntExtra("MasterAdjustmentID",0);
        setTitle("damage # " + masterAdjustmentId);
        userName=i.getStringExtra("UserName");
        adjustDate=i.getStringExtra("AdjustDate");
        adjustTime=i.getStringExtra("AdjustTime");
        remark=i.getStringExtra("Remark");
        totalAmount=i.getIntExtra("TotalAmount",0);

        fillAdjustmentData();

        lvAdjustmentItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditQuantityDialog(lstTranAdjustment.get(i).getProductName(),lstTranAdjustment.get(i).getUnitKeyword(),lstTranAdjustment.get(i).getQuantity(),i,lstTranAdjustment.get(i).getProductId(),lstTranAdjustment.get(i).getUnitId(),lstTranAdjustment.get(i).getTranAdjustmentId());
            }
        });
        lvAdjustmentItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDialog(i);
                return true;
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

    private void fillAdjustmentData(){
        tvUser.setText(getResources().getString(R.string.user) + ": " + userName);
        tvDateTime.setText(getResources().getString(R.string.date_time) + ": " + adjustDate+" "+adjustTime);
        if(remark.length() != 0) {
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(remark);
        }
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));

        lstTranAdjustment = db.getTranAdjustmentByMaster(masterAdjustmentId);
        adjustmentTranAdapter = new AdjustmentTranAdapter(this, lstTranAdjustment);
        lvAdjustmentItem.setAdapter(adjustmentTranAdapter);
    }

    private void showEditQuantityDialog(String productName, String unitKeyword,final int quantity, final int currentPosition, final int productId, final int unitId, final int tranAdjustId){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_edit_quantity, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final TextView tvProduct=v.findViewById(R.id.tvProduct);
        final ImageButton btnMinus=v.findViewById(R.id.btnMinus);
        final ImageButton btnPlus=v.findViewById(R.id.btnPlus);
        final TextView tvQuantity=v.findViewById(R.id.tvQuantity);
        final TextView tvUnit=v.findViewById(R.id.tvUnit);

        tvProduct.setText(productName);
        tvQuantity.setText(String.valueOf(quantity));
        if(unitKeyword==null) tvUnit.setText("");
        else tvUnit.setText(unitKeyword);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curQty = Integer.parseInt(tvQuantity.getText().toString());
                if(curQty <= 1) return;
                int decQty = curQty - 1;
                tvQuantity.setText(String.valueOf(decQty));
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curQty = Integer.parseInt(tvQuantity.getText().toString());
                int incQty = curQty + 1;
                tvQuantity.setText(String.valueOf(incQty));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int amount;
                int updateQuantity = Integer.parseInt(tvQuantity.getText().toString());
                if(updateQuantity!=0) {
                    lstTranAdjustment.get(currentPosition).setQuantity(updateQuantity);
                    amount=updateQuantity*lstTranAdjustment.get(currentPosition).getPurPrice();
                    lstTranAdjustment.get(currentPosition).setAmount(amount);
                    db.updateTranAdjustmentQtyAmt(tranAdjustId,productId,unitId,quantity,updateQuantity,amount);
                }
                else {
                    lstTranAdjustment.remove(currentPosition);
                    db.deleteTranAdjustment(tranAdjustId,productId);
                }
                updateListAndData();
                showDialog.dismiss();
            }
        });
    }

    private void showConfirmDialog(final int removePosition){
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
                db.deleteTranAdjustment(lstTranAdjustment.get(removePosition).getTranAdjustmentId(),lstTranAdjustment.get(removePosition).getProductId());
                lstTranAdjustment.remove(removePosition);
                updateListAndData();
                showDialog.dismiss();
            }
        });
    }

    private void updateListAndData(){
        totalAmount=0;
        adjustmentTranAdapter = new AdjustmentTranAdapter(context, lstTranAdjustment);
        lvAdjustmentItem.setAdapter(adjustmentTranAdapter);

        for(int i=0;i<lstTranAdjustment.size();i++){
            totalAmount+=lstTranAdjustment.get(i).getQuantity()*lstTranAdjustment.get(i).getPurPrice();
        }
        db.updateMasterAdjustmentAmt(masterAdjustmentId,totalAmount);

        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
    }

    private void setLayoutResource(){
        tvUser=findViewById(R.id.tvUser);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvDateTime=findViewById(R.id.tvDateTime);
        tvRemark=findViewById(R.id.tvRemark);
        lvAdjustmentItem=findViewById(R.id.lvAdjustmentItem);
    }
}
