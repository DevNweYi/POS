package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.DetailedSaleAdapter;
import au.com.bytecode.opencsv.CSVWriter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.MasterSaleModel;

public class RpDetailedSaleActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    RecyclerView rvDetailedSale;
    TextView tvDatePeriod,tvTotalAmt,tvDiscountAmt,tvNetAmt;
    String fromDate,toDate;
    List<MasterSaleModel> lstMasterSale=new ArrayList<>();
    DetailedSaleAdapter detailedSaleAdapter;
    SystemInfo systemInfo=new SystemInfo();
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_detailed_sale);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_detailed_sale);

        Intent i=getIntent();
        fromDate=i.getStringExtra("FromDate");
        toDate=i.getStringExtra("ToDate");

        tvDatePeriod.setText("Period from " + fromDate + " to " + toDate);

        lstMasterSale=db.getMasterSaleByFromToDate(fromDate,toDate);
        detailedSaleAdapter=new DetailedSaleAdapter(context,lstMasterSale);
        rvDetailedSale.setAdapter(detailedSaleAdapter);
        rvDetailedSale.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvDetailedSale.addItemDecoration(new DividerItemDecoration(rvDetailedSale.getContext(), DividerItemDecoration.VERTICAL));

        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(db.getTotalAmtByFromToDate(fromDate,toDate)))+" MMK");
        tvDiscountAmt.setText(String.valueOf(systemInfo.df.format(db.getDiscountAmtByFromToDate(fromDate,toDate)))+" MMK");
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(db.getNetAmtByFromToDate(fromDate,toDate)))+" MMK");

        detailedSaleAdapter.setOnItemClickListener(onItemClickListener);
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(oldView!=null){
                oldView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                view.setBackgroundColor(getResources().getColor(R.color.colorGrayLight));
                oldView=view;
            } else {
                oldView=view;
                view.setBackgroundColor(getResources().getColor(R.color.colorGrayLight));
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_export_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_excel:
                ExportToExcel exportToExcel=new ExportToExcel();
                exportToExcel.execute("");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ExportToExcel extends AsyncTask<String ,String, String> {
        String message="";
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args){
            File exportDir = new File(Environment.getExternalStorageDirectory().getPath());
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, "DetailedSale.csv");

            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String header[] = {getResources().getString(R.string.voucher_no),getResources().getString(R.string.date_time),getResources().getString(R.string.customer),getResources().getString(R.string.user),getResources().getString(R.string.total_amount),getResources().getString(R.string.discount),getResources().getString(R.string.net_amount),getResources().getString(R.string.last_debt_amount),getResources().getString(R.string.paid_amount),getResources().getString(R.string.change_amount),getResources().getString(R.string.debt_amount)};
                csvWrite.writeNext(header);
                for (int i = 0; i < lstMasterSale.size(); i++) {
                    String data[] = {String.valueOf(lstMasterSale.get(i).getVoucherNumber()),lstMasterSale.get(i).getDate()+" "+lstMasterSale.get(i).getTime(),lstMasterSale.get(i).getCustomerName(),lstMasterSale.get(i).getUserName(),String.valueOf(lstMasterSale.get(i).getTotalAmount()),String.valueOf(lstMasterSale.get(i).getTotalDisAmount()),String.valueOf(lstMasterSale.get(i).getNetAmount()),String.valueOf(lstMasterSale.get(i).getLastDebtAmount()),String.valueOf(lstMasterSale.get(i).getPaidAmount()),String.valueOf(lstMasterSale.get(i).getChangeAmount()),String.valueOf(lstMasterSale.get(i).getDebtAmount())};
                    csvWrite.writeNext(data);
                }
                String footer[] = {"Total","","","",tvTotalAmt.getText().toString(),tvDiscountAmt.getText().toString(),tvNetAmt.getText().toString(),"","","",""};
                csvWrite.writeNext(footer);
                csvWrite.close();

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

    private void setLayoutResource(){
        rvDetailedSale=findViewById(R.id.rvDetailedSale);
        tvDatePeriod=findViewById(R.id.tvDatePeriod);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvDiscountAmt=findViewById(R.id.tvDiscountAmt);
        tvNetAmt=findViewById(R.id.tvNetAmt);
    }
}
