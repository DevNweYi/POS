package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.ReceivableListAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.CustomerModel;

public class ReceivableListActivity extends AppCompatActivity {

    TextView tvTotalPay;
    EditText etSearch,etDate;
    ListView lvReceivable;
    DatabaseAccess db;
    Context context=this;
    ReceivableListAdapter receivableListAdapter;
    List<CustomerModel> lstCustomer=new ArrayList<>();
    SystemInfo systemInfo=new SystemInfo();
    int customerId;
    private final int DATE_PICKER_DIALOG=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_receivable_list);

        getCustomerDebtAmount(etSearch.getText().toString());
        tvTotalPay.setText(String.valueOf(systemInfo.df.format(db.getCustomerTotalDebtAmount())));

        lvReceivable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                customerId=lstCustomer.get(i).getCustomerId();
                showPayDebtDialog(lstCustomer.get(i).getCustomerName(),lstCustomer.get(i).getDebtAmount());
            }
        });

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.getText().toString().length() != 0){
                            getCustomerDebtAmount(etSearch.getText().toString());
                        }
                    }
                }
                return false;
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

    private void clearControls(){
        etSearch.setText("");
        getCustomerDebtAmount(etSearch.getText().toString());
    }

    private void getCustomerDebtAmount(String customerName){
        lstCustomer = db.getCustomerDebtAmount(customerName);
        receivableListAdapter = new ReceivableListAdapter(this, lstCustomer);
        lvReceivable.setAdapter(receivableListAdapter);
    }

    private void updateAdapter(String customerName){
        lstCustomer = db.getCustomerDebtAmount(customerName);
        receivableListAdapter.updateResults(lstCustomer);
    }

    private void showPayDebtDialog(final String name, final int debtAmount){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_pay_debt, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final ImageButton btnClose=v.findViewById(R.id.btnClose);
        final ImageButton btnDate=v.findViewById(R.id.btnDate);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etDebtAmount=v.findViewById(R.id.etDebtAmount);
        final EditText etPaidAmount=v.findViewById(R.id.etPaidAmount);
        etDate=v.findViewById(R.id.etDate);
        final EditText etRemark=v.findViewById(R.id.etRemark);
        final TextView tvName=v.findViewById(R.id.tvName);
        final TextInputLayout input_paid_amount=v.findViewById(R.id.input_paid_amount);
        final TextInputLayout input_debt_amount=v.findViewById(R.id.input_debt_amount);

        tvName.setText("From "+name);
        etDebtAmount.setText(String.valueOf(systemInfo.df.format(debtAmount)));
        etDate.setText(systemInfo.getTodayDate());

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etPaidAmount.getText().toString().length()==0){
                    input_paid_amount.setError(getResources().getString(R.string.please_enter_value));
                    return;
                }
                int paidAmount=Integer.parseInt(etPaidAmount.getText().toString());
                if(paidAmount>debtAmount){
                    input_paid_amount.setError(getResources().getString(R.string.paid_greater_receivable));
                    return;
                }
                int curDebtAmount = debtAmount - paidAmount;
                db.updateDebtAmountByCustomer(customerId, curDebtAmount);
                db.insertReceivable(customerId,debtAmount,paidAmount,etDate.getText().toString(),etRemark.getText().toString());
                updateAdapter("");
                tvTotalPay.setText(String.valueOf(systemInfo.df.format(db.getCustomerTotalDebtAmount())));
                Toast.makeText(context,"Payment Success!",Toast.LENGTH_SHORT).show();
                showDialog.dismiss();
            }
        });
    }

    private DatePickerDialog showDatePicker(){
        final Calendar cCalendar=Calendar.getInstance();
        DatePickerDialog datePicker=new DatePickerDialog(ReceivableListActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cCalendar.set(Calendar.YEAR,year);
                cCalendar.set(Calendar.MONTH, monthOfYear);
                cCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat=new SimpleDateFormat(systemInfo.DATE_FORMAT);
                etDate.setText(dateFormat.format(cCalendar.getTime()));
            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void setLayoutResource(){
        lvReceivable=findViewById(R.id.lvReceivable);
        etSearch=findViewById(R.id.etSearch);
        tvTotalPay=findViewById(R.id.tvTotalPay);
    }
}
