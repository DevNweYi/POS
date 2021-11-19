package common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developerstar.pos.AdjustmentSetupActivity;
import com.developerstar.pos.R;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import database.DatabaseAccess;
import model.TranAdjustmentModel;
import model.TranPurchaseModel;

public class SystemInfo {
    public int StartSaleVoucherNumber=100001;
    public int StartPurchaseVoucherNumber=100001;
    //public final String DATE_FORMAT="dd-MM-yyyy";
    public final String DATE_FORMAT="yyyy-MM-dd";
    public final String TIME_FORMAT="hh:mm a";
    public DecimalFormat df= new DecimalFormat("#,###");
    public DecimalFormat df2d= new DecimalFormat("#.##");
    public final String PaperWidth58="58 mm";
    public final String PaperWidth80="80 mm";
    public final String InterfaceEthernet="Ethernet";
    public final String InterfaceBluetooth="Bluetooth";
    public final int DirectCustomer=1;
    public final int InDirectCustomer=2;
    public final int EndUserType=InDirectCustomer;
    public final int SaleModule=1,PurchaseModule=2;
    public final String StandardUnit="Standard Unit",SaleUnit="Sale Unit",PurchaseUnit="Purchase Unit";
    public int calcQtyEditStatus =1, calcPriceEditStatus =2;
    public boolean isOnlyDeveloper=false;
    public final int TrialAllowDay=14;
    public final String MonthlyPlan="Monthly Plan",YearlyPlan="Yearly Plan",CustomizedPlan="Customized Plan";
    public final String MonthlyPlanPrice="MMK 0.00",YearlyPlanPrice="MMK 0.00",CustomizedPlanPrice="Negotiate";

