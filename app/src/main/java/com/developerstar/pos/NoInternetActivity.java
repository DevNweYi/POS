package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import common.SystemInfo;

public class NoInternetActivity extends AppCompatActivity {

    ImageButton btnClose;
    Button btnRetry;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getSupportActionBar().hide();
        setLayoutResource();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(systemInfo.checkConnection(context)){
                    Intent intent = new Intent(context, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setLayoutResource(){
        btnClose=findViewById(R.id.btnClose);
        btnRetry=findViewById(R.id.btnRetry);
    }
}
