package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.SpUserAdapter;
import database.DatabaseAccess;
import model.UserModel;

public class LoginActivity extends AppCompatActivity {

    Spinner spUser;
    TextInputLayout input_password;
    EditText etPassword;
    Button btnLogin,btnForgetPassword,btnSignUp;
    TextView tvWelcome;
    DatabaseAccess db;
    Context context=this;
    List<UserModel> lstUser=new ArrayList<>();
    SpUserAdapter spUserAdapter;
    public static int UserID;
    public static String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        getUser();
        tvWelcome.setText("Welcome to "+db.getShopName());

        if(lstUser.size()==0){
            Intent i=new Intent(context,MainActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().length()==0){
                    input_password.setError(getResources().getString(R.string.please_enter_value));
                    return;
                }
                int position = spUser.getSelectedItemPosition();
                if(checkUser(position)) {
                    UserID = lstUser.get(position).getUserId();
                    UserName = lstUser.get(position).getUserName();
                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(context,"Wrong Password!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private boolean checkUser(int position){
        boolean result=false;
        int count=db.isExistUser(lstUser.get(position).getUserId(),etPassword.getText().toString());
        if(count == 1)result=true;
        return result;
    }

    private void getUser(){
        lstUser=new ArrayList<>();
        lstUser = db.getUserTableData();
        spUserAdapter=new SpUserAdapter(context,lstUser);
        spUser.setAdapter(spUserAdapter);
    }

    private void setLayoutResource(){
        spUser=findViewById(R.id.spUser);
        input_password=findViewById(R.id.input_password);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnForgetPassword=findViewById(R.id.btnForgetPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        tvWelcome=findViewById(R.id.tvWelcome);
    }
}