    public String getTodayDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date.trim();
    }

    public String getYesterdayDate(){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public String getCurrentTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String time = timeFormat.format(Calendar.getInstance().getTime());
        return time.trim();
    }

    public String convertTSValToNormal(String val){
        String result;
        if(val.contains(",")){
            String[] arr=val.split(",");
            String first=arr[0];
            String second=arr[1];
            result=first+second;
        }else{
            result=val;
        }
        return result;
    }

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    public void showCalcDialog(final TextView tvQuantity, final TextView tvPrice, final Context context, final int calcEditStatus, final List<TranPurchaseModel> lstTranPurchase,final int position, final List<TranAdjustmentModel> lstTranAdjustment, final TextView tvTotalAmt){
        LayoutInflater li=LayoutInflater.from(context);
        View view=li.inflate(R.layout.dialog_number, null);
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setView(view);

        final EditText etInput= view.findViewById(R.id.etInput);
        final Button btnOk= view.findViewById(R.id.btnOk);
        final Button btnCancel= view.findViewById(R.id.btnCancel);
        final Button btnOne= view.findViewById(R.id.btnOne);
        final Button btnTwo= view.findViewById(R.id.btnTwo);
        final Button btnThree= view.findViewById(R.id.btnThree);
        final Button btnFour= view.findViewById(R.id.btnFour);
        final Button btnFive= view.findViewById(R.id.btnFive);
        final Button btnSix= view.findViewById(R.id.btnSix);
        final Button btnSeven= view.findViewById(R.id.btnSeven);
        final Button btnEight= view.findViewById(R.id.btnEight);
        final Button btnNine= view.findViewById(R.id.btnNine);
        final Button btnZero= view.findViewById(R.id.btnZero);

        if(calcEditStatus == calcPriceEditStatus)etInput.setText(convertTSValToNormal(tvPrice.getText().toString()));

        dialog.setCancelable(true);
        final AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        etInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (etInput.getRight() - etInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etInput.getText().toString().length()!=0){
                            String num=etInput.getText().toString();
                            num=num.substring(0,num.length()-1);
                            etInput.setText(num);
                        }
                    }
                }
                return false;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View arg0) {
                if(etInput.getText().toString().length() == 0){
                    if(calcEditStatus == calcQtyEditStatus) Toast.makeText(context,R.string.please_enter_value,Toast.LENGTH_SHORT).show();
                    else if(calcEditStatus == calcPriceEditStatus) Toast.makeText(context,R.string.please_enter_value,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(calcEditStatus == calcQtyEditStatus) {
                    if(tvQuantity!=null) tvQuantity.setText(etInput.getText().toString());
                    if(lstTranPurchase!=null)lstTranPurchase.get(position).setQuantity(Integer.parseInt(etInput.getText().toString()));
                    if(lstTranAdjustment!=null){
                        AdjustmentSetupActivity adjustmentSetupActivity=new AdjustmentSetupActivity();
                        lstTranAdjustment.get(position).setQuantity(Integer.parseInt(etInput.getText().toString()));
                        adjustmentSetupActivity.calculateTotalAmount(lstTranAdjustment,tvTotalAmt);
                    }
                }else if(calcEditStatus == calcPriceEditStatus){
                    if(tvPrice!=null) tvPrice.setText(String.valueOf(df.format(Double.parseDouble(etInput.getText().toString()))));
                    if(lstTranPurchase!=null)lstTranPurchase.get(position).setPurPrice(Integer.parseInt(etInput.getText().toString()));
                    if(lstTranAdjustment!=null){
                        AdjustmentSetupActivity adjustmentSetupActivity=new AdjustmentSetupActivity();
                        lstTranAdjustment.get(position).setPurPrice(Integer.parseInt(etInput.getText().toString()));
                        adjustmentSetupActivity.calculateTotalAmount(lstTranAdjustment,tvTotalAmt);
                    }
                }
                alertDialog.dismiss();
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnOne.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnOne.getText().toString());
                }
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnTwo.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnTwo.getText().toString());
                }
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnThree.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnThree.getText().toString());
                }
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnFour.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnFour.getText().toString());
                }
            }
        });

        btnFive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnFive.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnFive.getText().toString());
                }
            }
        });

        btnSix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnSix.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnSix.getText().toString());
                }
            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnSeven.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnSeven.getText().toString());
                }
            }
        });

        btnEight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnEight.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnEight.getText().toString());
                }
            }
        });

        btnNine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnNine.getText().toString();
                    etInput.setText(newNum);
                }else{
                    etInput.setText(btnNine.getText().toString());
                }
            }
        });

        btnZero.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(etInput.getText().toString().length()!=0){
                    String num=etInput.getText().toString();
                    String newNum=num+btnZero.getText().toString();
                    etInput.setText(newNum);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });
    }

    public String dataRestore(DatabaseAccess db,Context context){
        String message="";
        File importDir = new File(Environment.getExternalStorageDirectory().getPath()+context.getResources().getString(R.string.folder_name), "DataBackup");
        if (!importDir.exists()) {
            importDir.mkdirs();
        }

        File fileCategory = new File(importDir, "Category.csv");
        File fileCustomer = new File(importDir, "Customer.csv");
        File fileUnit = new File(importDir, "Unit.csv");
        File fileUser = new File(importDir, "User.csv");
        File fileProduct = new File(importDir, "Product.csv");
        File fileSupplier = new File(importDir, "Supplier.csv");
        File fileShopSetting = new File(importDir, "ShopSetting.csv");
        File fileProductBalance = new File(importDir, "ProductBalance.csv");
        File fileBillSetting = new File(importDir, "BillSetting.csv");

        try {
            db.truncateDataTable();

            CSVReader csvReadCategory = new CSVReader(new FileReader(fileCategory));
            String[] nextLineCategory;
            csvReadCategory.readNext();
            while ((nextLineCategory = csvReadCategory.readNext()) != null) {
                int categoryId = Integer.parseInt(nextLineCategory[0]);
                String categoryName = nextLineCategory[1];
                db.importCategory(categoryId,categoryName);
            }

            CSVReader csvReadCustomer = new CSVReader(new FileReader(fileCustomer));
            String[] nextLineCustomer;
            csvReadCustomer.readNext();
            while ((nextLineCustomer = csvReadCustomer.readNext()) != null) {
                int customerId = Integer.parseInt(nextLineCustomer[0]);
                String customerName = nextLineCustomer[1];
                String mobileNum = nextLineCustomer[2];
                String otherMobiNum = nextLineCustomer[3];
                String address = nextLineCustomer[4];
                int isAllowCredit = Integer.parseInt(nextLineCustomer[5]);
                int debtAmount=Integer.parseInt(nextLineCustomer[6]);
                String contactName = nextLineCustomer[7];
                db.importCustomer(customerId,customerName,mobileNum,otherMobiNum,address,isAllowCredit,debtAmount,contactName);
            }

            CSVReader csvReadSupplier = new CSVReader(new FileReader(fileSupplier));
            String[] nextLineSupplier;
            csvReadSupplier.readNext();
            while ((nextLineSupplier = csvReadSupplier.readNext()) != null) {
                int supplierId = Integer.parseInt(nextLineSupplier[0]);
                String supplierName = nextLineSupplier[1];
                String mobileNum = nextLineSupplier[2];
                String otherMobiNum = nextLineSupplier[3];
                String address = nextLineSupplier[4];
                int isAllowCredit = Integer.parseInt(nextLineSupplier[5]);
                int debtAmount=Integer.parseInt(nextLineSupplier[6]);
                String contactName = nextLineSupplier[7];
                db.importSupplier(supplierId,supplierName,mobileNum,otherMobiNum,address,isAllowCredit,debtAmount,contactName);
            }

            CSVReader csvReadUnit = new CSVReader(new FileReader(fileUnit));
            String[] nextLineUnit;
            csvReadUnit.readNext();
            while ((nextLineUnit = csvReadUnit.readNext()) != null) {
                int unitId = Integer.parseInt(nextLineUnit[0]);
                String unitName = nextLineUnit[1];
                String unitKeyword = nextLineUnit[2];
                db.importUnit(unitId,unitName,unitKeyword);
            }

            CSVReader csvReadUser = new CSVReader(new FileReader(fileUser));
            String[] nextLineUser;
            csvReadUser.readNext();
            while ((nextLineUser = csvReadUser.readNext()) != null) {
                int userId = Integer.parseInt(nextLineUser[0]);
                String userName = nextLineUser[1];
                String mobileNumber = nextLineUser[2];
                String password = nextLineUser[3];
                db.importUser(userId,userName,mobileNumber,password);
            }

            CSVReader csvReadProduct = new CSVReader(new FileReader(fileProduct));
            String[] nextLineProduct;
            csvReadProduct.readNext();
            while ((nextLineProduct = csvReadProduct.readNext()) != null) {
                int productId = Integer.parseInt(nextLineProduct[0]);
                String productCode = nextLineProduct[1];
                String productName = nextLineProduct[2];
                int categoryId = Integer.parseInt(nextLineProduct[3]);
                int salePrice = Integer.parseInt(nextLineProduct[4]);
                int purPrice = Integer.parseInt(nextLineProduct[5]);
                int isTrackStock = Integer.parseInt(nextLineProduct[6]);
                int trackStock = Integer.parseInt(nextLineProduct[7]);
                int isProductUnit = Integer.parseInt(nextLineProduct[8]);
                int quantity = Integer.parseInt(nextLineProduct[9]);
                int standardUnitId = Integer.parseInt(nextLineProduct[10]);
                int saleUnitId = Integer.parseInt(nextLineProduct[11]);
                int saleUnitQty = Integer.parseInt(nextLineProduct[12]);
                int saleStanUnitQty = Integer.parseInt(nextLineProduct[13]);
                int saleUnitSalePrice = Integer.parseInt(nextLineProduct[14]);
                int saleUnitPurPrice = Integer.parseInt(nextLineProduct[15]);
                int purUnitId = Integer.parseInt(nextLineProduct[16]);
                int purUnitQty = Integer.parseInt(nextLineProduct[17]);
                int purStanUnitQty = Integer.parseInt(nextLineProduct[18]);
                int purUnitSalePrice = Integer.parseInt(nextLineProduct[19]);
                int purUnitPurPrice = Integer.parseInt(nextLineProduct[20]);
                db.importProduct(productId,productCode, productName, categoryId, salePrice,purPrice,quantity,isTrackStock,trackStock,isProductUnit,standardUnitId,saleUnitId,saleUnitQty,saleStanUnitQty,saleUnitSalePrice,saleUnitPurPrice,purUnitId,purUnitQty,purStanUnitQty,purUnitSalePrice,purUnitPurPrice);
            }

            CSVReader csvReadProductBalance = new CSVReader(new FileReader(fileProductBalance));
            String[] nextLineProductBalance;
            csvReadProductBalance.readNext();
            while ((nextLineProductBalance = csvReadProductBalance.readNext()) != null) {
                int id = Integer.parseInt(nextLineProductBalance[0]);
                int productId = Integer.parseInt(nextLineProductBalance[1]);
                int unitId = Integer.parseInt(nextLineProductBalance[2]);
                double quantity = Double.parseDouble(nextLineProductBalance[3]);
                db.importProductBalance(id, productId, unitId, quantity);
            }

            CSVReader csvReadShopSetting = new CSVReader(new FileReader(fileShopSetting));
            String[] nextLineShopSetting;
            csvReadShopSetting.readNext();
            if ((nextLineShopSetting = csvReadShopSetting.readNext()) != null) {
                int id = Integer.parseInt(nextLineShopSetting[0]);
                byte[] logo =nextLineShopSetting[1].getBytes();
                String shopName=nextLineShopSetting[2];
                String mobile=nextLineShopSetting[3];
                String address=nextLineShopSetting[4];
                String currencySymbol=nextLineShopSetting[5];
                String description=nextLineShopSetting[6];
                String otherMobile=nextLineShopSetting[7];
                db.importShopSetting(id,logo,shopName,mobile,address,currencySymbol,description,otherMobile);

                logo=db.getTempShopLogo();
                db.updateShopLogo(logo);
            }

            CSVReader csvReadBillSetting = new CSVReader(new FileReader(fileBillSetting));
            String[] nextLineBillSetting;
            csvReadBillSetting.readNext();
            if ((nextLineBillSetting = csvReadBillSetting.readNext()) != null) {
                int id = Integer.parseInt(nextLineBillSetting[0]);
                String footerMessage1=nextLineBillSetting[1];
                String footerMessage2=nextLineBillSetting[2];
                String remark=nextLineBillSetting[3];
                db.importBillSetting(id,footerMessage1,footerMessage2,remark);
            }

        } catch (IOException e) {
            message=e.getMessage();
        }
        return message;
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }
}
