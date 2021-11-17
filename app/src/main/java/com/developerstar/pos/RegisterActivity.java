package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import common.SystemInfo;
import database.DatabaseAccess;

public class RegisterActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    EditText etMacAddress,etRegisterKey;
    Button btnRegister;
    String generateKey,macAddress,registerKey;
    String[] splitarr;
    String firstkey,secondkey,thirdkey,fourthkey,fifthkey,sixkey,formula="n1w2e3y4i5a4",splitMac="",finalkey;
    TextInputLayout input_register_key;
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        generateRegisterKey();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        macAddress=etMacAddress.getText().toString();
        registerKey=etRegisterKey.getText().toString();

        if(macAddress.length()==0){
            Toast.makeText(context,"Not Found MAC Address!",Toast.LENGTH_LONG).show();
            return;
        }
        else if(registerKey.length()==0){
            input_register_key.setError(getResources().getString(R.string.please_enter_value));
            return;
        }
        else if(!generateKey.equals(registerKey)){
            Toast.makeText(context,"Invalid Register Key!",Toast.LENGTH_LONG).show();
            return;
        }
        else{
            db.setRegister();
            Intent i=new Intent(context,InfoSlideActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void setLayoutResource(){
        etMacAddress=findViewById(R.id.etMacAddress);
        etRegisterKey=findViewById(R.id.etRegisterKey);
        btnRegister=findViewById(R.id.btnRegister);
        input_register_key=findViewById(R.id.input_register_key);
    }

    private void generateRegisterKey(){
        String macAddress= systemInfo.getMacAddress();
        if(macAddress.length()==0)return;

        etMacAddress.setText(macAddress);

        splitarr=macAddress.split(":");
        for(int i=0;i<splitarr.length;i++){
            splitMac+=splitarr[i];
        }

        sixkey=Character.toString(splitMac.charAt(11))+Character.toString(splitMac.charAt(10))+Character.toString(splitMac.charAt(1))+Character.toString(splitMac.charAt(0));
        thirdkey=Character.toString(splitMac.charAt(9))+Character.toString(splitMac.charAt(8))+Character.toString(splitMac.charAt(7))+Character.toString(splitMac.charAt(6));
        fourthkey=Character.toString(splitMac.charAt(5))+Character.toString(splitMac.charAt(4))+Character.toString(splitMac.charAt(3))+Character.toString(splitMac.charAt(2));

        firstkey=Character.toString(formula.charAt(11))+Character.toString(formula.charAt(10))+Character.toString(formula.charAt(9))+Character.toString(formula.charAt(8));
        secondkey=Character.toString(sixkey.charAt(0))+Character.toString(formula.charAt(6))+Character.toString(formula.charAt(5))+Character.toString(sixkey.charAt(3));
        fifthkey=Character.toString(sixkey.charAt(1))+Character.toString(formula.charAt(2))+Character.toString(sixkey.charAt(2))+Character.toString(formula.charAt(0));

        finalkey=firstkey+"-"+secondkey+"-"+thirdkey+"-"+fourthkey+"-"+fifthkey+"-"+sixkey+"-";
        generateKey=finalkey.substring(0, finalkey.length()-1);
    }
}
