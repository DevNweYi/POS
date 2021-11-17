package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import common.HttpHandler;
import common.ServiceURL;
import database.DatabaseAccess;

public class DataChoiceActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    Button btnUseSampleData,btnCreateOwnData;
    private ProgressDialog progressDialog;
    private String TAG = DataChoiceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_choice);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        btnUseSampleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadOnlineData downloadOnlineData=new DownloadOnlineData();
                downloadOnlineData.execute("");
                /*Intent i = new Intent(context, ImportDataActivity.class);
                startActivity(i);
                finish();*/
            }
        });
        btnCreateOwnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"Press above 'Quickly Import the Data' button for getting ready-made online data and testing quickly!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, DataSetupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class DownloadOnlineData extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpHandler sh = new HttpHandler();
                String jsonStrCategory = "{\"Category\":"+ sh.makeServiceCall(ServiceURL.CATEGORY)+"}";
                String jsonStrUnit = "{\"Unit\":"+ sh.makeServiceCall(ServiceURL.UNIT)+"}";
                String jsonStrCustomer = "{\"Customer\":"+ sh.makeServiceCall(ServiceURL.CUSTOMER)+"}";
                String jsonStrSupplier = "{\"Supplier\":"+ sh.makeServiceCall(ServiceURL.SUPPLIER)+"}";
                String jsonStrBillSetting = "{\"BillSetting\":"+ sh.makeServiceCall(ServiceURL.BILLSETTING)+"}";
                String jsonStrProduct = "{\"Product\":"+ sh.makeServiceCall(ServiceURL.PRODUCT)+"}";
                String jsonStrProductBalance = "{\"ProductBalance\":"+ sh.makeServiceCall(ServiceURL.PRODUCTBALANCE)+"}";

                try {
                    if (jsonStrCategory != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrCategory);
                        JSONArray jsonArray = jsonObject.getJSONArray("Category");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int categoryId=c.getInt("CategoryID");
                            String categoryName = c.getString("CategoryName");
                            db.importCategory(categoryId,categoryName);
                        }
                    }
                    if (jsonStrUnit != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrUnit);
                        JSONArray jsonArray = jsonObject.getJSONArray("Unit");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int unitId=c.getInt("UnitID");
                            String unitName = c.getString("UnitName");
                            String unitKeyword = c.getString("UnitKeyword");
                            db.importUnit(unitId,unitName,unitKeyword);
                        }
                    }
                    if (jsonStrCustomer != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrCustomer);
                        JSONArray jsonArray = jsonObject.getJSONArray("Customer");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int customerId=c.getInt("CustomerID");
                            String customerName = c.getString("CustomerName");
                            String contactName = c.getString("ContactName");
                            String mobileNumber = c.getString("MobileNumber");
                            String otherMobileNumber = c.getString("OtherMobileNumber");
                            String address = c.getString("Address");
                            int isAllowCredit = c.getInt("IsAllowCredit");
                            int debtAmount = c.getInt("DebtAmount");
                            db.importCustomer(customerId,customerName,mobileNumber,otherMobileNumber,address,isAllowCredit,debtAmount,contactName);
                        }
                    }
                    if (jsonStrSupplier != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrSupplier);
                        JSONArray jsonArray = jsonObject.getJSONArray("Supplier");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int supplierId=c.getInt("SupplierID");
                            String supplierName = c.getString("SupplierName");
                            String contactName = c.getString("ContactName");
                            String mobileNumber = c.getString("MobileNumber");
                            String otherMobileNumber = c.getString("OtherMobileNumber");
                            String address = c.getString("Address");
                            int isAllowCredit = c.getInt("IsAllowCredit");
                            int debtAmount = c.getInt("DebtAmount");
                            db.importSupplier(supplierId,supplierName,mobileNumber,otherMobileNumber,address,isAllowCredit,debtAmount,contactName);
                        }
                    }
                    if (jsonStrBillSetting != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrBillSetting);
                        JSONArray jsonArray = jsonObject.getJSONArray("BillSetting");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int id=c.getInt("ID");
                            String footerMessage1 = c.getString("FooterMessage1");
                            String footerMessage2 = c.getString("FooterMessage2");
                            String remark = c.getString("Remark");
                            db.importBillSetting(id,footerMessage1,footerMessage2,remark);
                        }
                    }
                    if (jsonStrProduct != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrProduct);
                        JSONArray jsonArray = jsonObject.getJSONArray("Product");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int productId=c.getInt("ProductID");
                            String productCode = c.getString("ProductCode");
                            String productName = c.getString("ProductName");
                            int categoryID = c.getInt("CategoryID");
                            int salePrice = c.getInt("SalePrice");
                            int purPrice = c.getInt("PurPrice");
                            int isTrackStock = c.getInt("IsTrackStock");
                            int trackStock = c.getInt("TrackStock");
                            int isProductUnit = c.getInt("IsProductUnit");
                            int quantity = c.getInt("Quantity");
                            int standardUnitID = c.getInt("StandardUnitID");
                            int saleUnitID = c.getInt("SaleUnitID");
                            int saleUnitQty = c.getInt("SaleUnitQty");
                            int standardSaleUnitQty = c.getInt("StandardSaleUnitQty");
                            int saleUnitSalePrice = c.getInt("SaleUnitSalePrice");
                            int saleUnitPurPrice = c.getInt("SaleUnitPurPrice");
                            int purUnitID = c.getInt("PurUnitID");
                            int purUnitQty = c.getInt("PurUnitQty");
                            int standardPurUnitQty = c.getInt("StandardPurUnitQty");
                            int purUnitSalePrice = c.getInt("PurUnitSalePrice");
                            int purUnitPurPrice = c.getInt("PurUnitPurPrice");
                            db.importProduct(productId,productCode,productName,categoryID,salePrice,purPrice,quantity,isTrackStock,trackStock,isProductUnit,standardUnitID,saleUnitID,saleUnitQty,standardSaleUnitQty,saleUnitSalePrice,saleUnitPurPrice,purUnitID,purUnitQty,standardPurUnitQty,purUnitSalePrice,purUnitPurPrice);
                        }
                    }
                    if (jsonStrProductBalance != null) {
                        JSONObject jsonObject = new JSONObject(jsonStrProductBalance);
                        JSONArray jsonArray = jsonObject.getJSONArray("ProductBalance");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int id=c.getInt("ID");
                            int productID = c.getInt("ProductID");
                            int unitID = c.getInt("UnitID");
                            double quantity = c.getDouble("Quantity");
                            db.importProductBalance(id,productID,unitID,quantity);
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
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setLayoutResource(){
        btnUseSampleData=findViewById(R.id.btnYes);
        btnCreateOwnData=findViewById(R.id.btnCreateData);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }
}
