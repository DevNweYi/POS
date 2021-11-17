package com.developerstar.pos;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.AdjustmentProductAdapter;
import adapter.ProductUnitAdapter;
import adapter.SpCategoryAdapter;
import adapter.SpProductAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import listener.PurchaseAdjustListener;
import model.CategoryModel;
import model.ProductModel;
import model.ProductUnitModel;
import model.TranAdjustmentModel;

public class AdjustmentSetupActivity extends AppCompatActivity implements PurchaseAdjustListener {

    Spinner spCategory,spProduct;
    TextView tvProductUnit,tvTotalAmt;
    EditText etRemark;
    ImageButton btnRefresh,btnAdd;
    ListView lvProduct;
    Button btnAddAdjustment;
    DatabaseAccess db;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();
    SpCategoryAdapter spCategoryAdapter;
    List<CategoryModel> lstCategory=new ArrayList<>();
    SpProductAdapter spProductAdapter;
    List<ProductModel> lstProduct=new ArrayList<>();
    AdjustmentProductAdapter adjustmentProductAdapter;
    List<TranAdjustmentModel> lstTranAdjustment=new ArrayList<>();
    int editStatus, quantityEditStatus =1, priceEditStatus =2;
    int selectedProductPosition,selectedProductUnitID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_setup);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_add_adjust_product);

        getCategory();

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int catId = lstCategory.get(i).getCategoryId();
                getProduct(catId,"",false);
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
                selectedProductPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAddAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lstTranAdjustment.size()==0)return;
                db.insertAdjustment(systemInfo.getTodayDate(),systemInfo.getCurrentTime(),LoginActivity.UserID,LoginActivity.UserName,Integer.parseInt(systemInfo.convertTSValToNormal(tvTotalAmt.getText().toString())),etRemark.getText().toString(),lstTranAdjustment);
                finish();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCurrentAdjustment();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExistInPurchase=false;
                int productId,productUnitId=0;
                if(selectedProductPosition == 0)return;
                productId =lstProduct.get(selectedProductPosition).getProductId();
                if (lstProduct.get(selectedProductPosition).getIsProductUnit() == 1) productUnitId=selectedProductUnitID;
                for(int i=0;i<lstTranAdjustment.size();i++){
                    if(lstTranAdjustment.get(i).getProductId() == productId){
                        if(lstTranAdjustment.get(i).getIsProductUnit() == 1){  // if have unit
                            if(lstTranAdjustment.get(i).getUnitId()==productUnitId){
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
                    TranAdjustmentModel data = new TranAdjustmentModel();
                    data.setProductId(lstProduct.get(selectedProductPosition).getProductId());
                    data.setProductName(lstProduct.get(selectedProductPosition).getProductName());
                    data.setQuantity(1);
                    data.setPurPrice(lstProduct.get(selectedProductPosition).getPurPrice());
                    data.setIsProductUnit(lstProduct.get(selectedProductPosition).getIsProductUnit());
                    if (lstProduct.get(selectedProductPosition).getIsProductUnit() == 1) {
                        data.setUnitId(lstProduct.get(selectedProductPosition).getSelectUnitId());
                        data.setUnitKeyword(lstProduct.get(selectedProductPosition).getSelectUnitKeyword());
                        data.setPurPrice(lstProduct.get(selectedProductPosition).getPurPrice());
                        data.setUnitType(lstProduct.get(selectedProductPosition).getUnitType());
                    }
                    lstTranAdjustment.add(data);
                    setAdjustmentProduct(lstTranAdjustment);
                    calculateTotalAmount(lstTranAdjustment,tvTotalAmt);
                }else{
                    Toast.makeText(context,"Already exist in list!",Toast.LENGTH_SHORT).show();
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
    public void onQuantityClickListener(int position,TextView tvQuantity){
        editStatus = quantityEditStatus;
        systemInfo.showCalcDialog(tvQuantity,null,context,systemInfo.calcQtyEditStatus,null,position,lstTranAdjustment,tvTotalAmt);
    }

    @Override
    public void onPriceClickListener(int position,TextView tvPrice){
        editStatus = priceEditStatus;
        systemInfo.showCalcDialog(null,tvPrice,context,systemInfo.calcPriceEditStatus,null,position,lstTranAdjustment,tvTotalAmt);
    }

    public void calculateTotalAmount(List<TranAdjustmentModel> list,TextView textView){
        int totalAmount=0;
        for(int i=0;i<list.size();i++){
            totalAmount+=list.get(i).getQuantity()*list.get(i).getPurPrice();
        }
        if(textView!=null)textView.setText(String.valueOf(systemInfo.df.format(totalAmount)));
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

    private void setAdjustmentProduct(List<TranAdjustmentModel> list){
        adjustmentProductAdapter =new AdjustmentProductAdapter(this,list);
        lvProduct.setAdapter(adjustmentProductAdapter);
        adjustmentProductAdapter.setOnEventListener(this);
    }

    private void clearCurrentAdjustment(){
        selectedProductPosition=0;
        spCategory.setSelection(0);
        spProduct.setSelection(0);
        lstTranAdjustment=new ArrayList<>();
        setAdjustmentProduct(lstTranAdjustment);
        tvTotalAmt.setText("0");
        etRemark.setText("");
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
                lstTranAdjustment.remove(removePosition);
                setAdjustmentProduct(lstTranAdjustment);
                calculateTotalAmount(lstTranAdjustment,tvTotalAmt);
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
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        etRemark=findViewById(R.id.etRemark);
        spProduct=findViewById(R.id.spProduct);
        spCategory=findViewById(R.id.spCategory);
        btnRefresh=findViewById(R.id.btnRefresh);
        lvProduct=findViewById(R.id.lvProduct);
        btnAddAdjustment=findViewById(R.id.btnAddAdjustment);
        btnAdd=findViewById(R.id.btnAdd);
        tvProductUnit=findViewById(R.id.tvProductUnit);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
    }
}
