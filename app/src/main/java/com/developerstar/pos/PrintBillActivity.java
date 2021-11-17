package com.developerstar.pos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andprn.jpos.command.ESCPOSConst;
import com.andprn.jpos.printer.ESCPOSPrinter;
import com.developerstar.pos.bt.BtUtil;
import com.developerstar.pos.print.GPrinterCommand;
import com.developerstar.pos.print.PrintPic;
import com.developerstar.pos.print.PrintQueue;
import com.developerstar.pos.print.PrintUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import adapter.BtDeviceListAdapter;
import adapter.PrintPurVoucherAdapter;
import adapter.PrintSaleVoucherAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.BillSettingModel;
import model.PrintBillModel;
import model.PrinterModel;
import model.ShopSettingModel;
import model.TranPurchaseModel;
import model.TranSaleModel;

public class PrintBillActivity extends AppCompatActivity {

    LinearLayout layoutPrint80,layoutPrint58;
    Button btnPrint;
    public static BluetoothAdapter BA;
    private BtDeviceListAdapter deviceAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private ESCPOSPrinter posPtr;
    PrintSaleVoucherAdapter printSaleVoucherAdapter;
    PrintPurVoucherAdapter printPurVoucherAdapter;
    private final int PRINT_TRAN_SIZE=20;
    DatabaseAccess db;
    final Context context = this;
    SystemInfo systemInfo=new SystemInfo();
    List<PrinterModel> lstPrinter=new ArrayList<>();
    List<TranSaleModel> lstTranSale=new ArrayList<>();
    String shopName,description,phone,address,footerMessage1,footerMessage2,remark,customer,payType,date,supplier;
    int voucherNo,discount,totalAmount,netAmount,lastDebtAmount,paidAmount,changeAmount,debtAmount,isDebtAmount,customerId;
    boolean isSaleVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_bill);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(getResources().getString(R.string.printing));

        BA = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter = new BtDeviceListAdapter(getApplicationContext(), null);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        date=systemInfo.getTodayDate();

        Intent i=getIntent();
        isSaleVoucher=i.getBooleanExtra("IsSaleVoucher",false);
        if(isSaleVoucher) {
            totalAmount=i.getIntExtra("TotalAmount",0);
            discount=i.getIntExtra("Discount",0);
            netAmount=i.getIntExtra("NetAmount",0);
            lastDebtAmount=i.getIntExtra("LastDebtAmount",0);
            paidAmount=i.getIntExtra("PaidAmount",0);
            changeAmount=i.getIntExtra("ChangeAmount",0);
            debtAmount=i.getIntExtra("DebtAmount",0);
            isDebtAmount=i.getIntExtra("IsDebtAmount",0);
            voucherNo=i.getIntExtra("VoucherNo",0);
            payType=i.getStringExtra("PayType");
            customerId=i.getIntExtra("CustomerID",0);
            customer=i.getStringExtra("CustomerName");
            lstTranSale = (List<TranSaleModel>) i.getSerializableExtra("LstSaleProduct");
        }else{
            supplier=i.getStringExtra("SupplierName");
//            lstProduct = (List<ProductModel>) i.getSerializableExtra("LstPurchaseProduct");
        }

        getData();
        if(isSaleVoucher)createSalePrintData(lstTranSale);
