package com.developerstar.pos;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.CategoryAdapter;
import database.DatabaseAccess;
import model.CategoryModel;

public class CategoryActivity extends AppCompatActivity{

    CategoryAdapter categoryAdapter;
    RelativeLayout layoutDelete;
    ListView lvCategory;
    EditText etSearch;
    ImageButton btnClose,btnDelete;
    TextView tvCategoryCount;
    DatabaseAccess db;
    Context context=this;
    List<CategoryModel> lstCategory=new ArrayList<>();
    int save=1,edit=2,type,editCategoryId;
    String editCategoryName;
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_category);

        getCategory();

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editCategoryId =lstCategory.get(i).getCategoryId();
                editCategoryName =lstCategory.get(i).getCategoryName();
                type=edit;
                showCategoryDialog(type);
            }
        });
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                editCategoryId=lstCategory.get(i).getCategoryId();
                tvCategoryCount.setText(lstCategory.get(i).getCategoryName()+ " " +getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategoryId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteCategory(editCategoryId)) {
                    editCategoryId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_category, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.no_delete_category, Toast.LENGTH_SHORT).show();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etSearch.getText().toString().length() != 0) getCategoryByFilter(etSearch.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                type=save;
                showCategoryDialog(type);
            case R.id.action_refresh:
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.setText("");
                getCategory();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCategory(){
        lstCategory=new ArrayList<>();
        lstCategory = db.getCategory();
        categoryAdapter=new CategoryAdapter(context,lstCategory);
        lvCategory.setAdapter(categoryAdapter);
    }

    private void getCategoryByFilter(String categoryName){
        lstCategory=new ArrayList<>();
        lstCategory = db.getCategoryByFilter(categoryName);
        categoryAdapter=new CategoryAdapter(context,lstCategory);
        lvCategory.setAdapter(categoryAdapter);
    }

    private void updateAdapter(){
        lstCategory = db.getCategory();
        categoryAdapter.updateResults(lstCategory);
    }

    private void setLayoutResource(){
        lvCategory=findViewById(R.id.lvCategory);
        etSearch=findViewById(R.id.etSearch);
        layoutDelete=findViewById(R.id.layoutDelete);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        tvCategoryCount=findViewById(R.id.tvCategoryCount);
    }

    private void showCategoryDialog(final int typeSaveEdit){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_category, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etCategoryName=v.findViewById(R.id.etCategoryName);
        final TextInputLayout input_category_name=v.findViewById(R.id.input_category_name);

        if(typeSaveEdit ==edit )etCategoryName.setText(editCategoryName);

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
                if(etCategoryName.getText().length()==0){
                    input_category_name.setError(getResources().getString(R.string.please_enter_value));
                }else {
                    if(typeSaveEdit==save) {
                        if (db.insertCategory(etCategoryName.getText().toString())) {
                            updateAdapter();
                            etCategoryName.setText("");
                            Toast.makeText(context, R.string.saved_category, Toast.LENGTH_SHORT).show();
                        }
                    }else if(typeSaveEdit==edit){
                        if (db.updateCategory(editCategoryId,etCategoryName.getText().toString())) {
                            updateAdapter();
                            Toast.makeText(context, R.string.edit_success, Toast.LENGTH_SHORT).show();
                            showDialog.dismiss();
                        }
                    }
                }
            }
        });
    }
}
