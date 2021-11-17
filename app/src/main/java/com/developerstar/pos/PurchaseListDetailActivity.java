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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.PurchaseTranAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.TranPurchaseModel;

public class PurchaseListDetailActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    TextView tvUser,tvSupplier,tvDateTime,tvTotalAmt,tvDiscount,tvNetAmt,tvCredit,tvCreditRemark,tvLastDebtAmt,tvPaidAmt,tvChangeAmt,tvDebtAmt,tvDiscountLabel;
    ListView lvPurchaseItem;
    LinearLayout layoutCredit,layoutSupplierAmount,layoutChangeAmt,layoutDebtAmt;
    PurchaseTranAdapter purchaseTranAdapter;
    String userName,supplierName,purchaseDate,purchaseTime,creditRemark;
    int totalAmount,discountAmount,netAmount,isCredit,masterPurchaseId,voucherNumber,supplierId,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount,payDisPercent;
    List<TranPurchaseModel> lstTranPurchase=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list_detail);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();

        Intent i=getIntent();
        voucherNumber=i.getIntExtra("VoucherNumber",0);
        setTitle("#" + voucherNumber);
        masterPurchaseId=i.getIntExtra("MasterPurchaseID",0);
        userName=i.getStringExtra("UserName");
        supplierName=i.getStringExtra("SupplierName");
        supplierId=i.getIntExtra("SupplierID",0);
        purchaseDate=i.getStringExtra("PurchaseDate");
        purchaseTime=i.getStringExtra("PurchaseTime");
        creditRemark=i.getStringExtra("CreditRemark");
        totalAmount=i.getIntExtra("TotalAmount",0);
        discountAmount=i.getIntExtra("DiscountAmount",0);
        netAmount=i.getIntExtra("NetAmount",0);
        isCredit=i.getIntExtra("IsCredit",0);
        lastDebtAmount=i.getIntExtra("LastDebtAmount",0);
        paidAmount=i.getIntExtra("PaidAmount",0);
        changeAmount=i.getIntExtra("ChangeAmount",0);
        debtAmount=i.getIntExtra("DebtAmount",0);
        isDebtAmount=i.getIntExtra("IsDebtAmount",0);
        payDisPercent=i.getIntExtra("PayDisPercent",0);

        fillPurchaseData();

        lvPurchaseItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditQuantityDialog(lstTranPurchase.get(i).getProductName(),lstTranPurchase.get(i).getUnitKeyword(),lstTranPurchase.get(i).getQuantity(),i,lstTranPurchase.get(i).getProductId(),lstTranPurchase.get(i).getUnitId(),lstTranPurchase.get(i).getId());
            }
        });
        lvPurchaseItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void fillPurchaseData(){
        tvUser.setText(getResources().getString(R.string.user) + ": " + userName);
        tvSupplier.setText(getResources().getString(R.string.supplier) + ": " + supplierName);
        tvDateTime.setText(getResources().getString(R.string.date_time) + ": " + purchaseDate+" "+purchaseTime);
        if(creditRemark.length() != 0) tvCreditRemark.setText("("+creditRemark+")");
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvDiscount.setText(String.valueOf(systemInfo.df.format(discountAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));
        if(payDisPercent!=0)tvDiscountLabel.setText(getResources().getString(R.string.discount)+"(" + payDisPercent + "%)");
        if(isCredit == 1)layoutCredit.setVisibility(View.VISIBLE);
        else layoutCredit.setVisibility(View.GONE);

        if(supplierId!=0){
            layoutSupplierAmount.setVisibility(View.VISIBLE);
            tvLastDebtAmt.setText(String.valueOf(systemInfo.df.format(lastDebtAmount)));
            tvPaidAmt.setText(String.valueOf(systemInfo.df.format(paidAmount)));
            if(isDebtAmount==1){
                layoutDebtAmt.setVisibility(View.VISIBLE);
                tvDebtAmt.setText(String.valueOf(systemInfo.df.format(debtAmount)));
            }
            else {
                layoutChangeAmt.setVisibility(View.VISIBLE);
                tvChangeAmt.setText(String.valueOf(systemInfo.df.format(changeAmount)));
            }
        }

        lstTranPurchase = db.getTranPurchaseByMaster(masterPurchaseId);
        purchaseTranAdapter = new PurchaseTranAdapter(this, lstTranPurchase);
        lvPurchaseItem.setAdapter(purchaseTranAdapter);
    }

    private void showEditQuantityDialog(String productName, String unitKeyword,final int quantity, final int currentPosition, final int productId, final int unitId, final int tranPurchaseId){
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
                    lstTranPurchase.get(currentPosition).setQuantity(updateQuantity);
                    amount=updateQuantity*lstTranPurchase.get(currentPosition).getPurPrice();
                    lstTranPurchase.get(currentPosition).setAmount(amount);
                    db.updateTranPurchaseQtyAmt(tranPurchaseId,productId,unitId,quantity,updateQuantity,amount);
                }
                else {
                    lstTranPurchase.remove(currentPosition);
                    db.deleteTranPurchase(tranPurchaseId,productId,unitId);
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
                db.deleteTranPurchase(lstTranPurchase.get(removePosition).getId(),lstTranPurchase.get(removePosition).getProductId(),lstTranPurchase.get(removePosition).getUnitId());
                lstTranPurchase.remove(removePosition);
                updateListAndData();
                showDialog.dismiss();
            }
        });
    }

    private void updateListAndData(){
        totalAmount=0;
        purchaseTranAdapter = new PurchaseTranAdapter(context, lstTranPurchase);
        lvPurchaseItem.setAdapter(purchaseTranAdapter);

        for(int i=0;i<lstTranPurchase.size();i++){
            totalAmount+=lstTranPurchase.get(i).getQuantity()*lstTranPurchase.get(i).getPurPrice();
        }
        String result=db.updateMasterPurchaseAmt(voucherNumber,totalAmount,supplierId,lastDebtAmount,paidAmount);
        String[] arr=result.split(",");
        discountAmount=Integer.parseInt(arr[0]);
        netAmount=Integer.parseInt(arr[1]);
        isDebtAmount = Integer.parseInt(arr[2]);
        changeAmount=Integer.parseInt(arr[3]);
        debtAmount=Integer.parseInt(arr[4]);

        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvDiscount.setText(String.valueOf(systemInfo.df.format(discountAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));

        if(supplierId!=0) {
            if(isDebtAmount==1) tvDebtAmt.setText(String.valueOf(systemInfo.df.format(debtAmount)));
            else tvChangeAmt.setText(String.valueOf(systemInfo.df.format(changeAmount)));
        }
    }

    private void setLayoutResource(){
        tvNetAmt=findViewById(R.id.tvNetAmt);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvUser=findViewById(R.id.tvItem);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvSupplier=findViewById(R.id.tvSupplier);
        tvDateTime=findViewById(R.id.tvDateTime);
        tvCredit=findViewById(R.id.tvCredit);
        tvCreditRemark=findViewById(R.id.tvCreditRemark);
        lvPurchaseItem=findViewById(R.id.lvPurchaseItem);
        layoutCredit=findViewById(R.id.layoutCredit);
        layoutSupplierAmount=findViewById(R.id.layoutSupplierAmount);
        layoutChangeAmt=findViewById(R.id.layoutChangeAmt);
        layoutDebtAmt=findViewById(R.id.layoutDebtAmt);
        tvLastDebtAmt=findViewById(R.id.tvLastDebtAmt);
        tvPaidAmt=findViewById(R.id.tvPaidAmt);
        tvChangeAmt=findViewById(R.id.tvChangeAmt);
        tvDebtAmt=findViewById(R.id.tvDebtAmt);
        tvDiscountLabel=findViewById(R.id.tvDiscountLabel);
    }
}
