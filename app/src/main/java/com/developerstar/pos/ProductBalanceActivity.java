package com.developerstar.pos;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductBalanceAdapter;
import adapter.SpCategoryAdapter;
import database.DatabaseAccess;
import model.CategoryModel;
import model.ProductModel;

public class ProductBalanceActivity extends AppCompatActivity {

    SpCategoryAdapter spCategoryAdapter;
    List<CategoryModel> lstCategory=new ArrayList<>();
    List<ProductModel> lstProduct=new ArrayList<>();
    DatabaseAccess db;
    Context context=this;
    Spinner spCategory;
    ListView lvProductBalance;
    EditText etSearch;
    ProductBalanceAdapter productBalanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_balance);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_product_balance);

        getCategory();

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.getText().toString().length() != 0){
                            int catPosition = spCategory.getSelectedItemPosition();
                            int catId = lstCategory.get(catPosition).getCategoryId();
                            getProductBalance(etSearch.getText().toString(),catId);
                        }
                    }
                }
                return false;
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int catId = lstCategory.get(position).getCategoryId();
                getProductBalance(etSearch.getText().toString(),catId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                clearControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProductBalance(String productName,int categoryId){
        lstProduct = db.getProductBalance(productName,categoryId);
        productBalanceAdapter = new ProductBalanceAdapter(this, lstProduct);
        lvProductBalance.setAdapter(productBalanceAdapter);
    }

    private void clearControls(){
        spCategory.setVisibility(View.VISIBLE);
        spCategory.setSelection(0);
        etSearch.setText("");
        getProductBalance("",0);
    }

    private void getCategory(){
        lstCategory = db.getCategoryWithDefault(getResources().getString(R.string.choose_category));
        spCategoryAdapter = new SpCategoryAdapter(this, lstCategory);
        spCategory.setAdapter(spCategoryAdapter);
    }

    private void setLayoutResource(){
        spCategory=findViewById(R.id.spCategory);
        lvProductBalance=findViewById(R.id.lvProductBalance);
        etSearch=findViewById(R.id.etSearch);
    }
}
