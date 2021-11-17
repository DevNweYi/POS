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
import model.SupplierModel;

public class SupplierSetupActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    int editSupplierId;
    TextInputLayout input_supplier_name;
    EditText etSupplierName,etMobileNumber,etOtherMobileNumber,etAddress,etContactName;
    CheckBox chkIsAllowCredit;
    boolean isEdit;
    SupplierModel supplierModel=new SupplierModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_setup);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.to_add_new_supplier);

        Intent i = getIntent();
        editSupplierId = i.getIntExtra("SupplierID",0);

        if(editSupplierId != 0) fillEditData();
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
                if(!isEdit) saveSupplier();
                else editSupplier();
                return true;
            case R.id.action_refresh:
                clearControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveSupplier() {
        int isAllowCredit = 0;
        if (validateControls()) {
            String supplierName = etSupplierName.getText().toString();
            String contactName = etContactName.getText().toString();
            String mobileNumber = etMobileNumber.getText().toString();
            String otherMobileNumber = etOtherMobileNumber.getText().toString();
            String address = etAddress.getText().toString();
            if (chkIsAllowCredit.isChecked()) isAllowCredit = 1;
            if (db.insertSupplier(supplierName, mobileNumber, otherMobileNumber, address, isAllowCredit,contactName)) {
                clearControls();
                Toast.makeText(context, R.string.saved_supplier, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editSupplier() {
        int isAllowCredit = 0;
        if (validateControls()) {
            String supplierName = etSupplierName.getText().toString();
            String contactName = etContactName.getText().toString();
            String mobileNumber = etMobileNumber.getText().toString();
            String otherMobileNumber = etOtherMobileNumber.getText().toString();
            String address = etAddress.getText().toString();
            if (chkIsAllowCredit.isChecked()) isAllowCredit = 1;
            if (db.updateSupplier(editSupplierId, supplierName, mobileNumber, otherMobileNumber, address, isAllowCredit,contactName))
                finish();
        }
    }

    private void fillEditData(){
        isEdit=true;
        supplierModel = db.getSupplierBySupplierID(editSupplierId);
        etSupplierName.setText(supplierModel.getSupplierName());
        etContactName.setText(supplierModel.getContactName());
        etMobileNumber.setText(supplierModel.getMobileNumber());
        etOtherMobileNumber.setText(supplierModel.getOtherMobileNumber());
        etAddress.setText(supplierModel.getAddress());
        if(supplierModel.getIsAllowCredit() == 1)chkIsAllowCredit.setChecked(true);
        else chkIsAllowCredit.setChecked(false);
    }

    private void clearControls(){
        etSupplierName.setText("");
        etContactName.setText("");
        etMobileNumber.setText("");
        etOtherMobileNumber.setText("");
        etAddress.setText("");
        input_supplier_name.setErrorEnabled(false);
        chkIsAllowCredit.setChecked(false);
        isEdit=false;
    }

    private boolean validateControls(){
        if(etSupplierName.getText().toString().length() == 0){
            input_supplier_name.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private void setLayoutResource(){
        input_supplier_name=findViewById(R.id.input_supplier_name);
        etSupplierName=findViewById(R.id.etSupplierName);
        etContactName=findViewById(R.id.etContactName);
        etMobileNumber=findViewById(R.id.etMobileNumber);
        etOtherMobileNumber=findViewById(R.id.etOtherMobileNumber);
        etAddress=findViewById(R.id.etAddress);
        chkIsAllowCredit=findViewById(R.id.chkIsAllowCredit);
    }
}
