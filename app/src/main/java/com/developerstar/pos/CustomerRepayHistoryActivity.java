package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.CustomerOnlyAdapter;
import adapter.CustomerRepayAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.CustomerModel;
import model.ReceivableModel;

public class CustomerRepayHistoryActivity extends AppCompatActivity {

    TextView tvCustomer,tvDate;
    ListView lvRepayHistory;
    ImageButton btnDate;
    Button btnFromDate,btnToDate;
    DatabaseAccess db;
    Context context=this;
    List<CustomerModel> lstCustomer=new ArrayList<>();
    List<ReceivableModel> lstReceivable=new ArrayList<>();
    int selectedCustomerID;
    CustomerRepayAdapter customerRepayAdapter;
    String fromDate,toDate;
    SystemInfo systemInfo=new SystemInfo();
    boolean isFromDate;
    private final int DATE_PICKER_DIALOG=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_repay_history);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_customer_repayment_history);

        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();
        tvDate.setText(fromDate+" - "+toDate);
        getCustomerWithDefault();
        getReceivable();

        tvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomerDialog();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateRangeDialog();
            }
        });
        lvRepayHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showRepayHistoryDialog(lstReceivable.get(i).getDate(),lstReceivable.get(i).getCustomerName(),lstReceivable.get(i).getDebtAmount(),lstReceivable.get(i).getPaidAmount(),lstReceivable.get(i).getRemark());
            }
        });
        lvRepayHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDialog(i);
                return true;
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

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_PICKER_DIALOG:
                return showDatePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker(){
        final Calendar cCalendar=Calendar.getInstance();
        DatePickerDialog datePicker=new DatePickerDialog(CustomerRepayHistoryActivity.this,new DatePickerDialog.OnDateSetListener() {
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

    private void clearControls(){
        fromDate=systemInfo.getTodayDate();
        toDate=systemInfo.getTodayDate();
        tvDate.setText(fromDate+" - "+toDate);
        selectedCustomerID=lstCustomer.get(0).getCustomerId();
        tvCustomer.setText(lstCustomer.get(0).getCustomerName());
        getReceivable();
    }

    private void getCustomerWithDefault(){
        lstCustomer = db.getCustomerWithDefault();
        if(lstCustomer.size()!=0){
            CustomerModel data=new CustomerModel();
            data.setCustomerId(0);
            data.setCustomerName(getResources().getString(R.string.sub_customer));
            lstCustomer.set(0,data);
            selectedCustomerID=lstCustomer.get(0).getCustomerId();
            tvCustomer.setText(lstCustomer.get(0).getCustomerName());
        }
    }

    private void getReceivable(){
        lstReceivable=db.getReceivable(selectedCustomerID,fromDate,toDate);
        customerRepayAdapter=new CustomerRepayAdapter(context,lstReceivable);
        lvRepayHistory.setAdapter(customerRepayAdapter);
    }

    private void showCustomerDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_general_item, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ListView lvGeneralItem=v.findViewById(R.id.lvGeneralItem);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);

        tvProfileTitle.setText(getResources().getString(R.string.sub_customer));
        CustomerOnlyAdapter customerOnlyAdapter=new CustomerOnlyAdapter(context,lstCustomer);
        lvGeneralItem.setAdapter(customerOnlyAdapter);

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
                tvCustomer.setText(lstCustomer.get(i).getCustomerName());
                selectedCustomerID=lstCustomer.get(i).getCustomerId();
                getReceivable();
                showDialog.dismiss();
            }
        });
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
                getReceivable();
                showDialog.dismiss();
            }
        });
    }

    private void showRepayHistoryDialog(String date,String name,int debtAmt,int paidAmt,String remark){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_repay_history, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final TextView tvDate=v.findViewById(R.id.tvDate);
        final TextView tvName=v.findViewById(R.id.tvName);
        final TextView tvLabelDebtAmt=v.findViewById(R.id.tvLabelDebtAmt);
        final TextView tvDebtAmt=v.findViewById(R.id.tvDebtAmt);
        final TextView tvPaidAmt=v.findViewById(R.id.tvPaidAmt);
        final TextView tvRemark=v.findViewById(R.id.tvRemark);
        final ImageButton btnClose=v.findViewById(R.id.btnClose);

        tvDate.setText(date);
        tvName.setText(name);
        tvDebtAmt.setText(String.valueOf(systemInfo.df.format(debtAmt)));
        tvPaidAmt.setText(String.valueOf(systemInfo.df.format(paidAmt)));
        if(remark.length()!=0) tvRemark.setText(remark);
        else tvRemark.setText("-");

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
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
                db.deleteReceivable(lstReceivable.get(removePosition).getId());
                lstReceivable.remove(removePosition);
                customerRepayAdapter.updateResults(lstReceivable);
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        tvCustomer=findViewById(R.id.tvCustomer);
        btnDate=findViewById(R.id.btnDate);
        tvDate=findViewById(R.id.tvDate);
        lvRepayHistory=findViewById(R.id.lvRepayHistory);
    }
}
