package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.CustomerOnlyAdapter;
import adapter.PosAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.CustomerModel;
import model.TranSaleModel;

public class SaleActivity extends AppCompatActivity {

    TextView tvVoucherNo,tvCustomer,tvTotalAmt,tvDiscountValue,tvDiscount,tvNetAmt;
    Button btnAddSaleItem,btnPay,btnPayDiscount,btnHold;
    ImageButton btnRefresh,btnHideHiddenAmt,btnShowHiddenAmt,btnOption,btnBottomSheetClose;
    ListView lvProduct;
    TableRow trTotalAmt,trDiscount;
    LinearLayout bottom_sheet;
    EditText etDisPercent,etDisAmount;
    DatabaseAccess db;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();
    List<CustomerModel> lstCustomer=new ArrayList<>();
    List<TranSaleModel> lstTranSale=new ArrayList<>();
    int selectedCustomerID,disPercent,disAmount,paidAmount,changeAmount,debtAmount,isDebtAmount;
    BottomSheetBehavior sheetBehavior;
    boolean isFinishCalculate,isDiscountPercent,fromOpenBill;
    PosAdapter posAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        getCustomerWithDefault();
        Intent i=getIntent();
        fromOpenBill=i.getBooleanExtra("FromOpenBill",false);
        if(fromOpenBill){
            //tvTotalAmt.setText(String.valueOf(systemInfo.df.format(i.getIntExtra("TotalAmount",0))));
            disPercent=i.getIntExtra("PayDisPercent",0);
            disAmount=i.getIntExtra("PayDisAmount",0);
            if(disPercent!=0) {
                isDiscountPercent=true;
                tvDiscountValue.setText("(" + disPercent + "%)");
            }
            //tvDiscount.setText(String.valueOf(systemInfo.df.format(i.getIntExtra("TotalDisAmount",0))));
            //tvNetAmt.setText(String.valueOf(systemInfo.df.format(i.getIntExtra("NetAmount",0))));
            selectedCustomerID=i.getIntExtra("CustomerID",0);
            tvVoucherNo.setText(String.valueOf(i.getIntExtra("VoucherNo",0)));
            tvCustomer.setText(i.getStringExtra("CustomerName"));
            btnHold.setEnabled(false);
        }else{
            db.deleteSaleTemp();
        }

        sheetBehavior=BottomSheetBehavior.from(bottom_sheet);

        tvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerDialog();
            }
        });
        btnAddSaleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AddSaleProductActivity.class);
                startActivity(i);
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lstTranSale.size()==0)return;
                if(selectedCustomerID!=0) showAmountDialog(Integer.parseInt(systemInfo.convertTSValToNormal(tvNetAmt.getText().toString())),db.getDebtAmountByCustomer(selectedCustomerID));
                else payment(0,0,0,0,"");
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCurrentSale();
            }
        });
        btnHideHiddenAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trTotalAmt.setVisibility(View.GONE);
                trDiscount.setVisibility(View.GONE);
                btnShowHiddenAmt.setVisibility(View.VISIBLE);
            }
        });
        btnShowHiddenAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trTotalAmt.setVisibility(View.VISIBLE);
                trDiscount.setVisibility(View.VISIBLE);
                btnShowHiddenAmt.setVisibility(View.GONE);
            }
        });
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        btnBottomSheetClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        btnPayDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.getSaleItemTemp().size() == 0) return;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDiscountDialog();
            }
        });
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.getSaleItemTemp().size() == 0) return;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showConfirmHoldDialog();
            }
        });
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditQuantityDialog(lstTranSale.get(i).getProductName(),lstTranSale.get(i).getUnitKeyword(),lstTranSale.get(i).getQuantity(),i,lstTranSale.get(i).getProductId(),lstTranSale.get(i).getUnitId());
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDialog(i);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db.isExistTranSaleTemp() == 0) {
            getSaleVoucherNumber();
            clearCurrentSale();
        } else {
            lstTranSale = db.getSaleItemTemp();
            posAdapter = new PosAdapter(this, lstTranSale);
            lvProduct.setAdapter(posAdapter);
            calculateTotalAmount();
            calculateDisAndNetAmount();
        }

        if(!fromOpenBill)btnHold.setEnabled(true);
    }

    private void calculateTotalAmount(){
        int totalAmount=0;
        for(int i=0;i<lstTranSale.size();i++){
            totalAmount+=lstTranSale.get(i).getAmount();
        }
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
    }

    private void calculateDisAndNetAmount(){
        int discount;
        int totalAmount=Integer.parseInt(systemInfo.convertTSValToNormal(tvTotalAmt.getText().toString()));
        if(isDiscountPercent)discount=(totalAmount * disPercent) / 100;
        else discount=disAmount;

        if(discount > totalAmount){
            Toast.makeText(context,R.string.discount_greater_total,Toast.LENGTH_SHORT).show();
            return;
        }

        int netAmount = totalAmount-discount;

        tvDiscount.setText(String.valueOf(systemInfo.df.format(discount)));
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));
    }

    private void payment(int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,String creditRemark){
        int isCredit=0;
        int voucherNo=Integer.parseInt(tvVoucherNo.getText().toString());
        String customerName=tvCustomer.getText().toString();
        int totalAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvTotalAmt.getText().toString()));
        int totalDisAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvDiscount.getText().toString()));
        int netAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvNetAmt.getText().toString()));
