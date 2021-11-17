package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.DatabaseAccess;

public class TrialExpiredActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    Button btnSeeAllPlan,btnContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_expired);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "09420243505" ));
                startActivity(i);
            }
        });
        btnSeeAllPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TrialExpiredActivity.this,AppPurchasePlanActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setLayoutResource(){
        btnSeeAllPlan=findViewById(R.id.btnSeeAllPlan);
        btnContactUs=findViewById(R.id.btnContactUs);
    }
}
