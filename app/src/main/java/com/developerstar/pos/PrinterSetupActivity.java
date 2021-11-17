package com.developerstar.pos;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.andprn.jpos.command.ESCPOSConst;
import com.andprn.jpos.printer.ESCPOSPrinter;
import com.andprn.request.android.RequestHandler;
import com.developerstar.pos.bt.BtUtil;
import com.developerstar.pos.print.PrintQueue;
import com.developerstar.pos.print.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import adapter.BtDeviceListAdapter;
import adapter.SpPaperWidthAdapter;
import adapter.SpPrinterInterfaceAdapter;
import common.AlertView;
import common.BtService;
import common.PrinterWiFiPort;
import common.SystemInfo;
import database.DatabaseAccess;
import model.PaperWidthModel;
import model.PrinterInterfaceModel;
import model.PrinterModel;

public class PrinterSetupActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    Button btnPrintTest,btnSave,btnSearch,btnEdit;
    TextInputLayout input_printer_name,input_printer_ip,input_bluetooth_printer;
    EditText etPrinterName,etPrinterIP,etBluetoothPrinter;
    Spinner spInterface,spPaperWidth;
    LinearLayout layoutEthernet,layoutBluetooth;
    SpPrinterInterfaceAdapter spPrinterInterfaceAdapter;
    SpPaperWidthAdapter spPaperWidthAdapter;
    List<PrinterInterfaceModel> lstPrinterInterface;
    List<PaperWidthModel> lstPaperWidth;
    public static BluetoothAdapter BA;
    SystemInfo systemInfo=new SystemInfo();
    public static int WidthSize;
    private BtDeviceListAdapter deviceAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private static final String TAG = "WiFiConnectMenu";
    private PrinterWiFiPort wifiPort= PrinterWiFiPort.getInstance();
    private Thread hThread;
    String ioException;
    private ESCPOSPrinter posPtr;
    int editPrinterId;
    PrinterModel printerModel=new PrinterModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setup);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_printer);

        Intent i = getIntent();
        editPrinterId = i.getIntExtra("PrinterID",0);

        BA = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter=new BtDeviceListAdapter(getApplicationContext(), null);
        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        getPrinterInterface();
        getPaperWidth();

        if(editPrinterId != 0) fillEditData();

        spInterface.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    layoutEthernet.setVisibility(View.VISIBLE);
                    layoutBluetooth.setVisibility(View.GONE);
                }else if(position==1){
                    layoutEthernet.setVisibility(View.GONE);
                    layoutBluetooth.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBluetoothOn()) {
                    Intent i = new Intent(PrinterSetupActivity.this, BondBtActivity.class);
                    startActivity(i);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrinter();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPrinter();
            }
        });
        btnPrintTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int interfacePosition = spInterface.getSelectedItemPosition();
                String interfaceName = lstPrinterInterface.get(interfacePosition).getInterfaceName();
                int widthPosition = spPaperWidth.getSelectedItemPosition();
                String width = lstPaperWidth.get(widthPosition).getWidthName();
                if (width.equals(systemInfo.PaperWidth58)) WidthSize = 58;
                else if (width.equals(systemInfo.PaperWidth80)) WidthSize = 80;
                if (interfaceName.equals(systemInfo.InterfaceEthernet)) {
                    if(checkWifiOn()) testPrintEthernet();
                } else if (interfaceName.equals(systemInfo.InterfaceBluetooth)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (checkBluetoothOn()) {
                                if (checkBluetoothDevice()) {
                                    Intent intent = new Intent(getApplicationContext(), BtService.class);
                                    intent.setAction(PrintUtil.ACTION_PRINT_BITMAP);
                                    startService(intent);
                                }
                            }
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                clearControls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillEditData(){
        printerModel = db.getPrinterByID(editPrinterId);
        etPrinterName.setText(printerModel.getPrinterName());
        int editPrinterInterfaceId = printerModel.getPrinterInterfaceId();
        String editPaperWidth=printerModel.getPaperWidth();
        for(int j=0;j<lstPrinterInterface.size();j++){
            if(lstPrinterInterface.get(j).getInterfaceId() ==editPrinterInterfaceId){
                spInterface.setSelection(j);
                break;
            }
        }
        if(editPrinterInterfaceId==1){
            layoutEthernet.setVisibility(View.VISIBLE);
            layoutBluetooth.setVisibility(View.GONE);
            etPrinterIP.setText(printerModel.getNetPrinterIp());
        }else if(editPrinterInterfaceId==2){
            layoutEthernet.setVisibility(View.GONE);
            layoutBluetooth.setVisibility(View.VISIBLE);
            etBluetoothPrinter.setText(printerModel.getBtPrinterAddress());
        }
        for(int j=0;j<lstPaperWidth.size();j++){
            if(lstPaperWidth.get(j).getWidthName().equals(editPaperWidth)){
                spPaperWidth.setSelection(j);
                break;
            }
        }
        spInterface.setEnabled(false);
        btnSave.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
    }

    private void savePrinter() {
        String printerName, netPrinterIp, btPrinterAddress, widthName, printerInterfaceName;
        int printerInterfaceId;
        if (validate()) {
            printerName = etPrinterName.getText().toString();
            int widthPosition = spPaperWidth.getSelectedItemPosition();
            widthName = lstPaperWidth.get(widthPosition).getWidthName();

            int interfacePosition = spInterface.getSelectedItemPosition();
            printerInterfaceId = lstPrinterInterface.get(interfacePosition).getInterfaceId();
            printerInterfaceName = lstPrinterInterface.get(interfacePosition).getInterfaceName();
            if (printerInterfaceName.equals(systemInfo.InterfaceEthernet)) {
                if (checkPrinterIP()) {
                    netPrinterIp = etPrinterIP.getText().toString();
                    if (!db.isExistNetPrinter()) {
                        if (db.insertPrinter(printerName, printerInterfaceId, printerInterfaceName, netPrinterIp, "", widthName)) {
                            Toast.makeText(context, "Network Printer Saved!", Toast.LENGTH_LONG).show();
                            clearControls();
                        }
                    } else {
                        Toast.makeText(context, "Allow only one network printer!", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (printerInterfaceName.equals(systemInfo.InterfaceBluetooth)) {
                if (checkBluetoothPrinter()) {
                    btPrinterAddress = etBluetoothPrinter.getText().toString();
                    if (db.insertPrinter(printerName, printerInterfaceId, printerInterfaceName, "", btPrinterAddress, widthName)) {
                        Toast.makeText(context, "Bluetooth Printer Saved!", Toast.LENGTH_LONG).show();
                        clearControls();
                    }
                }
            }
        }
    }

    private void editPrinter() {
        String printerName, netPrinterIp, btPrinterAddress, widthName, printerInterfaceName;
        int printerInterfaceId;
        if (validate()) {
            printerName = etPrinterName.getText().toString();
            int widthPosition = spPaperWidth.getSelectedItemPosition();
            widthName = lstPaperWidth.get(widthPosition).getWidthName();

            int interfacePosition = spInterface.getSelectedItemPosition();
            printerInterfaceId = lstPrinterInterface.get(interfacePosition).getInterfaceId();
            printerInterfaceName = lstPrinterInterface.get(interfacePosition).getInterfaceName();
            if (printerInterfaceName.equals(systemInfo.InterfaceEthernet)) {
                if (checkPrinterIP()) {
                    netPrinterIp = etPrinterIP.getText().toString();
                    if (db.updatePrinter(editPrinterId, printerName, printerInterfaceId, printerInterfaceName, netPrinterIp, "", widthName)) {
                        Toast.makeText(context, "Network Printer Edited!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } else if (printerInterfaceName.equals(systemInfo.InterfaceBluetooth)) {
                if (checkBluetoothPrinter()) {
                    btPrinterAddress = etBluetoothPrinter.getText().toString();
                    if (db.updatePrinter(editPrinterId, printerName, printerInterfaceId, printerInterfaceName, "", btPrinterAddress, widthName)) {
                        Toast.makeText(context, "Bluetooth Printer Edited!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }

    private boolean validate(){
        if(etPrinterName.getText().toString().trim().length()==0){
            input_printer_name.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private boolean checkPrinterIP(){
        if(etPrinterIP.getText().toString().trim().length()==0){
            input_printer_ip.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private boolean checkBluetoothPrinter(){
        if(etBluetoothPrinter.getText().toString().trim().length()==0){
            Toast.makeText(context,"Cannot find Bluetooth Printer!",Toast.LENGTH_LONG).show();
            etBluetoothPrinter.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkWifiOn(){
        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifi.isWifiEnabled()) {
            Toast.makeText(context,"Please, Turn On Wifi!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void testPrintEthernet(){
        try {
            wifiDisConn();
        }catch (IOException e) {
            Log.e(TAG,e.getMessage(),e);
        }catch (InterruptedException e) {
            Log.e(TAG,e.getMessage(),e);
        }
        if(etPrinterIP.getText().toString().length()!=0) {
            connectPrinter(etPrinterIP.getText().toString());
        }else{
            input_printer_ip.setError(getResources().getString(R.string.please_enter_value));
        }
    }

    private void connectPrinter(String printerIPAddress){
        wifiPort = PrinterWiFiPort.getInstance();
        try{
            wifiConn(printerIPAddress);
        }
        catch (IOException e)
        {
            Log.e(TAG,e.getMessage(),e);
        }
    }

    private void wifiConn(String ipAddr) throws IOException
    {
        new connTask().execute(ipAddr);
    }

    private void wifiDisConn() throws IOException, InterruptedException {
        wifiPort.disconnect();
        if(hThread!=null)hThread.interrupt();
    }

    class connTask extends AsyncTask<String, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(PrinterSetupActivity.this);

        @Override
        protected void onPreExecute()
        {
            dialog.setTitle("Printer Connect");
            dialog.setMessage("Connecting");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            Integer retVal = null;
            try
            {
                wifiPort.connect(params[0]);
                retVal = new Integer(0);
            }
            catch (IOException e)
            {
                Log.e(TAG,e.getMessage(),e);
                retVal = new Integer(-1);
                ioException=e.getMessage();
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            if(result.intValue() == 0)
            {
                RequestHandler rh = new RequestHandler();
                hThread = new Thread(rh);
                hThread.start();
                if(dialog.isShowing()) {
                    dialog.dismiss();
                    try {
                        print(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                AlertView.showAlert("Failed", "Check Devices!"+ioException, context);
            }
            super.onPostExecute(result);
        }
    }

    public void print(Context context) throws IOException {
        File filePath;
        posPtr = new ESCPOSPrinter();
        File directory = new File(Environment.getExternalStorageDirectory().getPath(), "/OnePOSDB/TestPrint");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if(WidthSize==58) {
            filePath = new File(directory, "print58.png");
        }else{
            filePath = new File(directory, "print80.png");
        }
        String receiptPath=filePath.toString();
        posPtr.printBitmap(receiptPath, ESCPOSConst.ALIGNMENT_CENTER);
        posPtr.lineFeed(4);
        posPtr.cutPaper();
    }

    private boolean checkBluetoothOn(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(context,"Turned On",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean checkBluetoothDevice(){
        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices) {
            String address=bt.getAddress();
            String editAddress=etBluetoothPrinter.getText().toString();
            if(editAddress.equals(address)){
                connectDevice(bt);
                return true;
            }
        }
        return false;
    }

    public void connectDevice(BluetoothDevice bt){
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

    private void getPrinterInterface(){
        lstPrinterInterface=new ArrayList<>();

        PrinterInterfaceModel data=new PrinterInterfaceModel();
        data.setInterfaceId(1);
        data.setInterfaceName(systemInfo.InterfaceEthernet);
        lstPrinterInterface.add(data);

        data=new PrinterInterfaceModel();
        data.setInterfaceId(2);
        data.setInterfaceName(systemInfo.InterfaceBluetooth);
        lstPrinterInterface.add(data);

        spPrinterInterfaceAdapter = new SpPrinterInterfaceAdapter(this,lstPrinterInterface);
        spInterface.setAdapter(spPrinterInterfaceAdapter);
    }

    private void getPaperWidth(){
        lstPaperWidth=new ArrayList<>();

        PaperWidthModel data=new PaperWidthModel();
        data.setWidthId(1);
        data.setWidthName(systemInfo.PaperWidth58);
        lstPaperWidth.add(data);

        data=new PaperWidthModel();
        data.setWidthId(2);
        data.setWidthName(systemInfo.PaperWidth80);
        lstPaperWidth.add(data);

        spPaperWidthAdapter = new SpPaperWidthAdapter(this,lstPaperWidth);
        spPaperWidth.setAdapter(spPaperWidthAdapter);
    }

    private void clearControls(){
        etPrinterName.setText("");
        etPrinterIP.setText("");
        etBluetoothPrinter.setText("");
        editPrinterId=0;
        spInterface.setEnabled(true);
        btnSave.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        input_printer_name.setErrorEnabled(false);
        input_printer_ip.setErrorEnabled(false);
    }

    private void setLayoutResource(){
        btnPrintTest=findViewById(R.id.btnPrintTest);
        btnSave=findViewById(R.id.btnSave);
        btnEdit=findViewById(R.id.btnEdit);
        btnSearch=findViewById(R.id.btnSearch);
        etPrinterName=findViewById(R.id.etPrinterName);
        etPrinterIP=findViewById(R.id.etPrinterIP);
        etBluetoothPrinter=findViewById(R.id.etBluetoothPrinter);
        spInterface=findViewById(R.id.spInterface);
        spPaperWidth=findViewById(R.id.spPaperWidth);
        layoutBluetooth=findViewById(R.id.layoutBluetooth);
        layoutEthernet=findViewById(R.id.layoutEthernet);
        input_printer_name=findViewById(R.id.input_printer_name);
        input_printer_ip=findViewById(R.id.input_printer_ip);
        input_bluetooth_printer=findViewById(R.id.input_bluetooth_printer);
    }
}
