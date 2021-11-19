package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.DatabaseAccess;

public class AppPurchaseExpiredActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    Button btnContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_purchase_expired);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.company_phone) ));
                startActivity(i);
            }
        });
    }

    private void setLayoutResource(){
        btnContactUs=findViewById(R.id.btnContactUs);
    }
}
