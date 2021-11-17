package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.SpCategoryAdapter;
import adapter.UnitOnlyAdapter;
import database.DatabaseAccess;
import model.CategoryModel;
import model.ProductModel;
import model.UnitModel;

public class ProductSetupActivity extends AppCompatActivity {

    SpCategoryAdapter spCategoryAdapter;
    DatabaseAccess db;
    Context context=this;
    TextInputLayout input_code,input_name,input_sale_price,input_pur_price,input_quantity,input_track_stock;
    EditText etProductCode,etProductName,etSalePrice,etPurPrice,etQuantity,etTrackStock,etSUOMSalePrice,etSUOMPurPrice,
            etPUOMSalePrice,etPUOMPurPrice,etSaleUnitQty,etSaleStandardUnitQty,etPurUnitQty,etPurStandardUnitQty;
    Spinner spCategory;
    List<CategoryModel> lstCategory=new ArrayList<>();
    List<UnitModel> lstUnit=new ArrayList<>();
    LinearLayout layoutUnit,layoutSaleUnit,layoutPurUnit,layoutSaleUnitChild,layoutPurUnitChild,layoutSaleUnitQty,layoutPurUnitQty;
    Switch swtInventory,swtUnit;
    TextView tvSaleUnit,tvSaleStandardUnit,tvPurUnit,tvPurStandardUnit,tvStandardUnit,tvLabelSaleUnit,tvLabelPurUnit,
            tvQuantityUnit,tvTrackStockUnit,tvSalePriceUnit,tvPurPriceUnit;
    int isTrackStock,isProductUnit,editProductId,unitType,standardUnitId,saleUnitId,purUnitId;
    ProductModel productModel=new ProductModel();
    boolean isEdit;
    final int StandardUnit=1,SaleUnit=2,PurchaseUnit=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_setup);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_add_new_product);

        Intent i = getIntent();
        editProductId = i.getIntExtra("ProductID",0);

        getCategory();
        getUnit();

        if(editProductId != 0) fillEditData();

        swtUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isProductUnit=1;
                    clearUnitControls();
                    layoutUnit.setVisibility(View.VISIBLE);
                }else{
                    isProductUnit=0;
                    layoutUnit.setVisibility(View.GONE);
                    tvQuantityUnit.setVisibility(View.GONE);
                    tvTrackStockUnit.setVisibility(View.GONE);
                    tvSalePriceUnit.setVisibility(View.GONE);
                    tvPurPriceUnit.setVisibility(View.GONE);
                }
            }
        });
        swtInventory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isTrackStock=1;
                    etTrackStock.setVisibility(View.VISIBLE);
                    if(isProductUnit==1){
                        tvTrackStockUnit.setVisibility(View.VISIBLE);
                        tvTrackStockUnit.setText(tvStandardUnit.getText().toString());
                    }
                }else{
                    isTrackStock=0;
                    etTrackStock.setVisibility(View.GONE);
                    tvTrackStockUnit.setVisibility(View.GONE);
                }
            }
        });
        tvStandardUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitType=StandardUnit;
                showUnitListDialog();
            }
        });
        tvSaleUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitType=SaleUnit;
                showUnitListDialog();
            }
        });
        tvPurUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitType=PurchaseUnit;
                showUnitListDialog();
            }
        });
        etSaleUnitQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                int saleStandardUnitQty = Integer.parseInt(etSaleStandardUnitQty.getText().toString());
                if(saleStandardUnitQty!=1){
                    showAlertDialog(getResources().getString(R.string.one_base_unit_message));
                    return true;
                }
                return false;
            }
        });
        etSaleStandardUnitQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                int saleUnitQty = Integer.parseInt(etSaleUnitQty.getText().toString());
                if(saleUnitQty!=1){
                    showAlertDialog(getResources().getString(R.string.one_base_unit_message));
                    return true;
                }
                return false;
            }
        });
        etSaleUnitQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(etSaleUnitQty.getText().toString().length()==0 || etSaleUnitQty.getText().toString().startsWith("0")){
                        etSaleUnitQty.setText("1");
                    }
                }
            }
        });
        etSaleStandardUnitQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(etSaleStandardUnitQty.getText().toString().length()==0 || etSaleStandardUnitQty.getText().toString().startsWith("0")){
                        etSaleStandardUnitQty.setText("1");
                    }
                }
            }
        });
        etPurUnitQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                int purStandardUnitQty = Integer.parseInt(etPurStandardUnitQty.getText().toString());
                if(purStandardUnitQty!=1){
                    showAlertDialog(getResources().getString(R.string.one_base_unit_message));
                    return true;
                }
                return false;
            }
        });
        etPurStandardUnitQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                int purUnitQty = Integer.parseInt(etPurUnitQty.getText().toString());
                if(purUnitQty!=1){
                    showAlertDialog(getResources().getString(R.string.one_base_unit_message));
                    return true;
                }
                return false;
            }
        });
        etPurUnitQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(etPurUnitQty.getText().toString().length()==0 || etPurUnitQty.getText().toString().startsWith("0")){
                        etPurUnitQty.setText("1");
                    }
                }
            }
        });
        etPurStandardUnitQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(etPurStandardUnitQty.getText().toString().length()==0 || etPurStandardUnitQty.getText().toString().startsWith("0")){
                        etPurStandardUnitQty.setText("1");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if(!isEdit) saveProduct();
                else editProduct();
                return true;
            case R.id.action_refresh:
                clearControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillEditData(){
        isEdit=true;
        productModel = db.getProductByProductID(editProductId);
        etProductCode.setText(productModel.getProductCode());
        etProductName.setText(productModel.getProductName());
        for(int j=0;j<lstCategory.size();j++){
            if(lstCategory.get(j).getCategoryId() == productModel.getCategoryId()){
                spCategory.setSelection(j);
                break;
            }
        }
        etSalePrice.setText(String.valueOf(productModel.getSalePrice()));
        etPurPrice.setText(String.valueOf(productModel.getPurPrice()));
        etQuantity.setText(String.valueOf(productModel.getOpeningQuantity()));
        etQuantity.setEnabled(false);
        if(productModel.getIsTrackStock() == 1){
            isTrackStock=1;
            swtInventory.setChecked(true);
            etTrackStock.setVisibility(View.VISIBLE);
            etTrackStock.setText(String.valueOf(productModel.getTrackStock()));
        }
        if (productModel.getIsProductUnit() == 1) {
            isProductUnit = 1;
            swtUnit.setChecked(true);
            layoutUnit.setVisibility(View.VISIBLE);
            layoutSaleUnit.setVisibility(View.VISIBLE);
            layoutPurUnit.setVisibility(View.VISIBLE);
            standardUnitId=productModel.getStandardUnitId();
            tvStandardUnit.setText(productModel.getStandardUnitKeyword());
            tvQuantityUnit.setVisibility(View.VISIBLE);
            tvQuantityUnit.setText(tvStandardUnit.getText().toString());
            tvSalePriceUnit.setVisibility(View.VISIBLE);
            tvSalePriceUnit.setText(tvStandardUnit.getText().toString());
            tvPurPriceUnit.setVisibility(View.VISIBLE);
            tvPurPriceUnit.setText(tvStandardUnit.getText().toString());
            if(isTrackStock==1){
                tvTrackStockUnit.setVisibility(View.VISIBLE);
                tvTrackStockUnit.setText(tvStandardUnit.getText().toString());
            }
            tvStandardUnit.setEnabled(false);
            if(productModel.getSaleUnitId()!=0){
                layoutSaleUnitChild.setVisibility(View.VISIBLE);
                saleUnitId=productModel.getSaleUnitId();
                tvSaleUnit.setText(productModel.getSaleUnitKeyword());
                etSaleUnitQty.setText(String.valueOf(productModel.getSaleUnitQty()));
                tvLabelSaleUnit.setText(productModel.getSaleUnitKeyword());
                etSaleStandardUnitQty.setText(String.valueOf(productModel.getStandardSaleUnitQty()));
                tvSaleStandardUnit.setText(productModel.getStandardUnitKeyword());
                etSUOMSalePrice.setText(String.valueOf(productModel.getSaleUnitSalePrice()));
                etSUOMPurPrice.setText(String.valueOf(productModel.getSaleUnitPurPrice()));
                enableDisableSaleUnitQtyControls();
            }
            if(productModel.getPurUnitId()!=0){
                layoutPurUnitChild.setVisibility(View.VISIBLE);
                purUnitId=productModel.getPurUnitId();
                tvPurUnit.setText(productModel.getPurUnitKeyword());
                etPurUnitQty.setText(String.valueOf(productModel.getPurUnitQty()));
                tvLabelPurUnit.setText(productModel.getPurUnitKeyword());
                etPurStandardUnitQty.setText(String.valueOf(productModel.getStandardPurUnitQty()));
                tvPurStandardUnit.setText(productModel.getStandardUnitKeyword());
                etPUOMSalePrice.setText(String.valueOf(productModel.getPurUnitSalePrice()));
                etPUOMPurPrice.setText(String.valueOf(productModel.getPurUnitPurPrice()));
                enableDisablePurUnitQtyControls();
            }
        }
    }

    private void saveProduct(){
        int trackStock = 0,quantity = 0,salePrice,purPrice = 0,saleUnitQty=0,saleStanUnitQty=0,saleUnitSalePrice=0,saleUnitPurPrice=0,
            purUnitQty=0,purStanUnitQty=0,purUnitSalePrice=0,purUnitPurPrice=0;
        if(validateControls()){
            if(!db.isAlreadyExistProductCode(etProductCode.getText().toString())){
                String code=etProductCode.getText().toString();
                String name=etProductName.getText().toString();
                int catPosition = spCategory.getSelectedItemPosition();
                int categoryId=lstCategory.get(catPosition).getCategoryId();
                salePrice=Integer.parseInt(etSalePrice.getText().toString());
                if(etPurPrice.getText().toString().length()!=0)purPrice=Integer.parseInt(etPurPrice.getText().toString());
                if(etQuantity.getText().toString().length() != 0) quantity=Integer.parseInt(etQuantity.getText().toString());
                if(isTrackStock == 1 && etTrackStock.getText().toString().length() != 0) trackStock=Integer.parseInt(etTrackStock.getText().toString());

                if(isProductUnit==1) {
                    if(saleUnitId!=0) {
                        if (etSaleUnitQty.getText().toString().length() != 0)
                            saleUnitQty = Integer.parseInt(etSaleUnitQty.getText().toString());
                        if (etSaleStandardUnitQty.getText().toString().length() != 0)
                            saleStanUnitQty = Integer.parseInt(etSaleStandardUnitQty.getText().toString());
                        if (etSUOMSalePrice.getText().toString().length() != 0)
                            saleUnitSalePrice = Integer.parseInt(etSUOMSalePrice.getText().toString());
                        if (etSUOMPurPrice.getText().toString().length() != 0)
                            saleUnitPurPrice = Integer.parseInt(etSUOMPurPrice.getText().toString());
                    }
                    if(purUnitId!=0){
                        if (etPurUnitQty.getText().toString().length() != 0)
                            purUnitQty = Integer.parseInt(etPurUnitQty.getText().toString());
                        if (etPurStandardUnitQty.getText().toString().length() != 0)
                            purStanUnitQty = Integer.parseInt(etPurStandardUnitQty.getText().toString());
                        if (etPUOMSalePrice.getText().toString().length() != 0)
                            purUnitSalePrice = Integer.parseInt(etPUOMSalePrice.getText().toString());
                        if (etPUOMPurPrice.getText().toString().length() != 0)
                            purUnitPurPrice = Integer.parseInt(etPUOMPurPrice.getText().toString());
                    }
                }
                if (db.insertProduct(code, name, categoryId, salePrice, purPrice, quantity, isTrackStock, trackStock, isProductUnit, standardUnitId, saleUnitId, saleUnitQty, saleStanUnitQty, saleUnitSalePrice, saleUnitPurPrice, purUnitId,purUnitQty,purStanUnitQty,purUnitSalePrice,purUnitPurPrice )) {
                    clearControls();
                    Toast.makeText(context, R.string.saved_product, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context,R.string.product_code_already_exist,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editProduct(){
        int trackStock = 0,quantity = 0,salePrice ,purPrice = 0,saleUnitQty=0,saleStanUnitQty=0,saleUnitSalePrice=0,saleUnitPurPrice=0,
            purUnitQty=0,purStanUnitQty=0,purUnitSalePrice=0,purUnitPurPrice=0;
        if(validateControls()){
            if(!db.isAlreadyExistProductCodeForEdit(editProductId,etProductCode.getText().toString())){
                String code=etProductCode.getText().toString();
                String name=etProductName.getText().toString();
                int catPosition = spCategory.getSelectedItemPosition();
                int categoryId=lstCategory.get(catPosition).getCategoryId();
                salePrice=Integer.parseInt(etSalePrice.getText().toString());
                if(etPurPrice.getText().toString().length()!=0)purPrice=Integer.parseInt(etPurPrice.getText().toString());
                if(etQuantity.getText().toString().length() != 0) quantity=Integer.parseInt(etQuantity.getText().toString());
                if(isTrackStock == 1 && etTrackStock.getText().toString().length() != 0) trackStock=Integer.parseInt(etTrackStock.getText().toString());
                if(isProductUnit==1) {
                    if(saleUnitId!=0) {
                        if (etSaleUnitQty.getText().toString().length() != 0)
                            saleUnitQty = Integer.parseInt(etSaleUnitQty.getText().toString());
                        if (etSaleStandardUnitQty.getText().toString().length() != 0)
                            saleStanUnitQty = Integer.parseInt(etSaleStandardUnitQty.getText().toString());
                        if (etSUOMSalePrice.getText().toString().length() != 0)
                            saleUnitSalePrice = Integer.parseInt(etSUOMSalePrice.getText().toString());
                        if (etSUOMPurPrice.getText().toString().length() != 0)
                            saleUnitPurPrice = Integer.parseInt(etSUOMPurPrice.getText().toString());
                    }
                    if(purUnitId!=0){
                        if (etPurUnitQty.getText().toString().length() != 0)
                            purUnitQty = Integer.parseInt(etPurUnitQty.getText().toString());
                        if (etPurStandardUnitQty.getText().toString().length() != 0)
                            purStanUnitQty = Integer.parseInt(etPurStandardUnitQty.getText().toString());
                        if (etPUOMSalePrice.getText().toString().length() != 0)
                            purUnitSalePrice = Integer.parseInt(etPUOMSalePrice.getText().toString());
                        if (etPUOMPurPrice.getText().toString().length() != 0)
                            purUnitPurPrice = Integer.parseInt(etPUOMPurPrice.getText().toString());
                    }
                }
                if(db.updateProduct(editProductId,code,name,categoryId,salePrice,purPrice,quantity,isTrackStock,trackStock,isProductUnit, standardUnitId, saleUnitId, saleUnitQty, saleStanUnitQty, saleUnitSalePrice, saleUnitPurPrice, purUnitId,purUnitQty,purStanUnitQty,purUnitSalePrice,purUnitPurPrice))
                    Toast.makeText(context, R.string.edit_success, Toast.LENGTH_SHORT).show();
                    finish();
            }else{
                Toast.makeText(context,R.string.product_code_already_exist,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateControls(){
        if(etProductCode.getText().toString().length() == 0){
            input_code.setError(getResources().getString(R.string.enter_product_code));
            return false;
        }else if(etProductName.getText().toString().length() == 0){
            input_name.setError(getResources().getString(R.string.enter_product_name));
            return false;
        }else if(spCategory.getSelectedItemPosition() == 0){
            Toast.makeText(context,getResources().getString(R.string.please_choose_category),Toast.LENGTH_LONG).show();
            return false;
        }else if(etSalePrice.getText().toString().length() == 0){
            input_sale_price.setError(getResources().getString(R.string.enter_sale_price));
            return false;
        }
        if(isProductUnit==1){
            if(standardUnitId==0){
                showAlertDialog(getResources().getString(R.string.choose_standard_unit));
                return false;
            }
        }
        /*else if(isProductUnit == 0){
            if(etSalePrice.getText().toString().length() == 0){
                input_sale_price.setError(getResources().getString(R.string.enter_sale_price));
                return false;
            }
           *//* else if(etPurPrice.getText().toString().length() == 0){
                input_pur_price.setError(getResources().getString(R.string.enter_pur_price));
                return false;
            }*//*
        }else if(isProductUnit == 1){
            if(lstProductUnit.size() == 0){
                Toast.makeText(context,R.string.fill_unit_data,Toast.LENGTH_SHORT).show();
                return false;
            }
        }*/
        return true;
    }

    private void getCategory(){
        lstCategory = db.getCategoryWithDefault(getResources().getString(R.string.choose_category));
        spCategoryAdapter = new SpCategoryAdapter(this, lstCategory);
        spCategory.setAdapter(spCategoryAdapter);
    }

    private void getUnit(){
        lstUnit=db.getUnitTableData();
    }

    private void clearControls(){
        etProductCode.setText("");
        etProductName.setText("");
        etSalePrice.setText("");
        etPurPrice.setText("");
        etQuantity.setText("");
        etTrackStock.setText("");
        swtUnit.setChecked(false);
        isProductUnit=0;
        input_code.setErrorEnabled(false);
        input_name.setErrorEnabled(false);
        input_sale_price.setErrorEnabled(false);
        input_pur_price.setErrorEnabled(false);
        input_quantity.setErrorEnabled(false);
        isEdit=false;
    }

    private void clearUnitControls(){
        tvStandardUnit.setText("");
        tvSaleUnit.setText("");
        etSaleUnitQty.setText("1");
        tvLabelSaleUnit.setText("");
        etSaleStandardUnitQty.setText("1");
        tvSaleStandardUnit.setText("");
        etSUOMSalePrice.setText("");
        etSUOMPurPrice.setText("");
        tvPurUnit.setText("");
        etPurUnitQty.setText("1");
        tvLabelPurUnit.setText("");
        etPurStandardUnitQty.setText("1");
        tvPurStandardUnit.setText("");
        etPUOMSalePrice.setText("");
        etPUOMPurPrice.setText("");
        layoutSaleUnitChild.setVisibility(View.GONE);
        layoutPurUnitChild.setVisibility(View.GONE);
        layoutSaleUnit.setVisibility(View.GONE);
        layoutPurUnit.setVisibility(View.GONE);
        standardUnitId=0;
        saleUnitId=0;
        purUnitId=0;
    }

    private void showUnitListDialog(){
        UnitOnlyAdapter unitOnlyAdapter;
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_unit_list, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvUnitList=v.findViewById(R.id.lvUnitList);

        unitOnlyAdapter =new UnitOnlyAdapter(context,lstUnit);
        lvUnitList.setAdapter(unitOnlyAdapter);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        lvUnitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(unitType==StandardUnit) {
                    standardUnitId=lstUnit.get(i).getUnitId();
                    tvStandardUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvSaleStandardUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvPurStandardUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvQuantityUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvQuantityUnit.setVisibility(View.VISIBLE);
                    tvSalePriceUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvSalePriceUnit.setVisibility(View.VISIBLE);
                    tvPurPriceUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvPurPriceUnit.setVisibility(View.VISIBLE);
                    if(isTrackStock==1){
                        tvTrackStockUnit.setVisibility(View.VISIBLE);
                        tvTrackStockUnit.setText(lstUnit.get(i).getUnitKeyword());
                    }
                    layoutSaleUnit.setVisibility(View.VISIBLE);
                    layoutPurUnit.setVisibility(View.VISIBLE);
                    enableDisableSaleUnitQtyControls();
                    enableDisablePurUnitQtyControls();
                }
                else if(unitType==SaleUnit) {
                    saleUnitId=lstUnit.get(i).getUnitId();
                    tvSaleUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvLabelSaleUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvSaleStandardUnit.setText(tvStandardUnit.getText().toString());
                    layoutSaleUnitChild.setVisibility(View.VISIBLE);
                    enableDisableSaleUnitQtyControls();
                }
                else if(unitType==PurchaseUnit) {
                    purUnitId=lstUnit.get(i).getUnitId();
                    tvPurUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvLabelPurUnit.setText(lstUnit.get(i).getUnitKeyword());
                    tvPurStandardUnit.setText(tvStandardUnit.getText().toString());
                    layoutPurUnitChild.setVisibility(View.VISIBLE);
                    enableDisablePurUnitQtyControls();
                }
                showDialog.dismiss();
            }
        });
    }

    private void enableDisableSaleUnitQtyControls(){
        if(standardUnitId==saleUnitId){
            etSaleUnitQty.setEnabled(false);
            etSaleStandardUnitQty.setEnabled(false);
            etSaleUnitQty.setText("1");
            etSaleStandardUnitQty.setText("1");
        }else{
            etSaleUnitQty.setEnabled(true);
            etSaleStandardUnitQty.setEnabled(true);
        }
    }

    private void enableDisablePurUnitQtyControls(){
        if(standardUnitId==purUnitId){
            etPurUnitQty.setEnabled(false);
            etPurStandardUnitQty.setEnabled(false);
            etPurUnitQty.setText("1");
            etPurStandardUnitQty.setText("1");
        }else{
            etPurUnitQty.setEnabled(true);
            etPurStandardUnitQty.setEnabled(true);
        }
    }

    private void showAlertDialog(String message){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_alert, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnOk=v.findViewById(R.id.btnOk);
        final TextView tvAlertMessage=v.findViewById(R.id.tvAlertMessage);

        tvAlertMessage.setText(message);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        spCategory=findViewById(R.id.spCategory);
        etProductCode=findViewById(R.id.etProductCode);
        etProductName=findViewById(R.id.etProductName);
        etSalePrice=findViewById(R.id.etSalePrice);
        etPurPrice=findViewById(R.id.etPurPrice);
        etQuantity=findViewById(R.id.etQuantity);
        etTrackStock=findViewById(R.id.etTrackStock);
        input_code=findViewById(R.id.input_code);
        input_name=findViewById(R.id.input_name);
        input_sale_price=findViewById(R.id.input_sale_price);
        input_pur_price=findViewById(R.id.input_pur_price);
        input_quantity=findViewById(R.id.input_quantity);
        input_track_stock=findViewById(R.id.input_track_stock);
        swtInventory=findViewById(R.id.swtInventory);
        swtUnit=findViewById(R.id.swtUnit);
        layoutUnit=findViewById(R.id.layoutUnit);
        tvStandardUnit=findViewById(R.id.tvStandardUnit);
        layoutSaleUnit=findViewById(R.id.layoutSaleUnit);
        layoutPurUnit=findViewById(R.id.layoutPurUnit);
        tvSaleUnit=findViewById(R.id.tvSaleUnit);
        tvSaleStandardUnit=findViewById(R.id.tvSaleStandardUnit);
        tvPurUnit=findViewById(R.id.tvPurUnit);
        tvPurStandardUnit=findViewById(R.id.tvPurStandardUnit);
        etSaleStandardUnitQty=findViewById(R.id.etSaleStandardUnitQty);
        etSaleUnitQty=findViewById(R.id.etSaleUnitQty);
        etPurStandardUnitQty=findViewById(R.id.etPurStandardUnitQty);
        etPurUnitQty=findViewById(R.id.etPurUnitQty);
        tvLabelSaleUnit=findViewById(R.id.tvLabelSaleUnit);
        tvLabelPurUnit=findViewById(R.id.tvLabelPurUnit);
        layoutSaleUnitChild=findViewById(R.id.layoutSaleUnitChild);
        layoutPurUnitChild=findViewById(R.id.layoutPurUnitChild);
        tvQuantityUnit=findViewById(R.id.tvQuantityUnit);
        tvTrackStockUnit=findViewById(R.id.tvTrackStockUnit);
        etSUOMSalePrice=findViewById(R.id.etSUOMSalePrice);
        etSUOMPurPrice=findViewById(R.id.etSUOMPurPrice);
        etPUOMSalePrice=findViewById(R.id.etPUOMSalePrice);
        etPUOMPurPrice=findViewById(R.id.etPUOMPurPrice);
        layoutSaleUnitQty=findViewById(R.id.layoutSaleUnitQty);
        layoutPurUnitQty=findViewById(R.id.layoutPurUnitQty);
        tvSalePriceUnit=findViewById(R.id.tvSalePriceUnit);
        tvPurPriceUnit=findViewById(R.id.tvPurPriceUnit);
    }
}
