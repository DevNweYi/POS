package com.developerstar.pos;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseAccess;
import model.BillSettingModel;

public class BillSettingActivity extends AppCompatActivity {

    TextInputLayout input_footer_message1,input_footer_message2;
    EditText etFooterMessage1,etFooterMessage2,etRemark;
    Button btnSave;
    DatabaseAccess db;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_setting);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_bill_setting);

        fillData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateControls()){
                    String footerMessage1=etFooterMessage1.getText().toString();
                    String footerMessage2=etFooterMessage2.getText().toString();
                    String remark=etRemark.getText().toString();
                    db.insertUpdateBillSetting(footerMessage1,footerMessage2,remark);
                    clearControls();
                    Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillData(){
        BillSettingModel billSettingModel = db.getBillSettingTableData();
        etFooterMessage1.setText(billSettingModel.getFooterMessage1());
        etFooterMessage2.setText(billSettingModel.getFooterMessage2());
        etRemark.setText(billSettingModel.getRemark());
    }

    private boolean validateControls(){
        if(etFooterMessage1.getText().toString().length()==0){
            input_footer_message1.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private void clearControls(){
        input_footer_message1.setErrorEnabled(false);
        input_footer_message2.setErrorEnabled(false);
    }

    private void setLayoutResource(){
        input_footer_message1=findViewById(R.id.input_footer_message1);
        input_footer_message2=findViewById(R.id.input_footer_message2);
        etFooterMessage1=findViewById(R.id.etFooterMessage1);
        etFooterMessage2=findViewById(R.id.etFooterMessage2);
        etRemark=findViewById(R.id.etRemark);
        btnSave=findViewById(R.id.btnSave);
    }
}
