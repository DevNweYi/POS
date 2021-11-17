package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductAdapter;
import adapter.SpCategoryAdapter;
import database.DatabaseAccess;
import model.CategoryModel;
import model.ProductModel;

public class ProductListActivity extends AppCompatActivity {

    SpCategoryAdapter spCategoryAdapter;
    List<CategoryModel> lstCategory=new ArrayList<>();
    List<ProductModel> lstProduct=new ArrayList<>();
    DatabaseAccess db;
    Context context=this;
    Spinner spCategory;
    ListView lvProduct;
    EditText etSearch;
    FloatingActionButton fab;
    ImageButton btnClose,btnDelete;
    RelativeLayout layoutDelete;
    TextView tvDeletedProduct;
    ProductAdapter productAdapter;
    View oldView;
    int deleteProductId,selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_product_list);

        getCategory();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ProductSetupActivity.class);
                startActivity(i);
            }
        });
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int productId = lstProduct.get(position).getProductId();
                Intent i=new Intent(context,ProductSetupActivity.class);
                i.putExtra("ProductID",productId);
                startActivity(i);
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                spCategory.setVisibility(View.GONE);
                deleteProductId=lstProduct.get(i).getProductId();
                tvDeletedProduct.setText(lstProduct.get(i).getProductName()+ " " + getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProductId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                spCategory.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteProduct(deleteProductId)) {
                    deleteProductId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    spCategory.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_product, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.no_delete_product, Toast.LENGTH_SHORT).show();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.getText().toString().length() != 0){
                            int catPosition = spCategory.getSelectedItemPosition();
                            int catId = lstCategory.get(catPosition).getCategoryId();
                            getProductByFilter(etSearch.getText().toString(),catId);
                        }
                    }
                }
                return false;
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedCategoryId = lstCategory.get(position).getCategoryId();
                getProductByFilter(etSearch.getText().toString(),selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getProductByFilter("",selectedCategoryId);
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

  /*  private void getProduct(){
        lstProduct = db.getProduct();
        productAdapter = new ProductAdapter(this, lstProduct);
        lvProduct.setAdapter(productAdapter);
    }*/

    private void getProductByFilter(String productName,int categoryId){
        lstProduct = db.getProductByFilter(productName,categoryId);
        productAdapter = new ProductAdapter(this, lstProduct);
        lvProduct.setAdapter(productAdapter);
    }

    private void updateAdapter(){
        lstProduct = db.getProductByFilter("",selectedCategoryId);
        productAdapter.updateResults(lstProduct);
    }

    private void getCategory(){
        lstCategory = db.getCategoryWithDefault(getResources().getString(R.string.choose_category));
        spCategoryAdapter = new SpCategoryAdapter(this, lstCategory);
        spCategory.setAdapter(spCategoryAdapter);
    }

    private void clearControls(){
        layoutDelete.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        spCategory.setVisibility(View.VISIBLE);
        spCategory.setSelection(0);
        etSearch.setText("");
        getProductByFilter("",selectedCategoryId);
    }

    private void setLayoutResource(){
        spCategory=findViewById(R.id.spCategory);
        lvProduct=findViewById(R.id.lvProduct);
        etSearch=findViewById(R.id.etSearch);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        layoutDelete=findViewById(R.id.layoutDelete);
        tvDeletedProduct=findViewById(R.id.tvDeletedProduct);
        fab=findViewById(R.id.fab);
    }
}
