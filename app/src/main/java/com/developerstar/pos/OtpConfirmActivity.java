package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import common.SystemInfo;
import database.DatabaseAccess;

public class OtpConfirmActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    EditText etOTP1,etOTP2,etOTP3,etOTP4,etOTP5,etOTP6;
    Button btnNext,btnResendCode;
    TextView tvNumber;
    SystemInfo systemInfo=new SystemInfo();
    FirebaseAuth auth;
    String otp,verificationCode;
    String mobileNumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_confirm);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i=getIntent();
        mobileNumber=i.getStringExtra("MobileNumber");
        verificationCode=i.getStringExtra("VerificationCode");

        tvNumber.setText("We have sent you an SMS with a code to the number +95 - " + mobileNumber.substring(1));

        StartFirebaseLogin();

        etOTP1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(etOTP1.length()==0)etOTP1.requestFocus();
                else etOTP2.requestFocus();
                return false;
            }
        });
        etOTP2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(etOTP2.length()==0)etOTP2.requestFocus();
                else etOTP3.requestFocus();
                return false;
            }
        });
        etOTP3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(etOTP3.length()==0)etOTP3.requestFocus();
                else etOTP4.requestFocus();
                return false;
            }
        });
        etOTP4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(etOTP4.length()==0)etOTP4.requestFocus();
                else etOTP5.requestFocus();
                return false;
            }
        });
        etOTP5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(etOTP5.length()==0)etOTP5.requestFocus();
                else etOTP6.requestFocus();
                return false;
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // test code
                /*startActivity(new Intent(OtpConfirmActivity.this, InfoSlideActivity.class));
                finish();*/
                // end test code

                progressDialog.show();
                otp=etOTP1.getText().toString()+etOTP2.getText().toString()+etOTP3.getText().toString()+etOTP4.getText().toString()+etOTP5.getText().toString()+etOTP6.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithPhone(credential);
            }
        });
        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        mobileNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        OtpConfirmActivity.this,        // Activity (for callback binding)
                        mCallback);
            }
        });
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent i=new Intent(OtpConfirmActivity.this,InfoSlideActivity.class);
                            i.putExtra("MobileNumber",mobileNumber);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(OtpConfirmActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }
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
        etOTP1=findViewById(R.id.etOTP1);
        etOTP2=findViewById(R.id.etOTP2);
        etOTP3=findViewById(R.id.etOTP3);
        etOTP4=findViewById(R.id.etOTP4);
        etOTP5=findViewById(R.id.etOTP5);
        etOTP6=findViewById(R.id.etOTP6);
        btnNext=findViewById(R.id.btnNext);
        btnResendCode=findViewById(R.id.btnResendCode);
        tvNumber=findViewById(R.id.tvNumber);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }
}
