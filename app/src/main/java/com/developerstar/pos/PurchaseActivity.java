package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.ProductUnitAdapter;
import adapter.PurchaseProductAdapter;
import adapter.SpCategoryAdapter;
import adapter.SpProductAdapter;
import adapter.SpSupplierAdapter;
import adapter.SupplierOnlyAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import listener.PurchaseAdjustListener;
import model.CategoryModel;
import model.ProductModel;
import model.ProductUnitModel;
import model.SupplierModel;
import model.TranPurchaseModel;

public class PurchaseActivity extends AppCompatActivity implements PurchaseAdjustListener {

    Spinner spCategory,spProduct,spProductUnit;
    TextView tvVoucherNo,tvProductUnit,tvSupplier;
    ImageButton btnRefresh,btnAdd;
    ListView lvProduct;
    Button btnPay;
    DatabaseAccess db;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();
    SpSupplierAdapter spSupplierAdapter;
    List<SupplierModel> lstSupplier=new ArrayList<>();
    SpCategoryAdapter spCategoryAdapter;
    List<CategoryModel> lstCategory=new ArrayList<>();
    SpProductAdapter spProductAdapter;
    List<ProductModel> lstProduct=new ArrayList<>();
    PurchaseProductAdapter purchaseProductAdapter;
    List<TranPurchaseModel> lstTranPurchase=new ArrayList<>();
    int editStatus, quantityEditStatus =1, priceEditStatus =2;
//    SpProductUnitAdapter spProductUnitAdapter;
    int selectedProductPosition,selectedProductUnitPosition,selectedProductUnitID,selectedSupplierID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        getSupplier();
        db.deletePurchaseItemTemp();

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int catId = lstCategory.get(i).getCategoryId();
                getProduct(catId,"",false);
//                tvProductUnit.setText(lstProduct.get(0).getSelectUnitKeyword());
                /*spProductUnitAdapter=new SpProductUnitAdapter(context,lstProduct.get(0).getLstProductUnit());
                spProductUnit.setAdapter(spProductUnitAdapter);*/
                selectedProductPosition=0;
                tvProductUnit.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)return;
                tvProductUnit.setText(lstProduct.get(i).getSelectUnitKeyword());
                selectedProductUnitID=lstProduct.get(i).getSelectUnitId();
               /* lstProduct.get(selectedProductPosition).setSelectUnitId(lstProduct.get(i).getSelectUnitId());
                lstProduct.get(selectedProductPosition).setSelectUnitKeyword(lstProduct.get(i).getSelectUnitKeyword());
                lstProduct.get(selectedProductPosition).setUnitType(lstProduct.get(i).getUnitType());
                lstProduct.get(selectedProductPosition).setPurPrice(lstProduct.get(i).getPurPrice());*/
//                selectedProductUnitPosition=0;
                /*spProductUnitAdapter=new SpProductUnitAdapter(context,lstProduct.get(i).getLstProductUnit());
                spProductUnit.setAdapter(spProductUnitAdapter);*/
                selectedProductPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       /* spProductUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProductUnitPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSupplierID==0){
                    Toast.makeText(context,getResources().getString(R.string.please_choose_supplier),Toast.LENGTH_LONG).show();
                    return;
                }
                if(lstTranPurchase.size()==0)return;
                int voucherNo=Integer.parseInt(tvVoucherNo.getText().toString());
                for(int j=0;j<lstTranPurchase.size();j++){
                    int productId=lstTranPurchase.get(j).getProductId();
                    String productName=lstTranPurchase.get(j).getProductName();
                    int purPrice=lstTranPurchase.get(j).getPurPrice();
                    int quantity=lstTranPurchase.get(j).getQuantity();
                    int isProductUnit=lstTranPurchase.get(j).getIsProductUnit();
                    int unitId=lstTranPurchase.get(j).getUnitId();
                    String unitKeyword=lstTranPurchase.get(j).getUnitKeyword();
                    String unitType=lstTranPurchase.get(j).getUnitType();
                    db.insertPurchaseItemTemp(productId,productName,purPrice,quantity,isProductUnit,unitId,unitKeyword,unitType);
                }
                Intent i=new Intent(context,PurchaseConfirmActivity.class);
                i.putExtra("LstPurchaseProduct",(Serializable) lstTranPurchase);
                i.putExtra("SupplierID", selectedSupplierID);
                i.putExtra("VoucherNo", voucherNo);
                i.putExtra("SupplierName", tvSupplier.getText().toString());
                startActivity(i);
                lstTranPurchase=new ArrayList<>();
            }
        });
        /*btnSupplierAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSupplierDialog();
            }
        });*/
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCurrentPurchase();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExistInPurchase=false;
                int productId,productUnitId=0;
                if(selectedProductPosition == 0)return;
                productId =lstProduct.get(selectedProductPosition).getProductId();
