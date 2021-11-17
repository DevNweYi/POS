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

import adapter.PosAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.TranSaleModel;

public class SaleListDetailActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    TextView tvSalePerson,tvCustomer,tvDateTime,tvTotalAmt,tvDiscount,tvNetAmt,tvCredit,tvCreditRemark,tvLastDebtAmt,tvPaidAmt,tvChangeAmt,tvDebtAmt,tvDiscountLabel;
    ListView lvSaleItem;
    LinearLayout layoutCredit,layoutCustomerAmount,layoutChangeAmt,layoutDebtAmt;
    //SaleTranAdapter saleTranAdapter;
    String userName,customerName,saleDate,saleTime,creditRemark;
    int totalAmount,discountAmount,netAmount,isCredit,id,voucherNumber,customerId,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount,payDisPercent;
    List<TranSaleModel> lstTranSale=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();
    PosAdapter posAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_detail);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();

        Intent i=getIntent();
        voucherNumber=i.getIntExtra("VoucherNumber",0);
        setTitle("#" + voucherNumber);
        id=i.getIntExtra("id",0);
        userName=i.getStringExtra("UserName");
        customerName=i.getStringExtra("CustomerName");
        customerId=i.getIntExtra("CustomerID",0);
        saleDate=i.getStringExtra("SaleDate");
        saleTime=i.getStringExtra("SaleTime");
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

        fillSaleData();

        lvSaleItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditQuantityDialog(lstTranSale.get(i).getProductName(),lstTranSale.get(i).getUnitKeyword(),lstTranSale.get(i).getQuantity(),i,lstTranSale.get(i).getProductId(),lstTranSale.get(i).getUnitId(),lstTranSale.get(i).getId());
            }
        });
        lvSaleItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void fillSaleData(){
        tvSalePerson.setText(getResources().getString(R.string.sale_person) + ": " + userName);
        tvCustomer.setText(getResources().getString(R.string.customer) + ": " + customerName);
        tvDateTime.setText(getResources().getString(R.string.date_time) + ": " + saleDate+" "+saleTime);
        if(creditRemark.length() != 0) tvCreditRemark.setText("("+creditRemark+")");
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvDiscount.setText(String.valueOf(systemInfo.df.format(discountAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));
        if(payDisPercent!=0)tvDiscountLabel.setText(getResources().getString(R.string.discount)+"(" + payDisPercent + "%)");
        if(isCredit == 1)layoutCredit.setVisibility(View.VISIBLE);
        else layoutCredit.setVisibility(View.GONE);

        if(customerId!=0){
            layoutCustomerAmount.setVisibility(View.VISIBLE);
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

        lstTranSale = db.getTranSaleByMaster(id);
        posAdapter = new PosAdapter(this, lstTranSale);
        lvSaleItem.setAdapter(posAdapter);
    }

    private void showEditQuantityDialog(String productName, String unitKeyword,final int quantity, final int currentPosition, final int productId, final int unitId,final int tranSaleId){
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
                    lstTranSale.get(currentPosition).setQuantity(updateQuantity);
                    amount=updateQuantity*lstTranSale.get(currentPosition).getSalePrice();
                    lstTranSale.get(currentPosition).setAmount(amount);
                    db.updateTranSaleQtyAmt(tranSaleId,productId,unitId,quantity,updateQuantity,amount);
                }
                else {
                    lstTranSale.remove(currentPosition);
                    db.deleteTranSale(tranSaleId,productId,unitId);
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
                db.deleteTranSale(lstTranSale.get(removePosition).getId(),lstTranSale.get(removePosition).getProductId(),lstTranSale.get(removePosition).getUnitId());
                lstTranSale.remove(removePosition);
                updateListAndData();
                showDialog.dismiss();
            }
        });
    }

    private void updateListAndData(){
        totalAmount=0;
        posAdapter = new PosAdapter(context, lstTranSale);
        lvSaleItem.setAdapter(posAdapter);

        for(int i=0;i<lstTranSale.size();i++){
            totalAmount+=lstTranSale.get(i).getQuantity()*lstTranSale.get(i).getSalePrice();
        }
        String result=db.updateMasterSaleAmt(voucherNumber,totalAmount,customerId,lastDebtAmount,paidAmount);
        String[] arr=result.split(",");
        discountAmount=Integer.parseInt(arr[0]);
        netAmount=Integer.parseInt(arr[1]);
        isDebtAmount=Integer.parseInt(arr[2]);
        changeAmount=Integer.parseInt(arr[3]);
        debtAmount=Integer.parseInt(arr[4]);

        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvDiscount.setText(String.valueOf(systemInfo.df.format(discountAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));

        if(customerId!=0) {
            if(isDebtAmount==1) tvDebtAmt.setText(String.valueOf(systemInfo.df.format(debtAmount)));
            else tvChangeAmt.setText(String.valueOf(systemInfo.df.format(changeAmount)));
        }
    }

    private void setLayoutResource(){
        tvNetAmt=findViewById(R.id.tvNetAmt);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvSalePerson=findViewById(R.id.tvSalePerson);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvCustomer=findViewById(R.id.tvCustomer);
        tvDateTime=findViewById(R.id.tvDateTime);
        tvCredit=findViewById(R.id.tvCredit);
        tvCreditRemark=findViewById(R.id.tvCreditRemark);
        lvSaleItem=findViewById(R.id.lvSaleItem);
        layoutCredit=findViewById(R.id.layoutCredit);
        layoutCustomerAmount=findViewById(R.id.layoutCustomerAmount);
        layoutChangeAmt=findViewById(R.id.layoutChangeAmt);
        layoutDebtAmt=findViewById(R.id.layoutDebtAmt);
        tvLastDebtAmt=findViewById(R.id.tvLastDebtAmt);
        tvPaidAmt=findViewById(R.id.tvPaidAmt);
        tvChangeAmt=findViewById(R.id.tvChangeAmt);
        tvDebtAmt=findViewById(R.id.tvDebtAmt);
        tvDiscountLabel=findViewById(R.id.tvDiscountLabel);
    }
}
