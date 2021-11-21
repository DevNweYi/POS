package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import database.DatabaseAccess;

public class PhoneAuthActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    TextInputLayout input_mobile_number;
    EditText etMobileNumber;
    Button btnNext;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;
    String phoneNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        StartFirebaseLogin();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // test code
                /*Intent i = new Intent(PhoneAuthActivity.this, OtpConfirmActivity.class);
                i.putExtra("MobileNumber",etMobileNumber.getText().toString());
                i.putExtra("VerificationCode",verificationCode);
                startActivity(i);
                finish();*/
                // end test code

                phoneNumber=etMobileNumber.getText().toString();
                if(phoneNumber.length()==0){
                    input_mobile_number.setError(getResources().getString(R.string.enter_phone_number));
                    return;
                }
                progressDialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        PhoneAuthActivity.this,        // Activity (for callback binding)
                        mCallback);
            }
        });
    }

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                Toast.makeText(context,"Verification completed!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PhoneAuthActivity.this, OtpConfirmActivity.class);
                i.putExtra("MobileNumber",phoneNumber);
                i.putExtra("VerificationCode",verificationCode);
                startActivity(i);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(context,"Invalid number and verification failed!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(context,"Code sent!",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setLayoutResource(){
        input_mobile_number=findViewById(R.id.input_mobile_number);
        etMobileNumber=findViewById(R.id.etMobileNumber);
        btnNext=findViewById(R.id.btnNext);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }
}
