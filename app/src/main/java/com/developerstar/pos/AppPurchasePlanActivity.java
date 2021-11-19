package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import common.HttpHandler;
import common.ServiceURL;
import common.SystemInfo;
import database.DatabaseAccess;

public class AppPurchasePlanActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    LinearLayout layoutMonthlyPlan,layoutYearlyPlan,layoutCustomizedPlan;
    Button btnContactUs,btnPurchase;
    TextView tvMonthlyPlan,tvMonthlyPlanPrice,tvYearlyPlan,tvYearlyPlanPrice,tvCustomizedPlan,tvCustomizedPlanPrice;
    String selectedPlan,selectedPlanPrice,macAddress,name,mobileNumber,appPurchaseOrderMacAddress;
//    Date orderDate;
    SystemInfo systemInfo=new SystemInfo();
    private ProgressDialog progressDialog;
    int appPurchaseOrderId;
    private String TAG = AppPurchasePlanActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_purchase_plan);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        layoutMonthlyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPlan=systemInfo.MonthlyPlan;
                selectedPlanPrice=systemInfo.MonthlyPlanPrice;
                btnPurchase.setEnabled(true);
                layoutMonthlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r_yellow));
                layoutYearlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
                layoutCustomizedPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
            }
        });
        layoutYearlyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPlan=systemInfo.YearlyPlan;
                selectedPlanPrice=systemInfo.YearlyPlanPrice;
                btnPurchase.setEnabled(true);
                layoutMonthlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
                layoutYearlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r_yellow));
                layoutCustomizedPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
            }
        });
        layoutCustomizedPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPlan=systemInfo.CustomizedPlan;
                selectedPlanPrice=systemInfo.CustomizedPlanPrice;
                btnPurchase.setEnabled(true);
                layoutMonthlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
                layoutYearlyPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r));
                layoutCustomizedPlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_gray_5r_yellow));
            }
        });
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.company_phone) ));
                startActivity(i);
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppPurchaseOrderDialog();
            }
        });
    }

    private void showAppPurchaseOrderDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_app_purchase_order, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etName=v.findViewById(R.id.etName);
        final EditText etMobileNumber=v.findViewById(R.id.etMobileNumber);
        final TextInputLayout input_name=v.findViewById(R.id.input_name);
        final TextInputLayout input_mobile_number=v.findViewById(R.id.input_mobile_number);

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etName.getText().length()==0){
                    input_name.setError(getResources().getString(R.string.please_enter_value));
                }else if(etMobileNumber.getText().length()==0){
                    input_mobile_number.setError(getResources().getString(R.string.please_enter_value));
                }else {
                    if(systemInfo.checkConnection(context)){
                        macAddress=systemInfo.getMacAddress();
                        name=etName.getText().toString();
                        mobileNumber=etMobileNumber.getText().toString();
                       /* try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat(systemInfo.DATE_FORMAT);
                            orderDate = dateFormat.parse(systemInfo.getTodayDate());
                        }catch (ParseException ex){

                        }*/
                        CheckAppPurchaseByDevice checkAppPurchaseByDevice=new CheckAppPurchaseByDevice();
                        checkAppPurchaseByDevice.execute("");
                    }else{
                        Toast.makeText(context,"Please Turn On your Internet Connection!",Toast.LENGTH_LONG).show();
                    }
                    showDialog.dismiss();
                }
            }
        });
    }

    public class CheckAppPurchaseByDevice extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        Boolean isExistingAppPurchase=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpHandler sh = new HttpHandler();
                String jsonStrAppPurchaseOrder = "{\"AppPurchaseOrder\":"+ sh.makeServiceCall(ServiceURL.APPPURCHASEORDER+"?macAddress='"+macAddress+"'" )+ "}";

                try {
                    if (jsonStrAppPurchaseOrder != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrAppPurchaseOrder);
                        JSONArray jsonArray = jsonObject.getJSONArray("AppPurchaseOrder");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            appPurchaseOrderId=c.getInt("ID");
                            appPurchaseOrderMacAddress = c.getString("MacAddress");
                            break;
                        }
                        if(appPurchaseOrderId!=0) {
                            if (appPurchaseOrderMacAddress.equals(macAddress)) isExistingAppPurchase = true;
                        }
                    }
                    isSuccess = true;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (Exception ex){
                message=ex.getMessage();
            }

            return message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                if(isExistingAppPurchase){
                    Toast.makeText(context,"Dear customer, you have already purchased OnePOS. We will contact you soon.",Toast.LENGTH_LONG).show();
                }else{
                    UploadAppPurchaseOrder uploadAppPurchaseOrder=new UploadAppPurchaseOrder();
                    uploadAppPurchaseOrder.execute("");
                }
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadAppPurchaseOrder extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.APPPURCHASEORDER);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"MacAddress\":"+"\""+macAddress+"\",\"Name\":"+"\""+name+"\",\"MobileNumber\":"+"\""+mobileNumber+"\",\"PurchasePlan\":"+"\""+selectedPlan+"\",\"Amount\":"+"\""+selectedPlanPrice+"\"}";
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }
                isSuccess=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(!isSuccess) Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            else{
                Intent i=new Intent(context,AppPurchaseSuccessActivity.class);
                i.putExtra("SelectedPlan",selectedPlan);
                i.putExtra("SelectedPlanPrice",selectedPlanPrice);
                startActivity(i);
                finish();
            }
        }
    }

    private void setLayoutResource(){
        btnPurchase=findViewById(R.id.btnPurchase);
        btnContactUs=findViewById(R.id.btnContactUs);
        layoutMonthlyPlan=findViewById(R.id.layoutMonthlyPlan);
        layoutYearlyPlan=findViewById(R.id.layoutYearlyPlan);
        layoutCustomizedPlan=findViewById(R.id.layoutCustomizedPlan);
        tvMonthlyPlan=findViewById(R.id.tvMonthlyPlan);
        tvMonthlyPlanPrice=findViewById(R.id.tvMonthlyPlanPrice);
        tvYearlyPlan=findViewById(R.id.tvYearlyPlan);
        tvYearlyPlanPrice=findViewById(R.id.tvYearlyPlanPrice);
        tvCustomizedPlan=findViewById(R.id.tvCustomizedPlan);
        tvCustomizedPlanPrice=findViewById(R.id.tvCustomizedPlanPrice);

        tvMonthlyPlan.setText(systemInfo.MonthlyPlan);
        tvMonthlyPlanPrice.setText(systemInfo.MonthlyPlanPrice);
        tvYearlyPlan.setText(systemInfo.YearlyPlan);
        tvYearlyPlanPrice.setText(systemInfo.YearlyPlanPrice);
        tvCustomizedPlan.setText(systemInfo.CustomizedPlan);
        tvCustomizedPlanPrice.setText(systemInfo.CustomizedPlanPrice);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }
}