//                if (lstProduct.get(selectedProductPosition).getIsProductUnit() == 1) productUnitId=lstProduct.get(selectedProductPosition).getLstProductUnit().get(selectedProductUnitPosition).getUnitId();
                if (lstProduct.get(selectedProductPosition).getIsProductUnit() == 1) productUnitId=selectedProductUnitID;
                for(int i=0;i<lstTranPurchase.size();i++){
                    if(lstTranPurchase.get(i).getProductId() == productId){
                        if(lstTranPurchase.get(i).getIsProductUnit() == 1){  // if have unit
                            if(lstTranPurchase.get(i).getUnitId()==productUnitId){
                                isExistInPurchase=true;
                                break;
                            }
                        }else{  // no unit
                            isExistInPurchase=true;
                            break;
                        }
                    }
                }
                if(!isExistInPurchase) {
                    TranPurchaseModel data = new TranPurchaseModel();
                    data.setProductId(lstProduct.get(selectedProductPosition).getProductId());
                    data.setProductName(lstProduct.get(selectedProductPosition).getProductName());
                    data.setQuantity(1);
                    data.setPurPrice(lstProduct.get(selectedProductPosition).getPurPrice());
                    data.setIsProductUnit(lstProduct.get(selectedProductPosition).getIsProductUnit());
                    if (lstProduct.get(selectedProductPosition).getIsProductUnit() == 1) {
                       /* productModel.setSelectUnitId(lstProduct.get(selectedProductPosition).getLstProductUnit().get(selectedProductUnitPosition).getUnitId());
                        productModel.setSelectUnitKeyword(lstProduct.get(selectedProductPosition).getLstProductUnit().get(selectedProductUnitPosition).getUnitKeyword());
                        productModel.setPosPurPrice(lstProduct.get(selectedProductPosition).getLstProductUnit().get(selectedProductUnitPosition).getPuPurPrice());*/
                        data.setUnitId(lstProduct.get(selectedProductPosition).getSelectUnitId());
                        data.setUnitKeyword(lstProduct.get(selectedProductPosition).getSelectUnitKeyword());
                        data.setPurPrice(lstProduct.get(selectedProductPosition).getPurPrice());
                        data.setUnitType(lstProduct.get(selectedProductPosition).getUnitType());
                    }
                    lstTranPurchase.add(data);
                    setPurchaseProduct(lstTranPurchase);
                }else{
                    Toast.makeText(context,"Already exist in purchase!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDialog(i);
                return false;
            }
        });
        tvProductUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId=lstProduct.get(selectedProductPosition).getProductId();
                String productName=lstProduct.get(selectedProductPosition).getProductName();
                List<ProductUnitModel> lstProductUnit=db.getProductUnitByProductID(productId,systemInfo.PurchaseModule);
                showProductUnitDialog(lstProductUnit,productName);
            }
        });
        tvSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSupplierDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getCategory();
        lstTranPurchase=db.getPurchaseItemTemp();
        if (lstTranPurchase.size() == 0) {
            getPurVoucherNumber();
            clearCurrentPurchase();
        }
    }

    @Override
    public void onQuantityClickListener(int position,TextView tvQuantity){
        editStatus = quantityEditStatus;
        systemInfo.showCalcDialog(tvQuantity,null,context,systemInfo.calcQtyEditStatus,lstTranPurchase,position,null,null);
    }

    @Override
    public void onPriceClickListener(int position,TextView tvPrice){
        editStatus = priceEditStatus;
        systemInfo.showCalcDialog(null,tvPrice,context,systemInfo.calcPriceEditStatus,lstTranPurchase,position,null,null);
    }

    private void setPurchaseProduct(List<TranPurchaseModel> list){
        purchaseProductAdapter =new PurchaseProductAdapter(this,list);
        lvProduct.setAdapter(purchaseProductAdapter);
        purchaseProductAdapter.setOnEventListener(this);
    }

    private void getProduct(int categoryId,String keyword,boolean isSearch){
        lstProduct=db.getProductByCategoryOrKeyword(categoryId,keyword,isSearch,true,systemInfo.PurchaseModule);
        spProductAdapter=new SpProductAdapter(this,lstProduct);
        spProduct.setAdapter(spProductAdapter);
    }

    private void getCategory(){
        lstCategory = db.getCategoryWithDefault(getResources().getString(R.string.choose_category));
        spCategoryAdapter = new SpCategoryAdapter(this, lstCategory);
        spCategory.setAdapter(spCategoryAdapter);
    }

    private void getSupplier(){
        lstSupplier = db.getSupplierWithDefault(getResources().getString(R.string.supplier));
        if(lstSupplier.size()!=0){
            selectedSupplierID=lstSupplier.get(0).getSupplierId();
            tvSupplier.setText(lstSupplier.get(0).getSupplierName());
        }
        /*spSupplierAdapter = new SpSupplierAdapter(this, lstSupplier);
        spSupplier.setAdapter(spSupplierAdapter);*/
    }

    private void getPurVoucherNumber(){
        int purVoucherNumber= db.getPurVoucherNumber();
       /* if(purVoucherNumber == 0){  // not exist sale voucher number
            db.insertPurVoucherNumber(systemInfo.StartPurchaseVoucherNumber);
            purVoucherNumber=systemInfo.StartPurchaseVoucherNumber;
        }*/
        tvVoucherNo.setText(String.valueOf(purVoucherNumber));
    }

    private void clearCurrentPurchase(){
        selectedProductPosition=0;
//        spSupplier.setSelection(0);
        spCategory.setSelection(0);
        spProduct.setSelection(0);
        spProductUnit.setAdapter(null);
        lstTranPurchase=new ArrayList<>();
        setPurchaseProduct(lstTranPurchase);
    }

    /*private void showSupplierDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_supplier, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etSupplierName=v.findViewById(R.id.etSupplierName);
        final TextInputLayout input_supplier_name=v.findViewById(R.id.input_supplier_name);

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
                if(etSupplierName.getText().length()==0){
                    input_supplier_name.setError(getResources().getString(R.string.please_enter_value));
                }else {
                    String supplierName = etSupplierName.getText().toString();
                    if (db.insertSupplier(supplierName, "", "", "", 0)) {
                        getSupplier();
                        if(lstSupplier.size() != 0) spSupplier.setSelection(lstSupplier.size() -1 );
                        Toast.makeText(context, R.string.saved_supplier, Toast.LENGTH_SHORT).show();
                        showDialog.dismiss();
                    }
                }
            }
        });
    }*/

    /*private void showNumberDialog(final int position,final TextView tvQuantity,final TextView tvPrice){
        LayoutInflater li=LayoutInflater.from(context);
        View view=li.inflate(R.layout.dialog_number, null);
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setView(view);

        final EditText etInput= view.findViewById(R.id.etInput);
        final Button btnOk= view.findViewById(R.id.btnOk);
        final Button btnCancel= view.findViewById(R.id.btnCancel);
        final Button btnOne= view.findViewById(R.id.btnOne);
        final Button btnTwo= view.findViewById(R.id.btnTwo);
        final Button btnThree= view.findViewById(R.id.btnThree);
        final Button btnFour= view.findViewById(R.id.btnFour);
        final Button btnFive= view.findViewById(R.id.btnFive);
        final Button btnSix= view.findViewById(R.id.btnSix);
        final Button btnSeven= view.findViewById(R.id.btnSeven);
        final Button btnEight= view.findViewById(R.id.btnEight);
        final Button btnNine= view.findViewById(R.id.btnNine);
        final Button btnZero= view.findViewById(R.id.btnZero);

        *//*if(editStatus==quantityEditStatus)etInput.setText(tvQuantity.getText().toString());
        else if(editStatus==priceEditStatus)etInput.setText(systemInfo.convertTSValToNormal(tvPrice.getText().toString()));*//*

        dialog.setCancelable(true);
        final AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        etInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (etInput.getRight() - etInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etInput.getText().toString().length()!=0){
                            String num=etInput.getText().toString();
                            num=num.substring(0,num.length()-1);
                            etInput.setText(num);
                        }
                    }
                }
                return false;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View arg0) {
                if(etInput.getText().toString().length() == 0){
                    if(editStatus == quantityEditStatus) Toast.makeText(context,R.string.please_enter_value,Toast.LENGTH_SHORT).show();
                    else if(editStatus == priceEditStatus) {
                        tvPrice.setText("0");
                        lstTranPurchase.get(position).setPurPrice(0);
                    }
                    return;
                }
                if(editStatus == quantityEditStatus) {
                    tvQuantity.setText(etInput.getText().toString());
                    lstTranPurchase.get(position).setQuantity(Integer.parseInt(etInput.getText().toString()));
                }else if(editStatus == priceEditStatus){
                    tvPrice.setText(String.valueOf(systemInfo.df.format(Double.parseDouble(etInput.getText().toString()))));
                    lstTranPurchase.get(position).setPurPrice(Integer.parseInt(etInput.getText().toString()));
                }
                alertDialog.dismiss();
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnOne.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnOne.getText().toString());
                }
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnTwo.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnTwo.getText().toString());
                }
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnThree.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnThree.getText().toString());
                }
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnFour.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnFour.getText().toString());
                }
            }
        });

        btnFive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnFive.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnFive.getText().toString());
                }
            }
        });

        btnSix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnSix.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnSix.getText().toString());
                }
            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnSeven.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnSeven.getText().toString());
                }
            }
        });

        btnEight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnEight.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnEight.getText().toString());
                }
            }
        });

        btnNine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnNine.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnNine.getText().toString());
                }
            }
        });

        btnZero.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnZero.getText().toString();
                    etInput.setText(newNum);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });
    }*/

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
                lstTranPurchase.remove(removePosition);
                setPurchaseProduct(lstTranPurchase);
                showDialog.dismiss();
            }
        });
    }

    private void showSupplierDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_general_item, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvGeneralItem=v.findViewById(R.id.lvGeneralItem);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);

        tvProfileTitle.setText(getResources().getString(R.string.sub_supplier));
        SupplierOnlyAdapter supplierOnlyAdapter=new SupplierOnlyAdapter(context,lstSupplier);
        lvGeneralItem.setAdapter(supplierOnlyAdapter);

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
                tvSupplier.setText(lstSupplier.get(i).getSupplierName());
                selectedSupplierID=lstSupplier.get(i).getSupplierId();
                showDialog.dismiss();
            }
        });
    }

    private void showProductUnitDialog(final List<ProductUnitModel> lstProductUnit,String productName){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_general_item, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvProductUnit=v.findViewById(R.id.lvGeneralItem);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);

        tvProfileTitle.setText(productName);
        ProductUnitAdapter productUnitAdapter=new ProductUnitAdapter(context,lstProductUnit,systemInfo.PurchaseModule);
        lvProductUnit.setAdapter(productUnitAdapter);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        lvProductUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvProductUnit.setText(lstProductUnit.get(i).getUnitKeyword());
                selectedProductUnitID=lstProductUnit.get(i).getUnitId();
                lstProduct.get(selectedProductPosition).setSelectUnitId(lstProductUnit.get(i).getUnitId());
                lstProduct.get(selectedProductPosition).setSelectUnitKeyword(lstProductUnit.get(i).getUnitKeyword());
                lstProduct.get(selectedProductPosition).setPurPrice(lstProductUnit.get(i).getPuPurPrice());
                lstProduct.get(selectedProductPosition).setUnitType(lstProductUnit.get(i).getUnitType());
//                selectedProductUnitPosition=i;
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
//        spSupplier=findViewById(R.id.spSupplier);
        tvVoucherNo=findViewById(R.id.tvVoucherNo);
        spProduct=findViewById(R.id.spProduct);
        spCategory=findViewById(R.id.spCategory);
        spProductUnit=findViewById(R.id.spProductUnit);
        btnRefresh=findViewById(R.id.btnRefresh);
        lvProduct=findViewById(R.id.lvProduct);
//        btnSupplierAdd=findViewById(R.id.btnSupplierAdd);
        btnPay=findViewById(R.id.btnPay);
        btnAdd=findViewById(R.id.btnAdd);
        tvProductUnit=findViewById(R.id.tvProductUnit);
        tvSupplier=findViewById(R.id.tvSupplier);
    }
}
