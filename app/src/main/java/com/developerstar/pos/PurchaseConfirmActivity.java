package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.PurchaseAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.TranPurchaseModel;

public class PurchaseConfirmActivity extends AppCompatActivity {

    PurchaseAdapter purchaseAdapter;
    TextView tvVoucherNo,tvTotalAmt,tvDiscount,tvNetAmt,tvSupplier,tvDiscountValue;
    ListView lvProduct;
    CheckBox chkCreditPurchase;
    ImageButton btnDiscount;
    Button btnPay;
    DatabaseAccess db;
    Context context=this;
    List<TranPurchaseModel> lstTranPurchase=new ArrayList<>();
    int totalAmount,disPercent,disAmount,supplierId,voucherNo,paidAmount,changeAmount,debtAmount,isDebtAmount;
    String supplierName;
    SystemInfo systemInfo=new SystemInfo();
    boolean isFinishCalculate,isDiscountPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_confirm);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i=getIntent();
        lstTranPurchase = (List<TranPurchaseModel>) i.getSerializableExtra("LstPurchaseProduct");
        supplierId=i.getIntExtra("SupplierID",0);
        voucherNo=i.getIntExtra("VoucherNo",0);
        supplierName=i.getStringExtra("SupplierName");

        fillPurchaseData();

        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiscountDialog();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(supplierId!=0) showAmountDialog(Integer.parseInt(systemInfo.convertTSValToNormal(tvNetAmt.getText().toString())),db.getDebtAmountBySupplier(supplierId));
                else payment(0,0,0,0,"");
            }
        });
        /*chkCreditPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chkCreditPurchase.isChecked())etCreditRemark.setVisibility(View.VISIBLE);
                else etCreditRemark.setVisibility(View.GONE);
            }
        });*/
    }

    private void fillPurchaseData(){
        purchaseAdapter = new PurchaseAdapter(this, lstTranPurchase);
        lvProduct.setAdapter(purchaseAdapter);

        for(int i=0;i<lstTranPurchase.size();i++){
            totalAmount+=lstTranPurchase.get(i).getQuantity()*lstTranPurchase.get(i).getPurPrice();
        }

        tvVoucherNo.setText(String.valueOf(voucherNo));
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
        tvSupplier.setText(getResources().getString(R.string.supplier) + ": " + supplierName);

        /*if(db.isAllowCreditForSupplier(supplierId) == 1)chkCreditPurchase.setVisibility(View.VISIBLE);
        else chkCreditPurchase.setVisibility(View.GONE);*/
    }

    private void showDiscountDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_pay_discount, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etDisPercent=v.findViewById(R.id.etDisPercent);
        final EditText etDisAmount=v.findViewById(R.id.etDisAmount);
        final ImageView imgDisPercent=v.findViewById(R.id.imgDisPercent);
        final ImageView imgDisAmount=v.findViewById(R.id.imgDisAmount);

        if(disPercent==0 && disAmount==0){
            isDiscountPercent=true;
            imgDisPercent.setBackground(context.getResources().getDrawable(R.drawable.bg_yellow_soft_5r));
            imgDisAmount.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
            etDisPercent.setVisibility(View.VISIBLE);
            etDisAmount.setVisibility(View.GONE);
            etDisPercent.setText("");
            etDisAmount.setText("");
        }else{
            if(disPercent != 0) {
                isDiscountPercent=true;
                imgDisPercent.setBackground(context.getResources().getDrawable(R.drawable.bg_yellow_soft_5r));
                imgDisAmount.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
                etDisPercent.setVisibility(View.VISIBLE);
                etDisAmount.setVisibility(View.GONE);
                etDisPercent.setText(String.valueOf(disPercent));
                etDisAmount.setText("");
            }
            else if(disAmount != 0) {
                isDiscountPercent=false;
                imgDisPercent.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
                imgDisAmount.setBackground(context.getResources().getDrawable(R.drawable.bg_yellow_soft_5r));
                etDisPercent.setVisibility(View.GONE);
                etDisAmount.setVisibility(View.VISIBLE);
                etDisAmount.setText(String.valueOf(disAmount));
                etDisPercent.setText("");
            }
        }

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        imgDisPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDiscountPercent=true;
                imgDisPercent.setBackground(context.getResources().getDrawable(R.drawable.bg_yellow_soft_5r));
                imgDisAmount.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
                etDisPercent.setVisibility(View.VISIBLE);
                etDisAmount.setVisibility(View.GONE);
            }
        });
        imgDisAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDiscountPercent=false;
                imgDisPercent.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
                imgDisAmount.setBackground(context.getResources().getDrawable(R.drawable.bg_yellow_soft_5r));
                etDisPercent.setVisibility(View.GONE);
                etDisAmount.setVisibility(View.VISIBLE);
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
               /* if(isDiscountPercent)etDisAmount.setText("");
                else etDisPercent.setText("");
                if(etDisPercent.getText().toString().length() == 0) disPercent = 0;
                else disPercent = Integer.parseInt(etDisPercent.getText().toString());
                if(etDisAmount.getText().toString().length() == 0) disAmount = 0;
                else disAmount = Integer.parseInt(etDisAmount.getText().toString());

                if(disPercent > 100){
                    Toast.makeText(context,R.string.percent_validate,Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(isDiscountPercent){
                    if(etDisPercent.getText().toString().length()==0 || etDisPercent.getText().toString()=="0"){
                        Toast.makeText(context, R.string.please_enter_value, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    disPercent=Integer.parseInt(etDisPercent.getText().toString());
                    if (disPercent > 100) {
                        Toast.makeText(context, R.string.percent_validate, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    disAmount=0;
                    tvDiscountValue.setText("(" + disPercent + "%)");
                }else{
                    if(etDisAmount.getText().toString().length()==0 || etDisAmount.getText().toString()=="0"){
                        Toast.makeText(context, R.string.please_enter_value, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    disPercent=0;
                    disAmount = Integer.parseInt(etDisAmount.getText().toString());
                    tvDiscountValue.setText("");
                }

                int disPercentAmount= (totalAmount * disPercent) / 100;
                int totalDisAmount = disAmount + disPercentAmount;
                if(totalDisAmount > totalAmount){
                    Toast.makeText(context,R.string.discount_greater_total,Toast.LENGTH_SHORT).show();
                    return;
                }
                int netAmount = totalAmount-totalDisAmount;
                tvDiscount.setText(String.valueOf(systemInfo.df.format(totalDisAmount)));
                tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));
                showDialog.dismiss();

               /* if(disPercent != 0 && disAmount != 0) tvDiscountValue.setText("(" + disPercent + "%+" + disAmount + ")");
                else if(disPercent != 0 && disAmount == 0) tvDiscountValue.setText("(" + disPercent + "%)");
                else if(disPercent == 0 && disAmount != 0) tvDiscountValue.setText("(" + disAmount + ")");
                else if(disPercent == 0 && disAmount == 0) tvDiscountValue.setText("");*/

            }
        });
    }

    private void showAmountDialog(final int netAmount, final int lastDebtAmount){
        isFinishCalculate=false;
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_amount, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final Button btnCalculate=v.findViewById(R.id.btnCalculate);
        final EditText etNetAmount=v.findViewById(R.id.etNetAmount);
        final EditText etDebtAmount=v.findViewById(R.id.etDebtAmount);
        final EditText etPaidAmount=v.findViewById(R.id.etPaidAmount);
        final EditText etCreditRemark=v.findViewById(R.id.etCreditRemark);
        final TextView tvDebtAmount=v.findViewById(R.id.tvDebtAmount);
        final TextView tvChangeAmount=v.findViewById(R.id.tvChangeAmount);
        final TextView tvName=v.findViewById(R.id.tvName);
        final LinearLayout layoutPaidAmount=v.findViewById(R.id.layoutPaidAmount);

        tvName.setText(supplierName);
        etNetAmount.setText(String.valueOf(systemInfo.df.format(netAmount)));
        etDebtAmount.setText(String.valueOf(systemInfo.df.format(lastDebtAmount)));

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
                if(isFinishCalculate) {
                    payment(lastDebtAmount, paidAmount, changeAmount, debtAmount, etCreditRemark.getText().toString());
                    showDialog.dismiss();
                }else{
                    layoutPaidAmount.setBackground(getResources().getDrawable(R.drawable.bd_green_5r_3s));
                    Toast.makeText(context,getResources().getString(R.string.please_apply_paid_amount),Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFinishCalculate=true;
                int curAmount=netAmount+lastDebtAmount;
                if(etPaidAmount.getText().toString().trim().length()!=0) paidAmount=Integer.parseInt(etPaidAmount.getText().toString());

                if(paidAmount>=curAmount){
                    tvChangeAmount.setVisibility(View.VISIBLE);
                    tvDebtAmount.setVisibility(View.GONE);
                    changeAmount=paidAmount-curAmount;
                    tvChangeAmount.setText(getResources().getString(R.string.change_amount)+": "+systemInfo.df.format(changeAmount));
                }else{
                    tvChangeAmount.setVisibility(View.GONE);
                    tvDebtAmount.setVisibility(View.VISIBLE);
                    debtAmount=curAmount-paidAmount;
                    tvDebtAmount.setText(getResources().getString(R.string.debt_amount)+": "+systemInfo.df.format(debtAmount));
                    isDebtAmount=1;
                }
            }
        });
    }

    private void payment(int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,String creditRemark){
        int isCredit=0;
        int totalDisAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvDiscount.getText().toString()));
        int netAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvNetAmt.getText().toString()));
       // if(chkCreditPurchase.isChecked()) isCredit=1;

        db.insertPurchase(voucherNo,systemInfo.getTodayDate(),systemInfo.getCurrentTime(),LoginActivity.UserID,LoginActivity.UserName,supplierId,supplierName,totalAmount,disPercent,disAmount,totalDisAmt,netAmt,isCredit,creditRemark,lstTranPurchase,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount);
        if(supplierId!=0)db.updateDebtAmountBySupplier(supplierId,debtAmount);
        db.deletePurchaseItemTemp();
        db.increasePurVoucherNumber();
        Intent i=new Intent(context,PurchaseSuccessActivity.class);
        i.putExtra("SupplierName", supplierName);
        i.putExtra("LstPurchaseProduct",(Serializable) lstTranPurchase);
        startActivity(i);
        finish();
    }

    private void setLayoutResource(){
        tvNetAmt=findViewById(R.id.tvNetAmt);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvVoucherNo=findViewById(R.id.tvVoucherNo);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvSupplier=findViewById(R.id.tvSupplier);
        tvDiscountValue=findViewById(R.id.tvDiscountValue);
//        chkCreditPurchase=findViewById(R.id.chkCreditPurchase);
        lvProduct=findViewById(R.id.lvProduct);
        btnDiscount=findViewById(R.id.btnDiscount);
        btnPay=findViewById(R.id.btnPay);
    }
}