//        if(chkCreditSale.isChecked()) isCredit=1;

        db.insertSale(voucherNo,systemInfo.getTodayDate(),systemInfo.getCurrentTime(),LoginActivity.UserID,LoginActivity.UserName,selectedCustomerID,customerName,totalAmt,disPercent,disAmount,totalDisAmt,netAmt,isCredit,creditRemark,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount);
        //db.insertSale(voucherNo,"01-06-2021",systemInfo.getCurrentTime(),LoginActivity.UserID,LoginActivity.UserName,selectedCustomerID,customerName,totalAmt,disPercent,disAmount,totalDisAmt,netAmt,isCredit,creditRemark,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount);
        if(selectedCustomerID!=0)db.updateDebtAmountByCustomer(selectedCustomerID,debtAmount);
        db.deleteSaleTemp();
        db.deleteOpenBillByVoucher(voucherNo);
        if(!fromOpenBill)db.increaseSaleVoucherNumber();
        fromOpenBill=false;
        Intent i=new Intent(context,SaleSuccessActivity.class);
        i.putExtra("TotalAmount", totalAmt);
        i.putExtra("Discount",totalDisAmt );
        i.putExtra("NetAmount", netAmt);
        i.putExtra("LastDebtAmount", lastDebtAmount);
        i.putExtra("PaidAmount", paidAmount);
        i.putExtra("ChangeAmount", changeAmount);
        i.putExtra("DebtAmount", debtAmount);
        i.putExtra("IsDebtAmount", isDebtAmount);
        if(isCredit==1) i.putExtra("PayType", "Credit");
        else if(isCredit==0)i.putExtra("PayType","Cash");
        i.putExtra("VoucherNo", voucherNo);
        i.putExtra("CustomerID",selectedCustomerID);
        i.putExtra("CustomerName", customerName);
        i.putExtra("LstTranSale",(Serializable) lstTranSale);
        startActivity(i);
        //finish();
    }

    private void clearCurrentSale(){
        selectedCustomerID=0;
        tvCustomer.setText(lstCustomer.get(0).getCustomerName());
        tvTotalAmt.setText("0");
        tvDiscount.setText("0");
        tvDiscountValue.setText("");
        tvNetAmt.setText("0");
        db.deleteSaleTemp();
        disPercent=0;
        disAmount=0;
        lstTranSale = new ArrayList<>();
        posAdapter = new PosAdapter(this, lstTranSale);
        lvProduct.setAdapter(posAdapter);
    }

    private void getCustomerWithDefault(){
        lstCustomer = db.getCustomerWithDefault();
        if(lstCustomer.size()!=0){
            selectedCustomerID=lstCustomer.get(0).getCustomerId();
            tvCustomer.setText(lstCustomer.get(0).getCustomerName());
        }
    }

    private void getSaleVoucherNumber(){
        int saleVoucherNumber= db.getSaleVoucherNumber();
        tvVoucherNo.setText(String.valueOf(saleVoucherNumber));
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

        tvName.setText(tvCustomer.getText().toString());
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

    private void showEditQuantityDialog(String productName, String unitKeyword, int quantity, final int currentPosition, final int productId, final int unitId){
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
        tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemInfo.showCalcDialog(tvQuantity,null,context,systemInfo.calcQtyEditStatus,null,0,null,null);
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
//                int totalQuantity=0;
//                totalAmount=0;
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if(quantity!=0) {
//                    lstSaleProduct.get(currentPosition).setPosQuantity(quantity);
                    lstTranSale.get(currentPosition).setQuantity(quantity);
                    lstTranSale.get(currentPosition).setAmount(quantity*lstTranSale.get(currentPosition).getSalePrice());
                    db.updateSaleItemTempQty(productId,quantity,unitId,quantity*lstTranSale.get(currentPosition).getSalePrice());
                    /*if(fromOpenBill){
                        db.updateMasterOpenBillAmt(voucherNo,totalAmount);
                        db.updateTranOpenBillQty(productId,quantity,unitId,quantity*lstTranSale.get(currentPosition).getSalePrice());
                    }*/

                    posAdapter = new PosAdapter(context, lstTranSale);
                    lvProduct.setAdapter(posAdapter);

                    calculateTotalAmount();
                    calculateDisAndNetAmount();
                }
               /* else {
                    lstTranSale.remove(currentPosition);
                    if(!fromOpenBill)db.deleteSaleItemTemp(productId,unitId);
                    else db.deleteTranOpenBill(productId,unitId);
                }*/

                /*for(int i=0;i<lstSaleProduct.size();i++){
                    totalAmount+=lstSaleProduct.get(i).getPosQuantity()*lstSaleProduct.get(i).getPosSalePrice();
                    totalQuantity+=lstSaleProduct.get(i).getPosQuantity();
                }*/
                /*if(!fromOpenBill)db.updateMasterSaleTempQtyAndAmt(totalQuantity,totalAmount);
                else db.updateMasterOpenBillAmt(voucherNo,totalAmount);
                tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount)));
                calculateDiscount();*/
                //if(fromOpenBill)db.updateMasterOpenBillAmt(voucherNo,totalAmount);

                showDialog.dismiss();
            }
        });
    }

    private void showDiscountDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_pay_discount, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        etDisPercent=v.findViewById(R.id.etDisPercent);
        etDisAmount=v.findViewById(R.id.etDisAmount);
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
                //int totalAmount=Integer.parseInt(systemInfo.convertTSValToNormal(tvTotalAmt.getText().toString()));
                /*if(isDiscountPercent)etDisAmount.setText("");
                else etDisPercent.setText("");
                if(etDisPercent.getText().toString().length() == 0) disPercent = 0;
                else disPercent = Integer.parseInt(etDisPercent.getText().toString());
                if(etDisAmount.getText().toString().length() == 0) disAmount = 0;
                else disAmount = Integer.parseInt(etDisAmount.getText().toString());*/
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
                calculateDisAndNetAmount();
                showDialog.dismiss();

               /* if(isDiscountPercent) {
                    if (disPercent > 100) {
                        Toast.makeText(context, R.string.percent_validate, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                calculateDisAndNetAmount();*/
               /* int disPercentAmount= (totalAmount * disPercent) / 100;
                int totalDisAmount = disAmount + disPercentAmount;

                if(totalDisAmount > totalAmount){
                    Toast.makeText(context,R.string.discount_greater_total,Toast.LENGTH_SHORT).show();
                    return;
                }

                int netAmount = totalAmount-totalDisAmount;

                tvDiscount.setText(String.valueOf(systemInfo.df.format(totalDisAmount)));
                tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount)));*/

                /*if(isDiscountPercent)tvDiscountValue.setText("(" + disPercent + "%)");
                else tvDiscountValue.setText("");*/
                /*if(disPercent != 0 && disAmount != 0) tvDiscountValue.setText("(" + disPercent + "%+" + disAmount + ")");
                else if(disPercent != 0 && disAmount == 0) tvDiscountValue.setText("(" + disPercent + "%)");
                else if(disPercent == 0 && disAmount != 0) tvDiscountValue.setText("(" + disAmount + ")");
                else if(disPercent == 0 && disAmount == 0) tvDiscountValue.setText("");*/
            }
        });
    }

    private void showCustomerDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_general_item, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvGeneralItem=v.findViewById(R.id.lvGeneralItem);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);

        tvProfileTitle.setText(getResources().getString(R.string.sub_customer));
        CustomerOnlyAdapter customerOnlyAdapter=new CustomerOnlyAdapter(context,lstCustomer);
        lvGeneralItem.setAdapter(customerOnlyAdapter);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        lvGeneralItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvCustomer.setText(lstCustomer.get(i).getCustomerName());
                selectedCustomerID=lstCustomer.get(i).getCustomerId();
                showDialog.dismiss();
            }
        });
    }

    private void showConfirmHoldDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_confirm_hold, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etRemark=v.findViewById(R.id.etRemark);
        final TextInputLayout input_remark=v.findViewById(R.id.input_remark);

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
                if(etRemark.getText().toString().length() == 0){
                    input_remark.setError(getResources().getString(R.string.please_enter_value));
                    return;
                }
                int voucherNo=Integer.parseInt(tvVoucherNo.getText().toString());
                int totalAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvTotalAmt.getText().toString()));
                int totalDisAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvDiscount.getText().toString()));
                int netAmt=Integer.parseInt(systemInfo.convertTSValToNormal(tvNetAmt.getText().toString()));
                String customerName=tvCustomer.getText().toString();
                String remark=etRemark.getText().toString();
                db.insertOpenedBill(voucherNo,systemInfo.getTodayDate(),systemInfo.getCurrentTime(),LoginActivity.UserID,LoginActivity.UserName,selectedCustomerID,customerName,totalAmt,remark,disPercent,disAmount,totalDisAmt,netAmt);
                clearCurrentSale();
                db.increaseSaleVoucherNumber();
                tvVoucherNo.setText(String.valueOf(voucherNo+1));
                Toast.makeText(context,R.string.hold_success_message,Toast.LENGTH_SHORT).show();
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
                db.deleteSaleItemTemp(lstTranSale.get(removePosition).getProductId(),lstTranSale.get(removePosition).getUnitId());
                lstTranSale.remove(removePosition);
                posAdapter = new PosAdapter(context, lstTranSale);
                lvProduct.setAdapter(posAdapter);
                calculateTotalAmount();
                calculateDisAndNetAmount();
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        tvDiscountValue=findViewById(R.id.tvDiscountValue);
        tvCustomer=findViewById(R.id.tvCustomer);
        tvVoucherNo=findViewById(R.id.tvVoucherNo);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvNetAmt=findViewById(R.id.tvNetAmt);
        btnAddSaleItem=findViewById(R.id.btnAddSaleItem);
        lvProduct=findViewById(R.id.lvProduct);
        btnPay=findViewById(R.id.btnPay);
        btnRefresh=findViewById(R.id.btnRefresh);
        btnHideHiddenAmt=findViewById(R.id.btnHideHiddenAmt);
        btnShowHiddenAmt=findViewById(R.id.btnShowHiddenAmt);
        btnOption=findViewById(R.id.btnOption);
        trTotalAmt=findViewById(R.id.trTotalAmt);
        trDiscount=findViewById(R.id.trDiscount);
        bottom_sheet=findViewById(R.id.bottom_sheet);
        btnPayDiscount=findViewById(R.id.btnPayDiscount);
        btnHold=findViewById(R.id.btnHold);
        btnBottomSheetClose=findViewById(R.id.btnBottomSheetClose);
    }
}
