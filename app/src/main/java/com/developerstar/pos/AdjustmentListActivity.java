package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.AdjustmentMasterAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.MasterAdjustmentModel;

public class AdjustmentListActivity extends AppCompatActivity {

    EditText etSearch;
    ImageButton btnDate,btnClose,btnDelete;
    TextView tvDate,tvDeletedAdjustment;
    ListView lvAdjustmentList;
    Button btnFromDate,btnToDate;
    RelativeLayout layoutDelete;
    DatabaseAccess db;
    Context context=this;
    AdjustmentMasterAdapter adjustmentMasterAdapter;
    List<MasterAdjustmentModel> lstMasterAdjustment=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();
    private final int DATE_PICKER_DIALOG=1;
    View oldView;
    int deleteId;
    String fromDate,toDate;
    boolean isFromDate;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_adjust_product_list);

        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();
        tvDate.setText(fromDate+" - "+toDate);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AdjustmentSetupActivity.class);
                startActivity(i);
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateRangeDialog();
            }
        });
        lvAdjustmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i=new Intent(context,AdjustmentListDetailActivity.class);
                i.putExtra("MasterAdjustmentID",lstMasterAdjustment.get(position).getMasterAdjustmentId());
                i.putExtra("UserName",lstMasterAdjustment.get(position).getUserName());
                i.putExtra("AdjustDate",lstMasterAdjustment.get(position).getDate());
                i.putExtra("AdjustTime",lstMasterAdjustment.get(position).getTime());
                i.putExtra("TotalAmount",lstMasterAdjustment.get(position).getTotalAmount());
                i.putExtra("Remark",lstMasterAdjustment.get(position).getRemark());
                startActivity(i);
            }
        });
        lvAdjustmentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                btnDate.setVisibility(View.GONE);
                deleteId=lstMasterAdjustment.get(i).getMasterAdjustmentId();
                tvDeletedAdjustment.setText("damage # "+lstMasterAdjustment.get(i).getMasterAdjustmentId()+ " " + getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                deleteId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                btnDate.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteAdjustment(deleteId)) {
                    deleteId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    btnDate.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_adjustment, Toast.LENGTH_SHORT).show();
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
                            getMasterAdjustmentBySearch(fromDate,toDate,etSearch.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getMasterAdjustmentBySearch(fromDate,toDate,etSearch.getText().toString());
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

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_PICKER_DIALOG:
                return showDatePicker();
        }
        return super.onCreateDialog(id);
    }

    private void updateAdapter(){
        lstMasterAdjustment = db.getMasterAdjustmentByFilter(fromDate,toDate,etSearch.getText().toString());
        adjustmentMasterAdapter.updateResults(lstMasterAdjustment);
    }

    private void clearControls(){
        layoutDelete.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        btnDate.setVisibility(View.VISIBLE);
        etSearch.setText("");
        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();
        tvDate.setText(fromDate+" - "+toDate);
        getMasterAdjustmentBySearch(fromDate,toDate,"");
    }

    private void getMasterAdjustmentBySearch(String fromDate,String toDate,String searchKeyword){
        lstMasterAdjustment = db.getMasterAdjustmentByFilter(fromDate,toDate,searchKeyword);
        adjustmentMasterAdapter = new AdjustmentMasterAdapter(this, lstMasterAdjustment);
        lvAdjustmentList.setAdapter(adjustmentMasterAdapter);
    }

    private DatePickerDialog showDatePicker(){
        final Calendar cCalendar=Calendar.getInstance();
        DatePickerDialog datePicker=new DatePickerDialog(AdjustmentListActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cCalendar.set(Calendar.YEAR,year);
                cCalendar.set(Calendar.MONTH, monthOfYear);
                cCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat=new SimpleDateFormat(systemInfo.DATE_FORMAT);
                if(isFromDate) btnFromDate.setText(dateFormat.format(cCalendar.getTime()));
                else btnToDate.setText(dateFormat.format(cCalendar.getTime()));
            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void showDateRangeDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_date_range, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        btnFromDate=v.findViewById(R.id.btnFromDate);
        btnToDate=v.findViewById(R.id.btnToDate);
        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);

        btnFromDate.setText(systemInfo.getTodayDate());
        btnToDate.setText(systemInfo.getTodayDate());

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate=true;
                showDialog(DATE_PICKER_DIALOG);
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isFromDate=false;
                showDialog(DATE_PICKER_DIALOG);
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
                fromDate=btnFromDate.getText().toString();
                toDate=btnToDate.getText().toString();
                if(fromDate.length()==0 || fromDate==null){
                    Toast.makeText(context,getResources().getString(R.string.please_select_date_range),Toast.LENGTH_SHORT).show();
                    return;
                }else if(toDate.length()==0 || toDate==null){
                    Toast.makeText(context,getResources().getString(R.string.please_select_date_range),Toast.LENGTH_SHORT).show();
                    return;
                }
                tvDate.setText(fromDate+" - "+toDate);
                getMasterAdjustmentBySearch(fromDate,toDate,etSearch.getText().toString());
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        etSearch=findViewById(R.id.etSearch);
        btnDate=findViewById(R.id.btnDate);
        tvDate=findViewById(R.id.tvDate);
        lvAdjustmentList=findViewById(R.id.lvAdjustmentList);
        tvDeletedAdjustment=findViewById(R.id.tvDeletedAdjustment);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        layoutDelete=findViewById(R.id.layoutDelete);
        fab=findViewById(R.id.fab);
    }
}