//        else createPurPrintData(lstProduct);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPrint.setEnabled(false);
                printByNetworkPrinter();
                BtPrint btPrint = new BtPrint();
                btPrint.execute("");
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

    private void createSalePrintData(List<TranSaleModel> lstTran){
        List<PrintBillModel> lstData=new ArrayList<>();

        while(lstTran.size()>PRINT_TRAN_SIZE) {
            List<TranSaleModel> lstTranSale=new ArrayList<>();
            for (int i = 0; i < PRINT_TRAN_SIZE; i++) {
                TranSaleModel data = lstTran.get(i);
                TranSaleModel curData = new TranSaleModel();
                curData.setProductName(data.getProductName());
                curData.setQuantity(data.getQuantity());
                curData.setUnitId(data.getUnitId());
                curData.setUnitKeyword(data.getUnitKeyword());
                curData.setSalePrice(data.getSalePrice());
                curData.setAmount(data.getAmount());

                lstTranSale.add(curData);
            }

            for (int i = 0; i < PRINT_TRAN_SIZE; i++) {
                lstTran.remove(0);
            }

            PrintBillModel data=new PrintBillModel();
            data.setShopName(shopName);
            data.setDescription(description);
            data.setAddress(address);
            data.setPhone(phone);
            data.setPrintDate("Date: "+ date);
            data.setCustomer("Customer: "+ customer);
            data.setVoucherNo("Voucher No: #"+ voucherNo);
            data.setPayType("Pay Type: "+ payType);
            data.setTotalAmount(totalAmount);
            data.setDiscount(discount);
            data.setNetAmount(netAmount);
            data.setLastDebtAmount(lastDebtAmount);
            data.setDebtAmount(debtAmount);
            data.setPaidAmount(paidAmount);
            data.setChangeAmount(changeAmount);
            data.setIsDebtAmount(isDebtAmount);
            data.setCustomerId(customerId);
            data.setLstTranSale(lstTranSale);
            lstData.add(data);
        }

        if(lstTran.size()<=PRINT_TRAN_SIZE) {
            List<TranSaleModel> lstTranSale=new ArrayList<>();
            for (int i = 0; i < lstTran.size(); i++) {
                TranSaleModel data = lstTran.get(i);
                TranSaleModel curData = new TranSaleModel();
                curData.setProductName(data.getProductName());
                curData.setQuantity(data.getQuantity());
                curData.setUnitId(data.getUnitId());
                curData.setUnitKeyword(data.getUnitKeyword());
                curData.setSalePrice(data.getSalePrice());
                curData.setAmount(data.getAmount());

                lstTranSale.add(curData);
            }
            PrintBillModel data=new PrintBillModel();
            data.setShopName(shopName);
            data.setDescription(description);
            data.setAddress(address);
            data.setPhone(phone);
            data.setPrintDate("Date: "+ date);
            data.setCustomer("Customer: "+ customer);
            data.setVoucherNo("Voucher No: #"+ voucherNo);
            data.setPayType("Pay Type: "+ payType);
            data.setTotalAmount(totalAmount);
            data.setDiscount(discount);
            data.setNetAmount(netAmount);
            data.setLastDebtAmount(lastDebtAmount);
            data.setDebtAmount(debtAmount);
            data.setPaidAmount(paidAmount);
            data.setChangeAmount(changeAmount);
            data.setIsDebtAmount(isDebtAmount);
            data.setFooterMessage1(footerMessage1);
            data.setFooterMessage2(footerMessage2);
            data.setRemark(remark);
            data.setCustomerId(customerId);
            data.setLstTranSale(lstTranSale);
            lstData.add(data);
        }
        printSaleVoucherAdapter =new PrintSaleVoucherAdapter(this,lstData);
        final int adapterCount = printSaleVoucherAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = printSaleVoucherAdapter.getView(i, null, null);
            layoutPrint80.addView(item);
//            layoutPrint58.addView(item);
        }
    }

    private void createPurPrintData(List<TranPurchaseModel> lstTran){
        List<PrintBillModel> lstData=new ArrayList<>();

        while(lstTran.size()>PRINT_TRAN_SIZE) {
            List<TranPurchaseModel> lstTranPurchase=new ArrayList<>();
            for (int i = 0; i < PRINT_TRAN_SIZE; i++) {
                TranPurchaseModel data = lstTran.get(i);
                TranPurchaseModel curData = new TranPurchaseModel();
                curData.setProductName(data.getProductName());
                curData.setQuantity(data.getQuantity());
                curData.setUnitId(data.getUnitId());
                curData.setUnitKeyword(data.getUnitKeyword());

                lstTranPurchase.add(curData);
            }

            for (int i = 0; i < PRINT_TRAN_SIZE; i++) {
                lstTran.remove(0);
            }

            PrintBillModel data=new PrintBillModel();
            data.setShopName(shopName);
            data.setDescription(description);
            data.setAddress(address);
            data.setPhone(phone);
            data.setPrintDate("Date: "+ date);
            data.setSupplier("Supplier: "+ supplier);
            data.setLstTranPurchase(lstTranPurchase);
            lstData.add(data);
        }

        if(lstTran.size()<=PRINT_TRAN_SIZE) {
            List<TranPurchaseModel> lstTranPurchase=new ArrayList<>();
            for (int i = 0; i < lstTran.size(); i++) {
                TranPurchaseModel data = lstTran.get(i);
                TranPurchaseModel curData = new TranPurchaseModel();
                curData.setProductName(data.getProductName());
                curData.setQuantity(data.getQuantity());
                curData.setUnitId(data.getUnitId());
                curData.setUnitKeyword(data.getUnitKeyword());

                lstTranPurchase.add(curData);
            }
            PrintBillModel data=new PrintBillModel();
            data.setShopName(shopName);
            data.setDescription(description);
            data.setAddress(address);
            data.setPhone(phone);
            data.setPrintDate("Date: "+ date);
            data.setSupplier("Supplier: "+ supplier);
            data.setLstTranPurchase(lstTranPurchase);
            lstData.add(data);
        }
        printPurVoucherAdapter =new PrintPurVoucherAdapter(this,lstData);
        final int adapterCount = printPurVoucherAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = printPurVoucherAdapter.getView(i, null, null);
            layoutPrint80.addView(item);
//            layoutPrint58.addView(item);
        }
    }

    private void printByNetworkPrinter() {
        View v;
        int removePosition;
        for (int n = 0; n < lstPrinter.size(); n++) {
            String interfaceName = lstPrinter.get(n).getPrinterInterfaceName();
            String widthName = lstPrinter.get(n).getPaperWidth();
            removePosition = n;
            if (interfaceName.equals(systemInfo.InterfaceEthernet)) {
                for (int i = 0; i < printSaleVoucherAdapter.getCount(); i++) {
                    if (widthName.equals(systemInfo.PaperWidth80)) {
                        v = layoutPrint80.getChildAt(i);
                        if (v != null) {
                            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            v.draw(canvas);
                            savePrintImageToStorage(bitmap);
                            try {
                                print(context, "Printed!");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (widthName.equals(systemInfo.PaperWidth58)) {
                        v = layoutPrint58.getChildAt(i);
                        if (v != null) {
                            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            v.draw(canvas);
                            savePrintImageToStorage(bitmap);
                            try {
                                print(context, "Printed!");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                lstPrinter.remove(removePosition);
            }
        }
    }

    public void print(Context context,String msg) throws IOException {
        posPtr = new ESCPOSPrinter();
        File directory = new File(Environment.getExternalStorageDirectory().getPath(), "/OnePOSDB/BillPrint");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File filePath=new File(directory,"print.png");
        String receiptPath=filePath.toString();
        posPtr.printBitmap(receiptPath, ESCPOSConst.ALIGNMENT_CENTER);
        posPtr.lineFeed(4);
        posPtr.cutPaper();
    }

    private String savePrintImageToStorage(Bitmap bitmapImage){
        File directory = new File(Environment.getExternalStorageDirectory().getPath(), "/OnePOSDB/BillPrint");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File logoPath=new File(directory,"print.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logoPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG,100,fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void getData(){
        ShopSettingModel model = db.getShopSettingTableData();
        shopName=model.getShopName();
        description=model.getDescription();
        phone = (model.getOtherMobile() == null || model.getOtherMobile().length() == 0)? model.getMobile() : model.getMobile() + "," + model.getOtherMobile();
        address=model.getAddress();

        if(isSaleVoucher) {
            BillSettingModel billSettingModel = db.getBillSettingTableData();
            footerMessage1 = billSettingModel.getFooterMessage1();
            footerMessage2 = billSettingModel.getFooterMessage2();
            remark = billSettingModel.getRemark();
        }

        lstPrinter=db.getPrinter();
    }

    /** start bluetooth code **/

    public class BtPrint extends AsyncTask<String,String,String> {
        Boolean isSuccess;
        View v;
        int removePosition;
        String widthName;
        @Override
        protected String doInBackground(String... params) {
            try {
                if(lstPrinter.size()!=0) {
                    for (int n = 0; n < lstPrinter.size(); n++) {
                        String interfaceName = lstPrinter.get(n).getPrinterInterfaceName();
                        String printerAddress = lstPrinter.get(n).getBtPrinterAddress();
                        widthName = lstPrinter.get(n).getPaperWidth();
                        removePosition = n;
                        if (interfaceName.equals(systemInfo.InterfaceBluetooth)) {
                            if (checkBluetoothOn()) {
                                if (checkBluetoothDevice(printerAddress)) {
                                    isSuccess = true;
                                    break;
                                }
                            }
                        }
                    }
                }else {
                    isSuccess = false;
                }
            } catch (Exception ex) {
                isSuccess = false;
            }
            return "";
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected void onPostExecute(String r){
            if(isSuccess){
                for (int i = 0; i < printSaleVoucherAdapter.getCount(); i++) {
                    if (widthName.equals(systemInfo.PaperWidth80)) {
                        v = layoutPrint80.getChildAt(i);
                        if (v != null) {
                            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            v.draw(canvas);
                            savePrintImageToStorage(bitmap);  // created bill print paper
                            printBitmapOrder();
                        }
                    } else if (widthName.equals(systemInfo.PaperWidth58)) {
                        v = layoutPrint58.getChildAt(i);
                        if (v != null) {
                            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            v.draw(canvas);
                            savePrintImageToStorage(bitmap);  // created bill print paper
                            printBitmapOrder();
                        }
                    }
                }
                lstPrinter.remove(removePosition);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BtPrint btPrint=new BtPrint();
                        btPrint.execute("");
                    }
                }, 5000);
            }else{
                finish();
            }
        }
    }

    private void printBitmapOrder() {
        Bitmap bitmap=null;
        File directory = new File(Environment.getExternalStorageDirectory().getPath(), "/OnePOSDB/BillPrint");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File filePath=new File(directory,"print.png");
        if(filePath.exists()) bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
        PrintPic printPic = PrintPic.getInstance();
        printPic.length=0;
        printPic.init(bitmap);
        if (null != bitmap) {
            if (bitmap.isRecycled()) {
                bitmap = null;
            } else {
                bitmap.recycle();
                bitmap = null;
            }
        }
        byte[] bytes = printPic.printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(com.developerstar.pos.print.GPrinterCommand.reset);
        printBytes.add(com.developerstar.pos.print.GPrinterCommand.print);
        printBytes.add(bytes);
        Log.e("BtService", "image bytes size is :" + bytes.length);
        printBytes.add(com.developerstar.pos.print.GPrinterCommand.print);
        printBytes.add(GPrinterCommand.cut);
        PrintQueue.getQueue(getApplicationContext()).add(printBytes);
    }

    private boolean checkBluetoothOn(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean checkBluetoothDevice(String btAddress){
        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices) {
            String address=bt.getAddress();
            if(btAddress.equals(address)){
                connectBluetoothDevice(bt);
                return true;
            }
        }
        return false;
    }

    public void connectBluetoothDevice(BluetoothDevice bt){
        if (null == deviceAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = bt;
        if (null == bluetoothDevice) {
            return;
        }
        try {
            BtUtil.cancelDiscovery(bluetoothAdapter);
            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
            if (null != deviceAdapter) {
                deviceAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
            }

            //if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            createBondMethod.invoke(bluetoothDevice);
            //}
            PrintQueue.getQueue(getApplicationContext()).disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), "");
            Toast.makeText(context,"Bluetooth Tethering Fail,Try Again",Toast.LENGTH_LONG).show();
        }
    }

    /** end bluetooth code **/

    private void setLayoutResource(){
        layoutPrint80=findViewById(R.id.layoutPrint80);
        layoutPrint58=findViewById(R.id.layoutPrint58);
        btnPrint=findViewById(R.id.btnPrint);
    }
}
