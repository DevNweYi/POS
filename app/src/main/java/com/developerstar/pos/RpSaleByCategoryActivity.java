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

import adapter.SaleByCategoryAdapter;
import au.com.bytecode.opencsv.CSVWriter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.MasterSaleModel;

public class RpSaleByCategoryActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    RecyclerView rvSaleByCategory;
    TextView tvDatePeriod,tvTotalQuantity,tvTotalAmt;
    String fromDate,toDate;
    List<MasterSaleModel> lstMasterSale=new ArrayList<>();
    SaleByCategoryAdapter saleByCategoryAdapter;
    SystemInfo systemInfo=new SystemInfo();
    View oldView;
    int totalQuantity,totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_sale_by_category);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_sale_by_category);

        Intent i=getIntent();
        fromDate=i.getStringExtra("FromDate");
        toDate=i.getStringExtra("ToDate");

        tvDatePeriod.setText("Period from " + fromDate + " to " + toDate);

        lstMasterSale=db.getSaleByCategory(fromDate,toDate);
        saleByCategoryAdapter=new SaleByCategoryAdapter(lstMasterSale,context);
        rvSaleByCategory.setAdapter(saleByCategoryAdapter);
        rvSaleByCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvSaleByCategory.addItemDecoration(new DividerItemDecoration(rvSaleByCategory.getContext(), DividerItemDecoration.VERTICAL));

        for(int j=0;j<lstMasterSale.size();j++){
            totalQuantity+=lstMasterSale.get(j).getTotalQuantity();
            totalAmount+=lstMasterSale.get(j).getTotalAmount();
        }

        tvTotalQuantity.setText(String.valueOf(totalQuantity));
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(totalAmount))+" MMK");

        saleByCategoryAdapter.setOnItemClickListener(onItemClickListener);
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
            File file = new File(exportDir, "SaleByCategory.csv");

            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String header[] = {getResources().getString(R.string.category),getResources().getString(R.string.total_quantity),getResources().getString(R.string.total_amount)};
                csvWrite.writeNext(header);
                for (int i = 0; i < lstMasterSale.size(); i++) {
                    String data[] = {lstMasterSale.get(i).getCategoryName(),String.valueOf(lstMasterSale.get(i).getTotalQuantity()),String.valueOf(lstMasterSale.get(i).getTotalAmount())};
                    csvWrite.writeNext(data);
                }
                String footer[] = {"Total",tvTotalQuantity.getText().toString(),tvTotalAmt.getText().toString()};
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
        rvSaleByCategory=findViewById(R.id.rvSaleByCategory);
        tvDatePeriod=findViewById(R.id.tvDatePeriod);
        tvTotalQuantity=findViewById(R.id.tvTotalQuantity);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
    }
}
