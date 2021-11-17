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

import adapter.UnitListAdapter;
import database.DatabaseAccess;
import model.UnitModel;

public class UnitActivity extends AppCompatActivity {

    UnitListAdapter unitListAdapter;
    RelativeLayout layoutDelete;
    ListView lvUnit;
    EditText etSearch;
    ImageButton btnClose,btnDelete;
    TextView tvUnitCount;
    DatabaseAccess db;
    Context context=this;
    List<UnitModel> lstUnit=new ArrayList<>();
    int save=1,edit=2,type,editUnitId;
    String editUnitName,editUnitKeyword;
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_unit);

        getUnit();

        lvUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editUnitId =lstUnit.get(i).getUnitId();
                editUnitName =lstUnit.get(i).getUnitName();
                editUnitKeyword=lstUnit.get(i).getUnitKeyword();
                type=edit;
                showUnitDialog(type);
            }
        });
        lvUnit.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                editUnitId=lstUnit.get(i).getUnitId();
                tvUnitCount.setText(lstUnit.get(i).getUnitName()+ " " +getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUnitId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteUnit(editUnitId)) {
                    editUnitId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_unit, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.no_delete_unit, Toast.LENGTH_SHORT).show();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etSearch.getText().toString().length() != 0) getUnitByFilter(etSearch.getText().toString());
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
                showUnitDialog(type);
            case R.id.action_refresh:
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.setText("");
                getUnit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUnit(){
        lstUnit=new ArrayList<>();
        lstUnit = db.getUnitTableData();
        unitListAdapter =new UnitListAdapter(context,lstUnit);
        lvUnit.setAdapter(unitListAdapter);
    }

    private void getUnitByFilter(String unitKeyword){
        lstUnit=new ArrayList<>();
        lstUnit = db.getUnitByFilter(unitKeyword);
        unitListAdapter =new UnitListAdapter(context,lstUnit);
        lvUnit.setAdapter(unitListAdapter);
    }

    private void updateAdapter(){
        lstUnit = db.getUnitTableData();
        unitListAdapter.updateResults(lstUnit);
    }

    private void setLayoutResource(){
        lvUnit=findViewById(R.id.lvUnit);
        etSearch=findViewById(R.id.etSearch);
        layoutDelete=findViewById(R.id.layoutDelete);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        tvUnitCount=findViewById(R.id.tvUnitCount);
    }

    private void showUnitDialog(final int typeSaveEdit){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_unit, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etUnitName=v.findViewById(R.id.etUnitName);
        final EditText etUnitKeyword=v.findViewById(R.id.etUnitKeyword);
        final TextInputLayout input_unit_name=v.findViewById(R.id.input_unit_name);
        final TextInputLayout input_unit_keyword=v.findViewById(R.id.input_unit_keyword);

        if(typeSaveEdit ==edit ){
            etUnitName.setText(editUnitName);
            etUnitKeyword.setText(editUnitKeyword);
        }

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
                if(etUnitName.getText().length()==0){
                    input_unit_name.setError(getResources().getString(R.string.please_enter_value));
                }else if(etUnitKeyword.getText().length()==0) {
                    input_unit_keyword.setError(getResources().getString(R.string.please_enter_value));
                }else {
                    if(typeSaveEdit==save) {
                        if (db.insertUnit(etUnitName.getText().toString(),etUnitKeyword.getText().toString())) {
                            updateAdapter();
                            etUnitName.setText("");
                            etUnitKeyword.setText("");
                            Toast.makeText(context, R.string.saved_unit, Toast.LENGTH_SHORT).show();
                        }
                    }else if(typeSaveEdit==edit){
                        if (db.updateUnit(editUnitId,etUnitName.getText().toString(),etUnitKeyword.getText().toString())) {
                            updateAdapter();
                            etUnitName.setText("");
                            etUnitKeyword.setText("");
                            Toast.makeText(context, R.string.edit_success, Toast.LENGTH_SHORT).show();
                            showDialog.dismiss();
                        }
                    }
                }
            }
        });
    }
}
