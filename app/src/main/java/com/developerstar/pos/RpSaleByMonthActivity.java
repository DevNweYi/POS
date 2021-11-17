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

import adapter.SaleByMonthAdapter;
import au.com.bytecode.opencsv.CSVWriter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.MasterSaleModel;

public class RpSaleByMonthActivity extends AppCompatActivity {

    RecyclerView rvSaleByMonth;
    TextView tvYear,tvTotalSale,tvTotalQuantity,tvTotalAmt,tvDiscountAmt,tvNetAmt;
    DatabaseAccess db;
    Context context=this;
    SystemInfo systemInfo=new SystemInfo();
    int year,totalSale,totalQuantity,totalAmount,totalDisAmount,netAmount;
    List<MasterSaleModel> lstMasterSale=new ArrayList<>();
    SaleByMonthAdapter saleByMonthAdapter;
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_sale_by_month);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_sale_by_month);

        Intent i=getIntent();
        year=i.getIntExtra("Year",0);

        tvYear.setText("Monthly Sale for " + year + " Year");

        db.getSaleByMonthly(year);

        lstMasterSale=db.getSaleByMonthly(year);
        saleByMonthAdapter=new SaleByMonthAdapter(lstMasterSale,context);
        rvSaleByMonth.setAdapter(saleByMonthAdapter);
        rvSaleByMonth.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvSaleByMonth.addItemDecoration(new DividerItemDecoration(rvSaleByMonth.getContext(), DividerItemDecoration.VERTICAL));

        for(int j=0;j<lstMasterSale.size();j++){
            totalSale+=lstMasterSale.get(j).getTotalSale();
            totalQuantity+=lstMasterSale.get(j).getTotalQuantity();
            totalAmount+=lstMasterSale.get(j).getTotalAmount();
            totalDisAmount+=lstMasterSale.get(j).getTotalDisAmount();
            netAmount+=lstMasterSale.get(j).getNetAmount();
        }

        tvTotalSale.setText(String.valueOf(totalSale));
        tvTotalQuantity.setText(String.valueOf(totalQuantity));
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount))+" MMK");
        tvDiscountAmt.setText(String.valueOf(systemInfo.df.format(totalDisAmount))+" MMK");
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(netAmount))+" MMK");

        saleByMonthAdapter.setOnItemClickListener(onItemClickListener);
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
            File file = new File(exportDir, "SaleByMonth.csv");

            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String header[] = {getResources().getString(R.string.month),getResources().getString(R.string.total_sale),getResources().getString(R.string.total_quantity),getResources().getString(R.string.total_amount),getResources().getString(R.string.discount),getResources().getString(R.string.net_amount)};
                csvWrite.writeNext(header);
                for (int i = 0; i < lstMasterSale.size(); i++) {
                    String data[] = {lstMasterSale.get(i).getMonthName(),String.valueOf(lstMasterSale.get(i).getTotalSale()),String.valueOf(lstMasterSale.get(i).getTotalQuantity()),String.valueOf(lstMasterSale.get(i).getTotalAmount()),String.valueOf(lstMasterSale.get(i).getTotalDisAmount()),String.valueOf(lstMasterSale.get(i).getNetAmount())};
                    csvWrite.writeNext(data);
                }
                String footer[] = {"Total",tvTotalSale.getText().toString(),tvTotalQuantity.getText().toString(),tvTotalAmt.getText().toString(),tvDiscountAmt.getText().toString(),tvNetAmt.getText().toString()};
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

    private void setLayoutResource() {
        rvSaleByMonth=findViewById(R.id.rvSaleByMonth);
        tvYear=findViewById(R.id.tvYear);
        tvTotalSale=findViewById(R.id.tvTotalSale);
        tvTotalQuantity=findViewById(R.id.tvTotalQuantity);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvDiscountAmt=findViewById(R.id.tvDiscountAmt);
        tvNetAmt=findViewById(R.id.tvNetAmt);
    }
}
