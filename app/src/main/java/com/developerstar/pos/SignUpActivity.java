package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import database.DatabaseAccess;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout input_name,input_mobile,input_password;
    EditText etName,etMobileNumber,etPassword;
    Button btnSignUp;
    DatabaseAccess db;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().length()==0){
                    input_name.setError(getResources().getString(R.string.please_enter_value));
                }else if(etPassword.getText().length() == 0){
                    input_password.setError(getResources().getString(R.string.please_enter_value));
                }else{
                    if (db.insertUser(etName.getText().toString(),etMobileNumber.getText().toString(),etPassword.getText().toString())) {
                        Intent i = new Intent(context, DataChoiceActivity.class);
                        startActivity(i);
                        finish();
                    }
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

    private void setLayoutResource(){
        input_name=findViewById(R.id.input_name);
        input_mobile=findViewById(R.id.input_mobile);
        input_password=findViewById(R.id.input_password);
        etName=findViewById(R.id.etName);
        etMobileNumber=findViewById(R.id.etMobileNumber);
        etPassword=findViewById(R.id.etPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
    }
}
