package com.developerstar.pos;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductForSaleAdapter;
import adapter.ProductUnitAdapter;
import adapter.SpCategoryAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import listener.ProductForSaleListener;
import model.CategoryModel;
import model.ProductModel;
import model.ProductUnitModel;

public class AddSaleProductActivity extends AppCompatActivity implements ProductForSaleListener {

    Spinner spCategory;
    ImageButton btnProductSearch,btnSearchCancel,btnBack;
    LinearLayout layoutSearch,layoutCategory;
    EditText etSearchKeyword;
    TextView tvTotalQuantity;
    ListView lvProduct;
    DatabaseAccess db;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();
    SpCategoryAdapter spCategoryAdapter;
    List<CategoryModel> lstCategory=new ArrayList<>();
    List<ProductModel> lstProduct=new ArrayList<>();
    ProductForSaleAdapter productForSaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale_product);
        db=new DatabaseAccess(context);
        setLayoutResource();

        getCategory();

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int catId = lstCategory.get(i).getCategoryId();
                getProduct(catId,"",false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        etSearchKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getProduct(0,etSearchKeyword.getText().toString(),true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnProductSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCategory.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
            }
        });
        btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCategory.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.GONE);
                etSearchKeyword.setText("");
                if(lstCategory.size()!=0) {
                    spCategory.setSelection(0);
                    getProduct(lstCategory.get(0).getCategoryId(),"",false);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTotalQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onUnitSelectListener(int position,TextView tvProductUnit,TextView tvSalePrice){
        int productId=lstProduct.get(position).getProductId();
        String productName=lstProduct.get(position).getProductName();
        List<ProductUnitModel> lstProductUnit=db.getProductUnitByProductID(productId,systemInfo.SaleModule);
        showProductUnitDialog(lstProductUnit,position,tvProductUnit,tvSalePrice,productName);
    }

    @Override
    public void onAddListener(int position){
        String productName=lstProduct.get(position).getProductName();
        int salePrice=lstProduct.get(position).getSalePrice();
        String unitKeyword=lstProduct.get(position).getSelectUnitKeyword();
        showAddSaleProductDialog(productName,unitKeyword,position,salePrice);
    }

    private void getProduct(int categoryId,String keyword,boolean isSearch){
        lstProduct=db.getProductByCategoryOrKeyword(categoryId,keyword,isSearch,false,systemInfo.SaleModule);
        productForSaleAdapter =new ProductForSaleAdapter(this,lstProduct);
        lvProduct.setAdapter(productForSaleAdapter);
        productForSaleAdapter.setOnEventListener(this);
    }

    private void getCategory(){
        lstCategory = db.getCategoryWithDefault(getResources().getString(R.string.choose_category));
        spCategoryAdapter = new SpCategoryAdapter(this, lstCategory);
        spCategory.setAdapter(spCategoryAdapter);
    }

    private void showProductUnitDialog(final List<ProductUnitModel> lstProductUnit,final int position,final TextView tvProductUnit,final TextView tvSalePrice,String productName){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_general_item, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvProductUnit=v.findViewById(R.id.lvGeneralItem);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);

        tvProfileTitle.setText(productName);
        ProductUnitAdapter productUnitAdapter=new ProductUnitAdapter(context,lstProductUnit,systemInfo.SaleModule);
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
                tvSalePrice.setText(String.valueOf(systemInfo.df.format(lstProductUnit.get(i).getPuSalePrice())));
                lstProduct.get(position).setSelectUnitId(lstProductUnit.get(i).getUnitId());
                lstProduct.get(position).setSelectUnitKeyword(lstProductUnit.get(i).getUnitKeyword());
                lstProduct.get(position).setSalePrice(lstProductUnit.get(i).getPuSalePrice());
                lstProduct.get(position).setUnitType(lstProductUnit.get(i).getUnitType());
                showDialog.dismiss();
            }
        });
    }

    private void showAddSaleProductDialog(String productName, String unitKeyword, final int currentPosition, int salePrice){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_sale_product, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final TextView tvProduct=v.findViewById(R.id.tvProduct);
        final ImageButton btnMinus=v.findViewById(R.id.btnMinus);
        final ImageButton btnPlus=v.findViewById(R.id.btnPlus);
        final TextView tvQuantity=v.findViewById(R.id.tvQuantity);
        final TextView tvSalePrice=v.findViewById(R.id.tvSalePrice);
        final TextView tvUnit=v.findViewById(R.id.tvUnit);

        tvProduct.setText(productName);
        tvSalePrice.setText(String.valueOf(systemInfo.df.format(salePrice)));
        if(unitKeyword==null) tvUnit.setText("");
        else tvUnit.setText(unitKeyword);
//        tvQuantity.setText(String.valueOf(quantity));

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
                systemInfo.showCalcDialog(tvQuantity,tvSalePrice,context,systemInfo.calcQtyEditStatus,null,0,null,null);
            }
        });
        tvSalePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemInfo.showCalcDialog(tvQuantity,tvSalePrice,context,systemInfo.calcPriceEditStatus,null,0,null,null);
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
                if(tvQuantity.getText().toString().equals("0"))return;
                int productId = lstProduct.get(currentPosition).getProductId();
                String productName = lstProduct.get(currentPosition).getProductName();
                int salePrice = Integer.parseInt(systemInfo.convertTSValToNormal(tvSalePrice.getText().toString()));
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                int isProductUnit = lstProduct.get(currentPosition).getIsProductUnit();
                int selectUnitId = lstProduct.get(currentPosition).getSelectUnitId();
                String selectUnitKeyword = lstProduct.get(currentPosition).getSelectUnitKeyword();
                String unitType=lstProduct.get(currentPosition).getUnitType();
                int amount=quantity*salePrice;
                db.insertSaleItemTemp(productId, productName, salePrice, quantity, isProductUnit, selectUnitId, selectUnitKeyword,unitType,amount);
                int totalQuantity=Integer.parseInt(tvTotalQuantity.getText().toString());
                totalQuantity=totalQuantity+quantity;
                tvTotalQuantity.setText(String.valueOf(totalQuantity));
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        spCategory=findViewById(R.id.spCategory);
        btnProductSearch=findViewById(R.id.btnProductSearch);
        lvProduct=findViewById(R.id.lvProduct);
        layoutCategory=findViewById(R.id.layoutCategory);
        layoutSearch=findViewById(R.id.layoutSearch);
        etSearchKeyword=findViewById(R.id.etSearchKeyword);
        btnSearchCancel=findViewById(R.id.btnSearchCancel);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_sale);
        View view=getSupportActionBar().getCustomView();
        btnBack=view.findViewById(R.id.btnBack);
        tvTotalQuantity =view.findViewById(R.id.tvSaleQuantity);
    }
}
