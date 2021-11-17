package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AppPurchaseSuccessActivity extends AppCompatActivity {

    Button btnExit;
    TextView tvPurchasePlan,tvPurchasePlanPrice;
    Context context=this;
    String selectedPlan,selectedPlanPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_purchase_success);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i = getIntent();
        selectedPlan = i.getStringExtra("SelectedPlan");
        selectedPlanPrice = i.getStringExtra("SelectedPlanPrice");

        tvPurchasePlan.setText(selectedPlan);
        tvPurchasePlanPrice.setText(selectedPlanPrice);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                finish();
            }
        });
    }

    private void setLayoutResource(){
        btnExit=findViewById(R.id.btnExit);
        tvPurchasePlan=findViewById(R.id.tvPurchasePlan);
        tvPurchasePlanPrice=findViewById(R.id.tvPurchasePlanPrice);
    }
}
