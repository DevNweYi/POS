package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseAccess;
import model.CustomerModel;

public class CustomerSetupActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    int editCustomerId;
    TextInputLayout input_customer_name;
    EditText etCustomerName,etMobileNumber,etOtherMobileNumber,etAddress,etContactName;
    CheckBox chkIsAllowCredit;
    boolean isEdit;
    CustomerModel customerModel=new CustomerModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_setup);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.to_add_new_customer);

        Intent i = getIntent();
        editCustomerId = i.getIntExtra("CustomerID",0);

        if(editCustomerId != 0) fillEditData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if(!isEdit) saveCustomer();
                else editCustomer();
                return true;
            case R.id.action_refresh:
                clearControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCustomer() {
        int isAllowCredit = 0;
        if (validateControls()) {
            String customerName = etCustomerName.getText().toString();
            String contactName = etContactName.getText().toString();
            String mobileNumber = etMobileNumber.getText().toString();
            String otherMobileNumber = etOtherMobileNumber.getText().toString();
            String address = etAddress.getText().toString();
            if (chkIsAllowCredit.isChecked()) isAllowCredit = 1;
            if (db.insertCustomer(customerName, mobileNumber, otherMobileNumber, address, isAllowCredit,contactName)) {
                clearControls();
                Toast.makeText(context, R.string.saved_customer, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editCustomer() {
        int isAllowCredit = 0;
        if (validateControls()) {
            String customerName = etCustomerName.getText().toString();
            String contactName = etContactName.getText().toString();
            String mobileNumber = etMobileNumber.getText().toString();
            String otherMobileNumber = etOtherMobileNumber.getText().toString();
            String address = etAddress.getText().toString();
            if (chkIsAllowCredit.isChecked()) isAllowCredit = 1;
            if (db.updateCustomer(editCustomerId, customerName, mobileNumber, otherMobileNumber, address, isAllowCredit,contactName))
                finish();
        }
    }

    private void fillEditData(){
        isEdit=true;
        customerModel = db.getCustomerByCustomerID(editCustomerId);
        etCustomerName.setText(customerModel.getCustomerName());
        etContactName.setText(customerModel.getContactName());
        etMobileNumber.setText(customerModel.getMobileNumber());
        etOtherMobileNumber.setText(customerModel.getOtherMobileNumber());
        etAddress.setText(customerModel.getAddress());
        if(customerModel.getIsAllowCredit() == 1)chkIsAllowCredit.setChecked(true);
        else chkIsAllowCredit.setChecked(false);
    }

    private void clearControls(){
        etCustomerName.setText("");
        etContactName.setText("");
        etMobileNumber.setText("");
        etOtherMobileNumber.setText("");
        etAddress.setText("");
        input_customer_name.setErrorEnabled(false);
        chkIsAllowCredit.setChecked(false);
        isEdit=false;
    }

    private boolean validateControls(){
        if(etCustomerName.getText().toString().length() == 0){
            input_customer_name.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private void setLayoutResource(){
        input_customer_name=findViewById(R.id.input_customer_name);
        etCustomerName=findViewById(R.id.etCustomerName);
        etContactName=findViewById(R.id.etContactName);
        etMobileNumber=findViewById(R.id.etMobileNumber);
        etOtherMobileNumber=findViewById(R.id.etOtherMobileNumber);
        etAddress=findViewById(R.id.etAddress);
        chkIsAllowCredit=findViewById(R.id.chkIsAllowCredit);
    }
}
