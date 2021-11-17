package com.developerstar.pos;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import common.HttpHandler;
import common.ServiceURL;
import common.SystemInfo;
import database.DatabaseAccess;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH=3000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Context context=this;
    DatabaseAccess db;
    SystemInfo systemInfo=new SystemInfo();
    String macAddress,endUserMacAddress,endUserMobileNumber,endUserName,purchasePlan,purchasePlanPrice,appOrderDate,appStartDate,appEndDate;
    private ProgressDialog progressDialog;
    private String TAG = SplashActivity.class.getSimpleName();
    int endUserSettingId;
    boolean isAppPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                permission();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void permission() {
        if (SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                start();
            }
        } else {
            start();
        }
    }

    private boolean checkPermission(){
        if(SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else{
            int write = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission(){
        if(SDK_INT >= Build.VERSION_CODES.R){
            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(intent,2000);
            }catch (Exception e){
                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(obj,2000);
            }
        }else{
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(storage && read){
                        start();
                    }else{
                        Toast.makeText(context,"Require Storage Permission, Please allow this permission in App SystemSetting.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000){
            if(SDK_INT >= Build.VERSION_CODES.R){
                if(Environment.isExternalStorageManager()){
                    start();
                }
            }
        }
    }

    /*private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }*/

    /* private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Require Storage Permission, Please allow this permission in App SystemSetting.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                } else {
                    Toast.makeText(context,"Require Storage Permission, Please allow this permission in App SystemSetting.",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }*/

    private void start(){
        if(systemInfo.EndUserType==systemInfo.DirectCustomer) {
            if (db.checkIsRegister()) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent intent;
                if(systemInfo.checkConnection(context)) intent = new Intent(SplashActivity.this, RegisterActivity.class);
                else intent = new Intent(SplashActivity.this, NoInternetActivity.class);
                startActivity(intent);
                finish();
            }
        }else if(systemInfo.EndUserType==systemInfo.InDirectCustomer){
            if(db.getIsTrial()==1){   // Trial
               /* if(db.isTrialExpired()){  // check is trial expired
                    if(systemInfo.checkConnection(context)) {
                        macAddress = systemInfo.getMacAddress();
                        CheckPurchaseByDevice checkPurchaseByDevice = new CheckPurchaseByDevice();  // check is app purchase
                        checkPurchaseByDevice.execute("");
                    }else startTrialExpired();
                }else{  // Trial not expired*/
                    main();
//                }
            }else if(db.getIsAppPurchase()==1){  // App Purchase
                if(db.isAppPurchaseExpired()){  // check App Purchase expired
                    //db.updateNotAppPurchase();
                    Intent i=new Intent(SplashActivity.this,AppPurchaseExpiredActivity.class);
                    startActivity(i);
                    finish();
                }else{  // App Purchase not expired
                    main();
                }
            }
            else{
                if(systemInfo.checkConnection(context)) {
                    Intent intent = new Intent(SplashActivity.this, PhoneAuthActivity.class);
                    startActivity(intent);
                    finish();
                    /*  macAddress = systemInfo.getMacAddress();
                    CheckUserByDevice checkUserByDevice = new CheckUserByDevice();
                    checkUserByDevice.execute("");*/
                }else {
                    Intent intent = new Intent(SplashActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    public class CheckPurchaseByDevice extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpHandler sh = new HttpHandler();
                String jsonStrEndUserSetting = "{\"EndUserSetting\":"+ sh.makeServiceCall(ServiceURL.ENDUSERSETTING+"?macAddress='"+macAddress+"'" )+ "}";

                try {
                    if (jsonStrEndUserSetting != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrEndUserSetting);
                        JSONArray jsonArray = jsonObject.getJSONArray("EndUserSetting");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            endUserSettingId=c.getInt("ID");
                            endUserMacAddress = c.getString("MacAddress");
                            endUserMobileNumber = c.getString("MobileNumber");
                            isAppPurchase =c.getBoolean("IsAppPurchase");
                            endUserName = c.getString("Name");
                            purchasePlan = c.getString("PurchasePlan");
                            purchasePlanPrice = c.getString("Amount");
                            appOrderDate = c.getString("OrderDate");
                            appStartDate = c.getString("StartDate");
                            appEndDate = c.getString("EndDate");
                            break;
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
                if(isAppPurchase){
                    db.updateNotTrial();
                    db.insertEndUserSetting(endUserMacAddress,endUserMobileNumber,1,endUserName,purchasePlan, purchasePlanPrice,appOrderDate,appStartDate,appEndDate);
                    main();
                }else startTrialExpired();
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CheckUserByDevice extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        Boolean isExistingEndUser=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpHandler sh = new HttpHandler();
                String jsonStrEndUserSetting = "{\"EndUserSetting\":"+ sh.makeServiceCall(ServiceURL.ENDUSERSETTING+"?macAddress='"+macAddress+"'" )+ "}";

                try {
                    if (jsonStrEndUserSetting != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrEndUserSetting);
                        JSONArray jsonArray = jsonObject.getJSONArray("EndUserSetting");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            endUserSettingId=c.getInt("ID");
                            endUserMacAddress = c.getString("MacAddress");
                            endUserMobileNumber = c.getString("MobileNumber");
                            isAppPurchase =c.getBoolean("IsAppPurchase");
                            endUserName = c.getString("Name");
                            purchasePlan = c.getString("PurchasePlan");
                            purchasePlanPrice = c.getString("Amount");
                            appOrderDate = c.getString("OrderDate");
                            appStartDate = c.getString("StartDate");
                            appEndDate = c.getString("EndDate");
                            break;
                        }
                        if(endUserSettingId!=0) {
                            if (endUserMacAddress.equals(macAddress)) isExistingEndUser = true;
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
                if(isExistingEndUser){  // status important! reinstall app by user
                    if(isAppPurchase){  // check app purchased or not
                        db.updateNotTrial();
                        db.insertEndUserSetting(endUserMacAddress,endUserMobileNumber,1,endUserName,purchasePlan, purchasePlanPrice,appOrderDate,appStartDate,appEndDate);
                        main();
                    }else startTrialExpired();
                }else{   // this is completely new user
                    Intent intent = new Intent(SplashActivity.this, PhoneAuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void main(){
        if(!db.isExistShopSetting()){
            Intent i = new Intent(SplashActivity.this, ShopSettingActivity.class);
            i.putExtra("IsNewUser",true);
            startActivity(i);
            finish();
        } else if (db.checkUserCount()==0) {
            Intent i = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void startTrialExpired(){
        Intent i=new Intent(SplashActivity.this,TrialExpiredActivity.class);
        startActivity(i);
        finish();
    }
}
