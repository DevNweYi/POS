package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import common.ServiceURL;
import common.SystemInfo;
import database.DatabaseAccess;
import model.CategoryModel;
import model.CustomerModel;
import model.BillSettingModel;
import model.MasterAdjustmentModel;
import model.MasterPurchaseModel;
import model.MasterSaleModel;
import model.ProductModel;
import model.ProductBalanceModel;
import model.ShopSettingModel;
import model.SupplierModel;
import model.TranAdjustmentModel;
import model.TranPurchaseModel;
import model.TranSaleModel;
import model.UnitModel;
import model.UserModel;
import model.VoucherNumberModel;

public class SettingActivity extends AppCompatActivity {

    LinearLayout layoutDataBackup,layoutDataRestore,layoutBackupDataCloud,layoutRestoreDataCloud;
    TextView tvShopSetting,tvPrinterSetting,tvBillSetting,tvUploadData,tvVoucherFormat;
    DatabaseAccess db;
    Context context=this;
    int status;
    final int StatusBackup =1, StatusRestore =2;
    private ProgressDialog progressDialog;
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_setting);

        layoutDataBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = StatusBackup;
                showImportExportDialog();
            }
        });
        layoutDataRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = StatusRestore;
                showImportExportDialog();
            }
        });
        layoutBackupDataCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,getResources().getString(R.string.still_develop),Toast.LENGTH_LONG).show();
            }
        });
        layoutRestoreDataCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,getResources().getString(R.string.still_develop),Toast.LENGTH_LONG).show();
            }
        });
        tvVoucherFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,getResources().getString(R.string.still_develop),Toast.LENGTH_LONG).show();
            }
        });
        tvShopSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ShopSettingActivity.class);
                startActivity(i);
            }
        });
        tvPrinterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PrinterListActivity.class);
                startActivity(i);
            }
        });
        tvBillSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,BillSettingActivity.class);
                startActivity(i);
            }
        });
        tvUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadCategory uploadCategory=new UploadCategory();
                uploadCategory.execute("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ExportData extends AsyncTask<String ,String, String> {
        String message="";
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting data...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args){
            File exportDir = new File(Environment.getExternalStorageDirectory().getPath()+context.getResources().getString(R.string.folder_name), "DataBackup");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File fileCategory = new File(exportDir, "Category.csv");
            File fileCustomer = new File(exportDir, "Customer.csv");
            File fileUnit = new File(exportDir, "Unit.csv");
            File fileUser = new File(exportDir, "User.csv");
            File fileProduct = new File(exportDir, "Product.csv");
            File fileSupplier = new File(exportDir, "Supplier.csv");
            File fileShopSetting = new File(exportDir, "ShopSetting.csv");
            File fileProductBalance = new File(exportDir, "ProductBalance.csv");
            File fileBillSetting = new File(exportDir, "BillSetting.csv");

            try {
                /** for Category **/
                fileCategory.createNewFile();
                CSVWriter csvWriteCategory = new CSVWriter(new FileWriter(fileCategory));
                List<CategoryModel> lstCategory = db.getCategoryTableData();
                String headerCategory[] = {"CategoryID", "CategoryName"};
                csvWriteCategory.writeNext(headerCategory);
                for (int i = 0; i < lstCategory.size(); i++) {
                    String data[] = {String.valueOf(lstCategory.get(i).getCategoryId()), lstCategory.get(i).getCategoryName()};
                    csvWriteCategory.writeNext(data);
                }
                csvWriteCategory.close();

                /** for Customer **/
                fileCustomer.createNewFile();
                CSVWriter csvWriteCustomer = new CSVWriter(new FileWriter(fileCustomer));
                List<CustomerModel> lstCustomer = db.getCustomerTableData();
                String headerCustomer[] = {"CustomerID", "CustomerName", "MobileNumber", "OtherMobileNumber", "Address", "IsAllowCredit", "DebtAmount","ContactName"};
                csvWriteCustomer.writeNext(headerCustomer);
                for (int i = 0; i < lstCustomer.size(); i++) {
                    String data[] = {String.valueOf(lstCustomer.get(i).getCustomerId()), lstCustomer.get(i).getCustomerName(), lstCustomer.get(i).getMobileNumber(), lstCustomer.get(i).getOtherMobileNumber(), lstCustomer.get(i).getAddress(),String.valueOf(lstCustomer.get(i).getIsAllowCredit()),String.valueOf(lstCustomer.get(i).getDebtAmount()),lstCustomer.get(i).getContactName()};
                    csvWriteCustomer.writeNext(data);
                }
                csvWriteCustomer.close();

                /** for Supplier **/
                fileSupplier.createNewFile();
                CSVWriter csvWriteSupplier = new CSVWriter(new FileWriter(fileSupplier));
                List<SupplierModel> lstSupplier = db.getSupplierTableData();
                String headerSupplier[] = {"SupplierID", "SupplierName", "MobileNumber", "OtherMobileNumber", "Address", "IsAllowCredit", "DebtAmount","ContactName"};
                csvWriteSupplier.writeNext(headerSupplier);
                for (int i = 0; i < lstSupplier.size(); i++) {
                    String data[] = {String.valueOf(lstSupplier.get(i).getSupplierId()), lstSupplier.get(i).getSupplierName(), lstSupplier.get(i).getMobileNumber(), lstSupplier.get(i).getOtherMobileNumber(), lstSupplier.get(i).getAddress(),String.valueOf(lstSupplier.get(i).getIsAllowCredit()),String.valueOf(lstSupplier.get(i).getDebtAmount()),lstSupplier.get(i).getContactName()};
                    csvWriteSupplier.writeNext(data);
                }
                csvWriteSupplier.close();

                /** for Unit **/
                fileUnit.createNewFile();
                CSVWriter csvWriteUnit = new CSVWriter(new FileWriter(fileUnit));
                List<UnitModel> lstUnit = db.getUnitTableData();
                String headerUnit[] = {"UnitID", "UnitName", "UnitKeyword"};
                csvWriteUnit.writeNext(headerUnit);
                for (int i = 0; i < lstUnit.size(); i++) {
                    String data[] = {String.valueOf(lstUnit.get(i).getUnitId()), lstUnit.get(i).getUnitName(), lstUnit.get(i).getUnitKeyword()};
                    csvWriteUnit.writeNext(data);
                }
                csvWriteUnit.close();

                /** for User **/
                fileUser.createNewFile();
                CSVWriter csvWriteUser = new CSVWriter(new FileWriter(fileUser));
                List<UserModel> lstUser = db.getUserTableData();
                String headerUser[] = {"UserID", "UserName","MobileNumber","Password"};
                csvWriteUser.writeNext(headerUser);
                for (int i = 0; i < lstUser.size(); i++) {
                    String data[] = {String.valueOf(lstUser.get(i).getUserId()), lstUser.get(i).getUserName(), lstUser.get(i).getMobileNumber(), lstUser.get(i).getPassword()};
                    csvWriteUser.writeNext(data);
                }
                csvWriteUser.close();

                /** for Product **/
                fileProduct.createNewFile();
                CSVWriter csvWriteProduct = new CSVWriter(new FileWriter(fileProduct));
                List<ProductModel> lstProduct = db.getProductTableData();
                String headerProduct[] = {"ProductID", "ProductCode", "ProductName", "CategoryID", "SalePrice", "PurPrice", "IsTrackStock", "TrackStock", "IsProductUnit", "Quantity", "StandardUnitID", "SaleUnitID", "SaleUnitQty", "SaleStanUnitQty", "SaleUnitSalePrice", "SaleUnitPurPrice", "PurUnitID", "PurUnitQty", "PurStanUnitQty", "PurUnitSalePrice", "PurUnitPurPrice"};
                csvWriteProduct.writeNext(headerProduct);
                for (int i = 0; i < lstProduct.size(); i++) {
                    String standardUnitId=String.valueOf(lstProduct.get(i).getStandardUnitId());
                    String saleUnitId=String.valueOf(lstProduct.get(i).getSaleUnitId());
                    String saleUnitQty=String.valueOf(lstProduct.get(i).getSaleUnitQty());
                    String saleStanUnitQty=String.valueOf(lstProduct.get(i).getStandardSaleUnitQty());
                    String saleUnitSalePrice=String.valueOf(lstProduct.get(i).getSaleUnitSalePrice());
                    String saleUnitPurPrice=String.valueOf(lstProduct.get(i).getSaleUnitPurPrice());
                    String purUnitId=String.valueOf(lstProduct.get(i).getPurUnitId());
                    String purUnitQty=String.valueOf(lstProduct.get(i).getPurUnitQty());
                    String purStanUnitQty=String.valueOf(lstProduct.get(i).getStandardPurUnitQty());
                    String purUnitSalePrice=String.valueOf(lstProduct.get(i).getPurUnitSalePrice());
                    String purUnitPurPrice=String.valueOf(lstProduct.get(i).getPurUnitPurPrice());
                    String data[] = {String.valueOf(lstProduct.get(i).getProductId()), lstProduct.get(i).getProductCode(), lstProduct.get(i).getProductName(), String.valueOf(lstProduct.get(i).getCategoryId()), String.valueOf(lstProduct.get(i).getSalePrice()),String.valueOf(lstProduct.get(i).getPurPrice()),String.valueOf(lstProduct.get(i).getIsTrackStock()),String.valueOf(lstProduct.get(i).getTrackStock()),String.valueOf(lstProduct.get(i).getIsProductUnit()),String.valueOf(lstProduct.get(i).getOpeningQuantity()),standardUnitId,saleUnitId,saleUnitQty,saleStanUnitQty,saleUnitSalePrice,saleUnitPurPrice,purUnitId,purUnitQty,purStanUnitQty,purUnitSalePrice,purUnitPurPrice};
                    csvWriteProduct.writeNext(data);
                }
                csvWriteProduct.close();

                /** for Product Balance **/
                fileProductBalance.createNewFile();
                CSVWriter csvWriteProductBalance = new CSVWriter(new FileWriter(fileProductBalance));
                List<ProductBalanceModel> lstProductQuantity = db.getProductBalanceTableData();
                String headerProductBalance[] = {"ID", "ProductID", "UnitID","Quantity"};
                csvWriteProductBalance.writeNext(headerProductBalance);
                for (int i = 0; i < lstProductQuantity.size(); i++) {
                    String data[] = {String.valueOf(lstProductQuantity.get(i).getId()), String.valueOf(lstProductQuantity.get(i).getProductId()), String.valueOf(lstProductQuantity.get(i).getUnitId()),String.valueOf(lstProductQuantity.get(i).getQuantity())};
                    csvWriteProductBalance.writeNext(data);
                }
                csvWriteProductBalance.close();

                /** for Shop Setting **/
                fileShopSetting.createNewFile();
                CSVWriter csvWriteShopSetting = new CSVWriter(new FileWriter(fileShopSetting));
                ShopSettingModel shopSettingModel = db.getShopSettingTableData();
                String headerShopSetting[] = {"ID", "Logo", "ShopName","Mobile","Address","CurrencySymbol","Description","OtherMobile"};
                csvWriteShopSetting.writeNext(headerShopSetting);
                String data[] = {String.valueOf(shopSettingModel.getId()),String.valueOf(shopSettingModel.getLogo()),shopSettingModel.getShopName(),shopSettingModel.getMobile(),shopSettingModel.getAddress(),shopSettingModel.getCurrencySymbol(),shopSettingModel.getDescription(),shopSettingModel.getOtherMobile()};
                csvWriteShopSetting.writeNext(data);
                csvWriteShopSetting.close();
                db.insertUpdateTempShopLogo(shopSettingModel.getLogo());

                /** for Bill Setting **/
                fileBillSetting.createNewFile();
                CSVWriter csvWriteBillSetting = new CSVWriter(new FileWriter(fileBillSetting));
                BillSettingModel billSettingModel = db.getBillSettingTableData();
                String headerBillSetting[] = {"ID", "FooterMessage1", "FooterMessage2","Remark"};
                csvWriteBillSetting.writeNext(headerBillSetting);
                String data1[] = {String.valueOf(billSettingModel.getId()), billSettingModel.getFooterMessage1(), billSettingModel.getFooterMessage2(),billSettingModel.getRemark()};
                csvWriteBillSetting.writeNext(data1);
                csvWriteBillSetting.close();

            } catch (IOException e) {
                message=e.getMessage();
            }

            return message;
        }

        @Override
        protected void onPostExecute(final String success) {
            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (message.isEmpty()){
                Toast.makeText(context,R.string.export_data_success,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ImportData extends AsyncTask<String ,String, String> {
        String message="";
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Importing data...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args){
            message=systemInfo.dataRestore(db,context);
            return message;
        }

        @Override
        protected void onPostExecute(final String success) {
            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (message.isEmpty()){
                Toast.makeText(context,R.string.import_data_success,Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context,LoginActivity.class);
                startActivity(i);
                finish();
            }
            else {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ExportTran extends AsyncTask<String ,String, String> {
        String message="";
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting transaction...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args){
            File exportDir = new File(Environment.getExternalStorageDirectory().getPath()+context.getResources().getString(R.string.folder_name), "TransactionBackup");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File fileTMasterSale = new File(exportDir, "TMasterSale.csv");
            File fileTTranSale = new File(exportDir, "TTranSale.csv");
            File fileTMasterPurchase = new File(exportDir, "TMasterPurchase.csv");
            File fileTTranPurchase = new File(exportDir, "TTranPurchase.csv");
            File fileTMasterAdjustment = new File(exportDir, "TMasterAdjustment.csv");
            File fileTTranAdjustment = new File(exportDir, "TTranAdjustment.csv");
            File fileTMasterOpenBill = new File(exportDir, "TMasterOpenBill.csv");
            File fileTTranOpenBill = new File(exportDir, "TTranOpenBill.csv");
            File fileVoucherNumber = new File(exportDir, "VoucherNumber.csv");

            try {
                /** for Master Sale **/
                fileTMasterSale.createNewFile();
                CSVWriter csvWriteMasterSale = new CSVWriter(new FileWriter(fileTMasterSale));
                List<MasterSaleModel> lstMasterSale = db.getMasterSaleTableData();
                String headerMasterSale[] = {"MasterSaleID", "VoucherNo","SaleDate", "SaleTime","UserID", "UserName","CustomerID", "CustomerName","TotalAmount", "PayDisPercent","PayDisAmount", "TotalDisAmount","NetAmount", "LastDebtAmount","PaidAmount", "ChangeAmount","DebtAmount", "IsDebtAmount","IsCredit","CreditRemark"};
                csvWriteMasterSale.writeNext(headerMasterSale);
                for (int i = 0; i < lstMasterSale.size(); i++) {
                    String data[] = {String.valueOf(lstMasterSale.get(i).getId()), String.valueOf(lstMasterSale.get(i).getVoucherNumber()),lstMasterSale.get(i).getDate(),lstMasterSale.get(i).getTime(), String.valueOf(lstMasterSale.get(i).getUserId()), lstMasterSale.get(i).getUserName(), String.valueOf(lstMasterSale.get(i).getCustomerId()), lstMasterSale.get(i).getCustomerName(), String.valueOf(lstMasterSale.get(i).getTotalAmount()), String.valueOf(lstMasterSale.get(i).getPayDisPercent()), String.valueOf(lstMasterSale.get(i).getPayDisAmount()), String.valueOf(lstMasterSale.get(i).getTotalDisAmount()), String.valueOf(lstMasterSale.get(i).getNetAmount()), String.valueOf(lstMasterSale.get(i).getLastDebtAmount()), String.valueOf(lstMasterSale.get(i).getPaidAmount()), String.valueOf(lstMasterSale.get(i).getChangeAmount()), String.valueOf(lstMasterSale.get(i).getDebtAmount()), String.valueOf(lstMasterSale.get(i).getIsDebtAmount()), String.valueOf(lstMasterSale.get(i).getIsCredit()),lstMasterSale.get(i).getCreditRemark()};
                    csvWriteMasterSale.writeNext(data);
                }
                csvWriteMasterSale.close();

                /** for Tran Sale **/
                fileTTranSale.createNewFile();
                CSVWriter csvWriteTranSale = new CSVWriter(new FileWriter(fileTTranSale));
                List<TranSaleModel> lstTranSale = db.getTranSaleTableData();
                String headerTranSale[] = {"TranSaleID", "MasterSaleID", "ProductID", "ProductName", "SalePrice", "Quantity", "IsProductUnit", "UnitID", "UnitKeyword", "Amount", "UnitType"};
                csvWriteTranSale.writeNext(headerTranSale);
                for (int i = 0; i < lstTranSale.size(); i++) {
                    String data[] = {String.valueOf(lstTranSale.get(i).getId()), String.valueOf(lstTranSale.get(i).getMasterSaleId()), String.valueOf(lstTranSale.get(i).getProductId()), lstTranSale.get(i).getProductName(), String.valueOf(lstTranSale.get(i).getSalePrice()),String.valueOf(lstTranSale.get(i).getQuantity()),String.valueOf(lstTranSale.get(i).getIsProductUnit()),String.valueOf(lstTranSale.get(i).getUnitId()),lstTranSale.get(i).getUnitKeyword(),String.valueOf(lstTranSale.get(i).getAmount()),lstTranSale.get(i).getUnitType()};
                    csvWriteTranSale.writeNext(data);
                }
                csvWriteTranSale.close();

                /** for Master Purchase **/
                fileTMasterPurchase.createNewFile();
                CSVWriter csvWriteMasterPurchase = new CSVWriter(new FileWriter(fileTMasterPurchase));
                List<MasterPurchaseModel> lstMasterPurchase = db.getMasterPurchaseTableData();
                String headerMasterPurchase[] = {"MasterPurchaseID", "VoucherNo","PurchaseDate", "PurchaseTime","UserID", "UserName","SupplierID", "SupplierName","TotalAmount", "PayDisPercent","PayDisAmount", "TotalDisAmount","NetAmount", "LastDebtAmount","PaidAmount", "ChangeAmount","DebtAmount", "IsDebtAmount","IsCredit","CreditRemark"};
                csvWriteMasterPurchase.writeNext(headerMasterPurchase);
                for (int i = 0; i < lstMasterPurchase.size(); i++) {
                    String data[] = {String.valueOf(lstMasterPurchase.get(i).getId()), String.valueOf(lstMasterPurchase.get(i).getVoucherNumber()),lstMasterPurchase.get(i).getDate(),lstMasterPurchase.get(i).getTime(), String.valueOf(lstMasterPurchase.get(i).getUserId()), lstMasterPurchase.get(i).getUserName(), String.valueOf(lstMasterPurchase.get(i).getSupplierId()), lstMasterPurchase.get(i).getSupplierName(), String.valueOf(lstMasterPurchase.get(i).getTotalAmount()), String.valueOf(lstMasterPurchase.get(i).getPayDisPercent()), String.valueOf(lstMasterPurchase.get(i).getPayDisAmount()), String.valueOf(lstMasterPurchase.get(i).getTotalDisAmount()), String.valueOf(lstMasterPurchase.get(i).getNetAmount()), String.valueOf(lstMasterPurchase.get(i).getLastDebtAmount()), String.valueOf(lstMasterPurchase.get(i).getPaidAmount()), String.valueOf(lstMasterPurchase.get(i).getChangeAmount()), String.valueOf(lstMasterPurchase.get(i).getDebtAmount()), String.valueOf(lstMasterPurchase.get(i).getIsDebtAmount()), String.valueOf(lstMasterSale.get(i).getIsCredit()),lstMasterSale.get(i).getCreditRemark()};
                    csvWriteMasterPurchase.writeNext(data);
                }
                csvWriteMasterPurchase.close();

                /** for Tran Purchase **/
                fileTTranPurchase.createNewFile();
                CSVWriter csvWriteTranPurchase = new CSVWriter(new FileWriter(fileTTranPurchase));
                List<TranPurchaseModel> lstTranPurchase = db.getTranPurchaseTableData();
                String headerTranPurchase[] = {"TranPurchaseID", "MasterPurchaseID", "ProductID", "ProductName", "PurchasePrice", "Quantity", "IsProductUnit", "UnitID", "UnitKeyword", "Amount", "UnitType"};
                csvWriteTranPurchase.writeNext(headerTranPurchase);
                for (int i = 0; i < lstTranPurchase.size(); i++) {
                    String data[] = {String.valueOf(lstTranPurchase.get(i).getId()), String.valueOf(lstTranPurchase.get(i).getMasterPurchaseId()), String.valueOf(lstTranPurchase.get(i).getProductId()), lstTranPurchase.get(i).getProductName(), String.valueOf(lstTranPurchase.get(i).getPurPrice()),String.valueOf(lstTranPurchase.get(i).getQuantity()),String.valueOf(lstTranPurchase.get(i).getIsProductUnit()),String.valueOf(lstTranPurchase.get(i).getUnitId()),lstTranPurchase.get(i).getUnitKeyword(),String.valueOf(lstTranPurchase.get(i).getAmount()),lstTranPurchase.get(i).getUnitType()};
                    csvWriteTranPurchase.writeNext(data);
                }
                csvWriteTranPurchase.close();

                /** for Master Adjustment **/
                fileTMasterAdjustment.createNewFile();
                CSVWriter csvWriteMasterAdjustment = new CSVWriter(new FileWriter(fileTMasterAdjustment));
                List<MasterAdjustmentModel> lstMasterAdjustment = db.getMasterAdjustmentTableData();
                String headerMasterAdjustment[] = {"MasterAdjustmentID", "AdjustmentDate", "AdjustmentTime","UserID", "UserName","TotalAmount", "Remark"};
                csvWriteMasterAdjustment.writeNext(headerMasterAdjustment);
                for (int i = 0; i < lstMasterAdjustment.size(); i++) {
                    String data[] = {String.valueOf(lstMasterAdjustment.get(i).getMasterAdjustmentId()),lstMasterAdjustment.get(i).getDate(),lstMasterAdjustment.get(i).getTime(), String.valueOf(lstMasterAdjustment.get(i).getUserId()), lstMasterAdjustment.get(i).getUserName(), String.valueOf(lstMasterAdjustment.get(i).getTotalAmount()), String.valueOf(lstMasterAdjustment.get(i).getRemark())};
                    csvWriteMasterAdjustment.writeNext(data);
                }
                csvWriteMasterAdjustment.close();

                /** for Tran Adjustment **/
                fileTTranAdjustment.createNewFile();
                CSVWriter csvWriteTranAdjustment = new CSVWriter(new FileWriter(fileTTranAdjustment));
                List<TranAdjustmentModel> lstTranAdjustment = db.getTranAdjustmentTableData();
                String headerTranAdjustment[] = {"TranAdjustmentID", "MasterAdjustmentID", "ProductID", "ProductName", "PurchasePrice", "Quantity", "IsProductUnit", "UnitID", "UnitKeyword", "Amount", "UnitType"};
                csvWriteTranAdjustment.writeNext(headerTranAdjustment);
                for (int i = 0; i < lstTranAdjustment.size(); i++) {
                    String data[] = {String.valueOf(lstTranAdjustment.get(i).getTranAdjustmentId()), String.valueOf(lstTranAdjustment.get(i).getMasterAdjustmentId()), String.valueOf(lstTranAdjustment.get(i).getProductId()), lstTranAdjustment.get(i).getProductName(), String.valueOf(lstTranAdjustment.get(i).getPurPrice()),String.valueOf(lstTranAdjustment.get(i).getQuantity()),String.valueOf(lstTranAdjustment.get(i).getIsProductUnit()),String.valueOf(lstTranAdjustment.get(i).getUnitId()),lstTranAdjustment.get(i).getUnitKeyword(),String.valueOf(lstTranAdjustment.get(i).getAmount()),lstTranAdjustment.get(i).getUnitType()};
                    csvWriteTranAdjustment.writeNext(data);
                }
                csvWriteTranAdjustment.close();

                /** for Master Opened Bill **/
                fileTMasterOpenBill.createNewFile();
                CSVWriter csvWriteMasterOpenBill = new CSVWriter(new FileWriter(fileTMasterOpenBill));
                List<MasterSaleModel> lstMasterOpenBill = db.getMasterOpenBillTableData();
                String headerMasterOpenBill[] = {"OpenBillID", "VoucherNo","OpenDate", "OpenTime","UserID", "UserName","CustomerID", "CustomerName","TotalAmount","Remark", "PayDisPercent","PayDisAmount", "TotalDisAmount","NetAmount"};
                csvWriteMasterOpenBill.writeNext(headerMasterOpenBill);
                for (int i = 0; i < lstMasterOpenBill.size(); i++) {
                    String data[] = {String.valueOf(lstMasterOpenBill.get(i).getId()), String.valueOf(lstMasterOpenBill.get(i).getVoucherNumber()),lstMasterOpenBill.get(i).getDate(),lstMasterOpenBill.get(i).getTime(), String.valueOf(lstMasterOpenBill.get(i).getUserId()), lstMasterOpenBill.get(i).getUserName(), String.valueOf(lstMasterOpenBill.get(i).getCustomerId()), lstMasterOpenBill.get(i).getCustomerName(), String.valueOf(lstMasterOpenBill.get(i).getTotalAmount()),lstMasterOpenBill.get(i).getOpenBillRemark(), String.valueOf(lstMasterOpenBill.get(i).getPayDisPercent()), String.valueOf(lstMasterOpenBill.get(i).getPayDisAmount()), String.valueOf(lstMasterOpenBill.get(i).getTotalDisAmount()), String.valueOf(lstMasterOpenBill.get(i).getNetAmount())};
                    csvWriteMasterOpenBill.writeNext(data);
                }
                csvWriteMasterOpenBill.close();

                /** for Tran Opened Bill **/
                fileTTranOpenBill.createNewFile();
                CSVWriter csvWriteTranOpenBill = new CSVWriter(new FileWriter(fileTTranOpenBill));
                List<TranSaleModel> lstTranOpenBill = db.getTranOpenBillTableData();
                String headerTranOpenBill[] = {"ID", "OpenBillID", "ProductID", "ProductName", "SalePrice", "Quantity", "IsProductUnit", "UnitID", "UnitKeyword", "Amount", "UnitType"};
                csvWriteTranOpenBill.writeNext(headerTranOpenBill);
                for (int i = 0; i < lstTranOpenBill.size(); i++) {
                    String data[] = {String.valueOf(lstTranOpenBill.get(i).getId()), String.valueOf(lstTranOpenBill.get(i).getOpenBillId()), String.valueOf(lstTranOpenBill.get(i).getProductId()), lstTranOpenBill.get(i).getProductName(), String.valueOf(lstTranOpenBill.get(i).getSalePrice()),String.valueOf(lstTranOpenBill.get(i).getQuantity()),String.valueOf(lstTranOpenBill.get(i).getIsProductUnit()),String.valueOf(lstTranOpenBill.get(i).getUnitId()),lstTranOpenBill.get(i).getUnitKeyword(),String.valueOf(lstTranOpenBill.get(i).getAmount()),lstTranOpenBill.get(i).getUnitType()};
                    csvWriteTranOpenBill.writeNext(data);
                }
                csvWriteTranOpenBill.close();

                /** for Voucher Number **/
                fileVoucherNumber.createNewFile();
                CSVWriter csvWriteVoucherNumber = new CSVWriter(new FileWriter(fileVoucherNumber));
                VoucherNumberModel voucherNoModel = db.getVoucherNoTableData();
                String headerVoucherNo[] = {"ID", "SaleVoucherNo", "PurchaseVoucherNo"};
                csvWriteVoucherNumber.writeNext(headerVoucherNo);
                String data[] = {String.valueOf(voucherNoModel.getId()),String.valueOf(voucherNoModel.getSaleVoucherNo()),String.valueOf(voucherNoModel.getPurchaseVoucherNo())};
                csvWriteVoucherNumber.writeNext(data);
                csvWriteVoucherNumber.close();

            } catch (IOException e) {
                message=e.getMessage();
            }

            return message;
        }

        @Override
        protected void onPostExecute(final String success) {
            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (message.isEmpty()){
                Toast.makeText(context,R.string.export_tran_success,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ImportTran extends AsyncTask<String ,String, String> {
        String message="";
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Importing transaction...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args){
            File importDir = new File(Environment.getExternalStorageDirectory().getPath()+context.getResources().getString(R.string.folder_name), "TransactionBackup");
            if (!importDir.exists()) {
                importDir.mkdirs();
            }

            File fileTMasterSale = new File(importDir, "TMasterSale.csv");
            File fileTTranSale = new File(importDir, "TTranSale.csv");
            File fileTMasterPurchase = new File(importDir, "TMasterPurchase.csv");
            File fileTTranPurchase = new File(importDir, "TTranPurchase.csv");
            File fileTMasterAdjustment = new File(importDir, "TMasterAdjustment.csv");
            File fileTTranAdjustment = new File(importDir, "TTranAdjustment.csv");
            File fileTMasterOpenBill = new File(importDir, "TMasterOpenBill.csv");
            File fileTTranOpenBill = new File(importDir, "TTranOpenBill.csv");
            File fileVoucherNumber = new File(importDir, "VoucherNumber.csv");

            try {
                db.truncateTransactionTable();

                CSVReader csvReadMasterSale = new CSVReader(new FileReader(fileTMasterSale));
                String[] nextLineMasterSale;
                csvReadMasterSale.readNext();
                while ((nextLineMasterSale = csvReadMasterSale.readNext()) != null) {
                    int masterSaleId = Integer.parseInt(nextLineMasterSale[0]);
                    int voucherNo = Integer.parseInt(nextLineMasterSale[1]);
                    String date = nextLineMasterSale[2];
                    String time = nextLineMasterSale[3];
                    int userId = Integer.parseInt(nextLineMasterSale[4]);
                    String userName = nextLineMasterSale[5];
                    int customerId = Integer.parseInt(nextLineMasterSale[6]);
                    String customerName = nextLineMasterSale[7];
                    int totalAmt = Integer.parseInt(nextLineMasterSale[8]);
                    int payDisPercent = Integer.parseInt(nextLineMasterSale[9]);
                    int payDisAmount = Integer.parseInt(nextLineMasterSale[10]);
                    int totalDisAmount = Integer.parseInt(nextLineMasterSale[11]);
                    int netAmt = Integer.parseInt(nextLineMasterSale[12]);
                    int lastDebtAmt = Integer.parseInt(nextLineMasterSale[13]);
                    int paidAmt = Integer.parseInt(nextLineMasterSale[14]);
                    int changeAmt = Integer.parseInt(nextLineMasterSale[15]);
                    int debtAmt = Integer.parseInt(nextLineMasterSale[16]);
                    int isDebtAmt = Integer.parseInt(nextLineMasterSale[17]);
                    int isCredit = Integer.parseInt(nextLineMasterSale[18]);
                    String creditRemark = nextLineMasterSale[19];
                    db.importMasterSale(masterSaleId,voucherNo,date,time,userId,userName,customerId,customerName,totalAmt,payDisPercent,payDisAmount,totalDisAmount,netAmt,lastDebtAmt,paidAmt,changeAmt,debtAmt,isDebtAmt,isCredit,creditRemark);
                }

                CSVReader csvReadTranSale = new CSVReader(new FileReader(fileTTranSale));
                String[] nextLineTranSale;
                csvReadTranSale.readNext();
                while ((nextLineTranSale = csvReadTranSale.readNext()) != null) {
                    int tranSaleId = Integer.parseInt(nextLineTranSale[0]);
                    int masterSaleId = Integer.parseInt(nextLineTranSale[1]);
                    int productId = Integer.parseInt(nextLineTranSale[2]);
                    String productName = nextLineTranSale[3];
                    int salePrice = Integer.parseInt(nextLineTranSale[4]);
                    int quantity = Integer.parseInt(nextLineTranSale[5]);
                    int isProductUnit = Integer.parseInt(nextLineTranSale[6]);
                    int unitId = Integer.parseInt(nextLineTranSale[7]);
                    String unitKeyword = nextLineTranSale[8];
                    int amount = Integer.parseInt(nextLineTranSale[9]);
                    String unitType = nextLineTranSale[10];
                    db.importTranSale(tranSaleId,masterSaleId,productId,productName,salePrice,quantity,isProductUnit,unitId,unitKeyword,amount,unitType);
                }

                CSVReader csvReadMasterPurchase = new CSVReader(new FileReader(fileTMasterPurchase));
                String[] nextLineMasterPurchase;
                csvReadMasterPurchase.readNext();
                while ((nextLineMasterPurchase = csvReadMasterPurchase.readNext()) != null) {
                    int masterPurchaseId = Integer.parseInt(nextLineMasterPurchase[0]);
                    int voucherNo = Integer.parseInt(nextLineMasterPurchase[1]);
                    String date = nextLineMasterPurchase[2];
                    String time = nextLineMasterPurchase[3];
                    int userId = Integer.parseInt(nextLineMasterPurchase[4]);
                    String userName = nextLineMasterPurchase[5];
                    int supplierId = Integer.parseInt(nextLineMasterPurchase[6]);
                    String supplierName = nextLineMasterPurchase[7];
                    int totalAmt = Integer.parseInt(nextLineMasterPurchase[8]);
                    int payDisPercent = Integer.parseInt(nextLineMasterPurchase[9]);
                    int payDisAmount = Integer.parseInt(nextLineMasterPurchase[10]);
                    int totalDisAmount = Integer.parseInt(nextLineMasterPurchase[11]);
                    int netAmt = Integer.parseInt(nextLineMasterPurchase[12]);
                    int lastDebtAmt = Integer.parseInt(nextLineMasterPurchase[13]);
                    int paidAmt = Integer.parseInt(nextLineMasterPurchase[14]);
                    int changeAmt = Integer.parseInt(nextLineMasterPurchase[15]);
                    int debtAmt = Integer.parseInt(nextLineMasterPurchase[16]);
                    int isDebtAmt = Integer.parseInt(nextLineMasterPurchase[17]);
                    int isCredit = Integer.parseInt(nextLineMasterPurchase[18]);
                    String creditRemark = nextLineMasterPurchase[19];
                    db.importMasterPurchase(masterPurchaseId,voucherNo,date,time,userId,userName,supplierId,supplierName,totalAmt,payDisPercent,payDisAmount,totalDisAmount,netAmt,lastDebtAmt,paidAmt,changeAmt,debtAmt,isDebtAmt,isCredit,creditRemark);
                }

                CSVReader csvReadTranPurchase = new CSVReader(new FileReader(fileTTranPurchase));
                String[] nextLineTranPurchase;
                csvReadTranPurchase.readNext();
                while ((nextLineTranPurchase = csvReadTranPurchase.readNext()) != null) {
                    int tranPurchaseId = Integer.parseInt(nextLineTranPurchase[0]);
                    int masterPurchaseId = Integer.parseInt(nextLineTranPurchase[1]);
                    int productId = Integer.parseInt(nextLineTranPurchase[2]);
                    String productName = nextLineTranPurchase[3];
                    int purPrice = Integer.parseInt(nextLineTranPurchase[4]);
                    int quantity = Integer.parseInt(nextLineTranPurchase[5]);
                    int isProductUnit = Integer.parseInt(nextLineTranPurchase[6]);
                    int unitId = Integer.parseInt(nextLineTranPurchase[7]);
                    String unitKeyword = nextLineTranPurchase[8];
                    int amount = Integer.parseInt(nextLineTranPurchase[9]);
                    String unitType = nextLineTranPurchase[10];
                    db.importTranPurchase(tranPurchaseId,masterPurchaseId,productId,productName,purPrice,quantity,isProductUnit,unitId,unitKeyword,amount,unitType);
                }

                CSVReader csvReadMasterAdjustment = new CSVReader(new FileReader(fileTMasterAdjustment));
                String[] nextLineMasterAdjustment;
                csvReadMasterAdjustment.readNext();
                while ((nextLineMasterAdjustment = csvReadMasterAdjustment.readNext()) != null) {
                    int masterAdjustmentId = Integer.parseInt(nextLineMasterAdjustment[0]);
                    String date = nextLineMasterAdjustment[1];
                    String time = nextLineMasterAdjustment[2];
                    int userId = Integer.parseInt(nextLineMasterAdjustment[3]);
                    String userName = nextLineMasterAdjustment[4];
                    int totalAmt = Integer.parseInt(nextLineMasterAdjustment[5]);
                    String remark = nextLineMasterAdjustment[6];
                    db.importMasterAdjustment(masterAdjustmentId,date,time,userId,userName,totalAmt,remark);
                }

                CSVReader csvReadTranAdjustment = new CSVReader(new FileReader(fileTTranAdjustment));
                String[] nextLineTranAdjustment;
                csvReadTranAdjustment.readNext();
                while ((nextLineTranAdjustment = csvReadTranAdjustment.readNext()) != null) {
                    int tranAdjustmentId = Integer.parseInt(nextLineTranAdjustment[0]);
                    int masterAdjustmentId = Integer.parseInt(nextLineTranAdjustment[1]);
                    int productId = Integer.parseInt(nextLineTranAdjustment[2]);
                    String productName = nextLineTranAdjustment[3];
                    int purPrice = Integer.parseInt(nextLineTranAdjustment[4]);
                    int quantity = Integer.parseInt(nextLineTranAdjustment[5]);
                    int isProductUnit = Integer.parseInt(nextLineTranAdjustment[6]);
                    int unitId = Integer.parseInt(nextLineTranAdjustment[7]);
                    String unitKeyword = nextLineTranAdjustment[8];
                    int amount = Integer.parseInt(nextLineTranAdjustment[9]);
                    String unitType = nextLineTranAdjustment[10];
                    db.importTranAdjustment(tranAdjustmentId,masterAdjustmentId,productId,productName,purPrice,quantity,isProductUnit,unitId,unitKeyword,amount,unitType);
                }

                CSVReader csvReadMasterOpenBill = new CSVReader(new FileReader(fileTMasterOpenBill));
                String[] nextLineMasterOpenBill;
                csvReadMasterOpenBill.readNext();
                while ((nextLineMasterOpenBill = csvReadMasterOpenBill.readNext()) != null) {
                    int openBillId = Integer.parseInt(nextLineMasterOpenBill[0]);
                    int voucherNo = Integer.parseInt(nextLineMasterOpenBill[1]);
                    String date = nextLineMasterOpenBill[2];
                    String time = nextLineMasterOpenBill[3];
                    int userId = Integer.parseInt(nextLineMasterOpenBill[4]);
                    String userName = nextLineMasterOpenBill[5];
                    int customerId = Integer.parseInt(nextLineMasterOpenBill[6]);
                    String customerName = nextLineMasterOpenBill[7];
                    int totalAmt = Integer.parseInt(nextLineMasterOpenBill[8]);
                    String remark = nextLineMasterOpenBill[9];
                    int payDisPercent = Integer.parseInt(nextLineMasterOpenBill[10]);
                    int payDisAmount = Integer.parseInt(nextLineMasterOpenBill[11]);
                    int totalDisAmount = Integer.parseInt(nextLineMasterOpenBill[12]);
                    int netAmt = Integer.parseInt(nextLineMasterOpenBill[13]);
                    db.importMasterOpenBill(openBillId,voucherNo,date,time,userId,userName,customerId,customerName,totalAmt,remark,payDisPercent,payDisAmount,totalDisAmount,netAmt);
                }

                CSVReader csvReadTranOpenBill = new CSVReader(new FileReader(fileTTranOpenBill));
                String[] nextLineTranOpenBill;
                csvReadTranOpenBill.readNext();
                while ((nextLineTranOpenBill = csvReadTranOpenBill.readNext()) != null) {
                    int id = Integer.parseInt(nextLineTranOpenBill[0]);
                    int openBillId = Integer.parseInt(nextLineTranOpenBill[1]);
                    int productId = Integer.parseInt(nextLineTranOpenBill[2]);
                    String productName = nextLineTranOpenBill[3];
                    int salePrice = Integer.parseInt(nextLineTranOpenBill[4]);
                    int quantity = Integer.parseInt(nextLineTranOpenBill[5]);
                    int isProductUnit = Integer.parseInt(nextLineTranOpenBill[6]);
                    int unitId = Integer.parseInt(nextLineTranOpenBill[7]);
                    String unitKeyword = nextLineTranOpenBill[8];
                    int amount = Integer.parseInt(nextLineTranOpenBill[9]);
                    String unitType = nextLineTranOpenBill[10];
                    db.importTranOpenBill(id,openBillId,productId,productName,salePrice,quantity,isProductUnit,unitId,unitKeyword,amount,unitType);
                }

                CSVReader csvReadVoucherNumber = new CSVReader(new FileReader(fileVoucherNumber));
                String[] nextLineVoucherNumber;
                csvReadVoucherNumber.readNext();
                if ((nextLineVoucherNumber = csvReadVoucherNumber.readNext()) != null) {
                    int id = Integer.parseInt(nextLineVoucherNumber[0]);
                    int saleVoucherNo = Integer.parseInt(nextLineVoucherNumber[1]);
                    int purVoucherNo = Integer.parseInt(nextLineVoucherNumber[2]);
                    db.importVoucherNumber(id,saleVoucherNo,purVoucherNo);
                }

            } catch (IOException e) {
                message=e.getMessage();
            }

            return message;
        }

        @Override
        protected void onPostExecute(final String success) {
            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (message.isEmpty()){
                Toast.makeText(context,R.string.import_tran_success,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class UploadCategory extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.CATEGORY);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<CategoryModel> lstCategory=db.getCategoryTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstCategory.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("CategoryID",lstCategory.get(i).getCategoryId());
                        jsonObject.put("CategoryName",lstCategory.get(i).getCategoryName());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Category...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadUnit uploadUnit=new UploadUnit();
                uploadUnit.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadUnit extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.UNIT);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<UnitModel> lstUnit=db.getUnitTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstUnit.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UnitID",lstUnit.get(i).getUnitId());
                        jsonObject.put("UnitName",lstUnit.get(i).getUnitName());
                        jsonObject.put("UnitKeyword",lstUnit.get(i).getUnitKeyword());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Unit...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadCustomer uploadCustomer=new UploadCustomer();
                uploadCustomer.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadCustomer extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.CUSTOMER);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<CustomerModel> lstCustomer=db.getCustomerTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstCustomer.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("CustomerID",lstCustomer.get(i).getCustomerId());
                        jsonObject.put("CustomerName",lstCustomer.get(i).getCustomerName());
                        jsonObject.put("ContactName",lstCustomer.get(i).getContactName());
                        jsonObject.put("MobileNumber",lstCustomer.get(i).getMobileNumber());
                        jsonObject.put("OtherMobileNumber",lstCustomer.get(i).getOtherMobileNumber());
                        jsonObject.put("Address",lstCustomer.get(i).getAddress());
                        jsonObject.put("IsAllowCredit",lstCustomer.get(i).getIsAllowCredit());
                        jsonObject.put("DebtAmount",lstCustomer.get(i).getDebtAmount());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Customer...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadSupplier uploadSupplier=new UploadSupplier();
                uploadSupplier.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadSupplier extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.SUPPLIER);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<SupplierModel> lstSupplier=db.getSupplierTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstSupplier.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("SupplierID",lstSupplier.get(i).getSupplierId());
                        jsonObject.put("SupplierName",lstSupplier.get(i).getSupplierName());
                        jsonObject.put("ContactName",lstSupplier.get(i).getContactName());
                        jsonObject.put("MobileNumber",lstSupplier.get(i).getMobileNumber());
                        jsonObject.put("OtherMobileNumber",lstSupplier.get(i).getOtherMobileNumber());
                        jsonObject.put("Address",lstSupplier.get(i).getAddress());
                        jsonObject.put("IsAllowCredit",lstSupplier.get(i).getIsAllowCredit());
                        jsonObject.put("DebtAmount",lstSupplier.get(i).getDebtAmount());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Supplier...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadProduct uploadProduct=new UploadProduct();
                uploadProduct.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadProduct extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.PRODUCT);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<ProductModel> lstProduct=db.getProductTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstProduct.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ProductID",lstProduct.get(i).getProductId());
                        jsonObject.put("ProductCode",lstProduct.get(i).getProductCode());
                        jsonObject.put("ProductName",lstProduct.get(i).getProductName());
                        jsonObject.put("CategoryID",lstProduct.get(i).getCategoryId());
                        jsonObject.put("SalePrice",lstProduct.get(i).getSalePrice());
                        jsonObject.put("PurPrice",lstProduct.get(i).getPurPrice());
                        jsonObject.put("IsTrackStock",lstProduct.get(i).getIsTrackStock());
                        jsonObject.put("TrackStock",lstProduct.get(i).getTrackStock());
                        jsonObject.put("IsProductUnit",lstProduct.get(i).getIsProductUnit());
                        jsonObject.put("Quantity",lstProduct.get(i).getOpeningQuantity());
                        jsonObject.put("StandardUnitID",lstProduct.get(i).getStandardUnitId());
                        jsonObject.put("SaleUnitID",lstProduct.get(i).getSaleUnitId());
                        jsonObject.put("SaleUnitQty",lstProduct.get(i).getSaleUnitQty());
                        jsonObject.put("StandardSaleUnitQty",lstProduct.get(i).getStandardSaleUnitQty());
                        jsonObject.put("SaleUnitSalePrice",lstProduct.get(i).getSaleUnitSalePrice());
                        jsonObject.put("SaleUnitPurPrice",lstProduct.get(i).getSaleUnitPurPrice());
                        jsonObject.put("PurUnitID",lstProduct.get(i).getPurUnitId());
                        jsonObject.put("PurUnitQty",lstProduct.get(i).getPurUnitQty());
                        jsonObject.put("StandardPurUnitQty",lstProduct.get(i).getStandardPurUnitQty());
                        jsonObject.put("PurUnitSalePrice",lstProduct.get(i).getPurUnitSalePrice());
                        jsonObject.put("PurUnitPurPrice",lstProduct.get(i).getPurUnitPurPrice());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Product...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadProductBalance uploadProductBalance=new UploadProductBalance();
                uploadProductBalance.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadProductBalance extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.PRODUCTBALANCE);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                List<ProductBalanceModel> lstProductBalance=db.getProductBalanceTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                for (int i = 0; i < lstProductBalance.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ID",lstProductBalance.get(i).getId());
                        jsonObject.put("ProductID",lstProductBalance.get(i).getProductId());
                        jsonObject.put("UnitID",lstProductBalance.get(i).getUnitId());
                        jsonObject.put("Quantity",lstProductBalance.get(i).getQuantity());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Product Balance...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                UploadBillSetting uploadBillSetting=new UploadBillSetting();
                uploadBillSetting.execute("");
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadBillSetting extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.BILLSETTING);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                BillSettingModel billSettingModel=db.getBillSettingTableData();
                JSONObject jsonObject=null;
                JSONArray jsonArray=new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("ID",billSettingModel.getId());
                    jsonObject.put("FooterMessage1",billSettingModel.getFooterMessage1());
                    jsonObject.put("FooterMessage2",billSettingModel.getFooterMessage2());
                    jsonObject.put("Remark",billSettingModel.getRemark());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonArray.toString().getBytes("utf-8");
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
            progressDialog.setMessage("Uploading Bill Setting...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                Toast.makeText(context,"Upload Success!",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showImportExportDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_import_export, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final TextView tvProfileTitle=v.findViewById(R.id.tvProfileTitle);
        final TextView tvImportWarning=v.findViewById(R.id.tvImportWarning);
        final CheckBox chkData=v.findViewById(R.id.chkData);
        final CheckBox chkTransaction=v.findViewById(R.id.chkTransaction);

        if(status == StatusBackup) {
            tvProfileTitle.setText(getResources().getString(R.string.backup_title));
            tvImportWarning.setVisibility(View.GONE);
        }
        else if(status == StatusRestore) {
            tvProfileTitle.setText(getResources().getString(R.string.restore_title));
            tvImportWarning.setVisibility(View.VISIBLE);
        }

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
                if(status == StatusBackup){
                    if(chkTransaction.isChecked()){
                        ExportTran exportTran=new ExportTran();
                        exportTran.execute("");
                    }
                    if(chkData.isChecked()) {
                        ExportData export = new ExportData();
                        export.execute();
                    }
                }else if(status == StatusRestore){
                    if(chkTransaction.isChecked()){
                        ImportTran importTran=new ImportTran();
                        importTran.execute();
                    }
                    if(chkData.isChecked()) {
                        ImportData importData=new ImportData();
                        importData.execute();
                    }
                }
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        layoutDataBackup=findViewById(R.id.layoutDataBackup);
        layoutDataRestore=findViewById(R.id.layoutDataRestore);
        layoutBackupDataCloud=findViewById(R.id.layoutBackupDataCloud);
        layoutRestoreDataCloud=findViewById(R.id.layoutRestoreDataCloud);
        tvShopSetting=findViewById(R.id.tvShopSetting);
        tvPrinterSetting=findViewById(R.id.tvPrinterSetting);
        tvBillSetting=findViewById(R.id.tvBillSetting);
        tvUploadData=findViewById(R.id.tvUploadData);
        tvVoucherFormat=findViewById(R.id.tvVoucherFormat);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        if(systemInfo.isOnlyDeveloper)tvUploadData.setVisibility(View.VISIBLE);
        else tvUploadData.setVisibility(View.GONE);
    }

    /* String jsonInputString = "{\"CategoryName\":"+"\""+lstCategory.get(0).getCategoryName()+"\"}";
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
                }*/
}
