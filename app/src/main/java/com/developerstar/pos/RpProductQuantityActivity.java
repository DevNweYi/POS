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

import adapter.ProductQuantityAdapter;
import au.com.bytecode.opencsv.CSVWriter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.ProductModel;

public class RpProductQuantityActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    RecyclerView rvProductQuantity;
    TextView tvDatePeriod;
    String fromDate,toDate;
    List<ProductModel> lstProduct=new ArrayList<>();
    ProductQuantityAdapter productQuantityAdapter;
    SystemInfo systemInfo=new SystemInfo();
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_product_quantity);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_product_quantity);

        Intent i=getIntent();
        fromDate=i.getStringExtra("FromDate");
        toDate=i.getStringExtra("ToDate");

        tvDatePeriod.setText("Period from " + fromDate + " to " + toDate);

        lstProduct=db.getProductQuantityByFromToDate(fromDate,toDate);
        productQuantityAdapter=new ProductQuantityAdapter(lstProduct,context);
        rvProductQuantity.setAdapter(productQuantityAdapter);
        rvProductQuantity.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvProductQuantity.addItemDecoration(new DividerItemDecoration(rvProductQuantity.getContext(), DividerItemDecoration.VERTICAL));

        productQuantityAdapter.setOnItemClickListener(onItemClickListener);
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
            File file = new File(exportDir, "ProductQuantity.csv");

            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String header[] = {getResources().getString(R.string.code),getResources().getString(R.string.product),getResources().getString(R.string.unit),getResources().getString(R.string.category),getResources().getString(R.string.opening_stock),getResources().getString(R.string.purchase),getResources().getString(R.string.sale),getResources().getString(R.string.damage),getResources().getString(R.string.balance)};
                csvWrite.writeNext(header);
                for (int i = 0; i < lstProduct.size(); i++) {
                    String data[] = {lstProduct.get(i).getProductCode(),lstProduct.get(i).getProductName(),lstProduct.get(i).getStandardUnitKeyword(),lstProduct.get(i).getCategoryName(),String.valueOf(systemInfo.df2d.format(lstProduct.get(i).getdOpeningQuantity())),String.valueOf(systemInfo.df2d.format(lstProduct.get(i).getdPurQuantity())),String.valueOf(systemInfo.df2d.format(lstProduct.get(i).getdSaleQuantity())),String.valueOf(systemInfo.df2d.format(lstProduct.get(i).getdAdjustQuantity())),String.valueOf(systemInfo.df2d.format(lstProduct.get(i).getBalQuantity()))};
                    csvWrite.writeNext(data);
                }
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
        rvProductQuantity=findViewById(R.id.rvProductQuantity);
        tvDatePeriod=findViewById(R.id.tvDatePeriod);
    }
}
