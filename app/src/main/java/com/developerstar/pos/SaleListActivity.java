package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import adapter.SaleMasterAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.MasterSaleModel;

public class SaleListActivity extends AppCompatActivity {

    EditText etSearch;
    ImageButton btnDate,btnClose,btnDelete;
    TextView tvDate,tvDeletedSale;
    Button btnFromDate,btnToDate;
    ListView lvSaleList;
    RelativeLayout layoutDelete;
    DatabaseAccess db;
    Context context=this;
    SaleMasterAdapter saleMasterAdapter;
    List<MasterSaleModel> lstMasterSale=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();
    private final int DATE_PICKER_DIALOG=1;
    View oldView;
    int deleteVoucherNo;
    String fromDate,toDate;
    boolean isFromDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_sale_receipts);

        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateRangeDialog();
            }
        });
        lvSaleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i=new Intent(context,SaleListDetailActivity.class);
                i.putExtra("id",lstMasterSale.get(position).getid());
                i.putExtra("VoucherNumber",lstMasterSale.get(position).getVoucherNumber());
                i.putExtra("UserName",lstMasterSale.get(position).getUserName());
                i.putExtra("CustomerID",lstMasterSale.get(position).getCustomerId());
                i.putExtra("CustomerName",lstMasterSale.get(position).getCustomerName());
                i.putExtra("SaleDate",lstMasterSale.get(position).getDate());
                i.putExtra("SaleTime",lstMasterSale.get(position).getTime());
                i.putExtra("TotalAmount",lstMasterSale.get(position).getTotalAmount());
                i.putExtra("PayDisPercent",lstMasterSale.get(position).getPayDisPercent());
                i.putExtra("DiscountAmount",lstMasterSale.get(position).getTotalDisAmount());
                i.putExtra("NetAmount",lstMasterSale.get(position).getNetAmount());
                i.putExtra("IsCredit",lstMasterSale.get(position).getIsCredit());
                i.putExtra("CreditRemark",lstMasterSale.get(position).getCreditRemark());
                i.putExtra("LastDebtAmount",lstMasterSale.get(position).getLastDebtAmount());
                i.putExtra("PaidAmount",lstMasterSale.get(position).getPaidAmount());
                i.putExtra("ChangeAmount",lstMasterSale.get(position).getChangeAmount());
                i.putExtra("DebtAmount",lstMasterSale.get(position).getDebtAmount());
                i.putExtra("IsDebtAmount",lstMasterSale.get(position).getIsDebtAmount());
                startActivity(i);
            }
        });
        lvSaleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                btnDate.setVisibility(View.GONE);
                deleteVoucherNo=lstMasterSale.get(i).getVoucherNumber();
                tvDeletedSale.setText(lstMasterSale.get(i).getVoucherNumber()+ " " + getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                deleteVoucherNo=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                btnDate.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteSale(deleteVoucherNo)) {
                    deleteVoucherNo = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    btnDate.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_sale, Toast.LENGTH_SHORT).show();
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
                            getMasterSaleBySearch(fromDate,toDate,etSearch.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getMasterSaleBySearch(fromDate,toDate,etSearch.getText().toString());
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
        lstMasterSale = db.getMasterSaleByFilter(fromDate,toDate,etSearch.getText().toString());
        saleMasterAdapter.updateResults(lstMasterSale);
    }

    private void clearControls(){
        layoutDelete.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        btnDate.setVisibility(View.VISIBLE);
        etSearch.setText("");
        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();
        tvDate.setText(getResources().getString(R.string.today_sale_list));
        getMasterSaleBySearch(fromDate,toDate,"");
    }

    private void getMasterSaleBySearch(String fromDate, String toDate, String searchKeyword){
        lstMasterSale = db.getMasterSaleByFilter(fromDate,toDate,searchKeyword);
        saleMasterAdapter = new SaleMasterAdapter(this, lstMasterSale);
        lvSaleList.setAdapter(saleMasterAdapter);
    }

    private DatePickerDialog showDatePicker(){
        final Calendar cCalendar=Calendar.getInstance();
        DatePickerDialog datePicker=new DatePickerDialog(SaleListActivity.this,new DatePickerDialog.OnDateSetListener() {
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
                getMasterSaleBySearch(fromDate,toDate,etSearch.getText().toString());
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        etSearch=findViewById(R.id.etSearch);
        btnDate=findViewById(R.id.btnDate);
        tvDate=findViewById(R.id.tvDate);
        lvSaleList=findViewById(R.id.lvSaleList);
        tvDeletedSale=findViewById(R.id.tvDeletedSale);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        layoutDelete=findViewById(R.id.layoutDelete);
    }
}
