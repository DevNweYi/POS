package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.CategoryFilterAdapter;
import common.SystemInfo;
import database.DatabaseAccess;
import model.CategoryModel;

public class ReportMenuActivity extends AppCompatActivity {

    TextView tvRpSaleByMonth,tvRpDetailedSale,tvRpDayEndSale,tvRpSaleByDate,tvRpTopSaleCategory,
            tvRpTopSaleProduct,tvRpDetailedPurchase,tvRpStockQuantity,tvRpSaleByCategory,tvRpSaleByProduct;
    Button btnFromDate,btnToDate;
    DatabaseAccess db;
    Context context=this;
    private final int DATE_PICKER_DIALOG=1;
    SystemInfo systemInfo=new SystemInfo();
    boolean isFromDate;
    enum ReportType{DetailedSale,SaleByDate,SaleByCategory,TopSaleCategory,SaleByProduct,TopSaleProduct,DetailedPurchase,ProductQuantity}
    ReportType reportType;
    List<CategoryModel> lstCategory=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_menu);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.main_report);

        tvRpDayEndSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ReportMenuActivity.this,RpDayEndSaleActivity.class);
                startActivity(i);
            }
        });
        tvRpDetailedSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.DetailedSale;
                showDateRangeDialog();
            }
        });
        tvRpSaleByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.SaleByDate;
                showDateRangeDialog();
            }
        });
        tvRpSaleByMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearPickerDialog();
            }
        });
        tvRpSaleByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.SaleByCategory;
                showDateRangeDialog();
            }
        });
        tvRpTopSaleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.TopSaleCategory;
                showDateRangeDialog();
            }
        });
        tvRpSaleByProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategory();
                reportType=ReportType.SaleByProduct;
                showDateRangeDialog();
            }
        });
        tvRpTopSaleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.TopSaleProduct;
                showDateRangeDialog();
            }
        });
        tvRpDetailedPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.DetailedPurchase;
                showDateRangeDialog();
            }
        });
        tvRpStockQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportType=ReportType.ProductQuantity;
                showDateRangeDialog();
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

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_PICKER_DIALOG:
                return showDatePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker(){
        final Calendar cCalendar=Calendar.getInstance();
        DatePickerDialog datePicker=new DatePickerDialog(ReportMenuActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cCalendar.set(Calendar.YEAR,year);
                cCalendar.set(Calendar.MONTH, monthOfYear);
                cCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat=new SimpleDateFormat(systemInfo.DATE_FORMAT);
                if(isFromDate) btnFromDate.setText(dateFormat.format(cCalendar.getTime()));
                else btnToDate.setText(dateFormat.format(cCalendar.getTime()));
            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void getCategory(){
        lstCategory=new ArrayList<>();
        lstCategory = db.getCategory();
    }

    private void showDateRangeDialog(){
        CategoryFilterAdapter categoryFilterAdapter;
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_date_range, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        btnFromDate=v.findViewById(R.id.btnFromDate);
        btnToDate=v.findViewById(R.id.btnToDate);
        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final LinearLayout layoutCategory=v.findViewById(R.id.layoutCategory);
        final RecyclerView rvCategory=v.findViewById(R.id.rvCategory);
        final TextView tvCategory=v.findViewById(R.id.tvCategory);

        btnFromDate.setText(systemInfo.getTodayDate());
        btnToDate.setText(systemInfo.getTodayDate());

        if(reportType==ReportType.SaleByProduct){
            layoutCategory.setVisibility(View.VISIBLE);
            categoryFilterAdapter=new CategoryFilterAdapter(lstCategory,context);
            rvCategory.setAdapter(categoryFilterAdapter);
            rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            rvCategory.addItemDecoration(new DividerItemDecoration(rvCategory.getContext(), DividerItemDecoration.VERTICAL));
        }

        dialog.setCancelable(false);
        final android.app.AlertDialog showDialog=dialog.create();
        showDialog.show();

        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rvCategory.isShown()){
                    tvCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    rvCategory.setVisibility(View.GONE);
                }
                else {
                    tvCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    rvCategory.setVisibility(View.VISIBLE);
                }
            }
        });
        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate=true;
                showDialog(DATE_PICKER_DIALOG);
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isFromDate=false;
                showDialog(DATE_PICKER_DIALOG);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String fromDate=btnFromDate.getText().toString();
                String toDate=btnToDate.getText().toString();
                if(fromDate.length()==0 || fromDate==null){
                    Toast.makeText(context,getResources().getString(R.string.please_select_date_range),Toast.LENGTH_SHORT).show();
                    return;
                }else if(toDate.length()==0 || toDate==null){
                    Toast.makeText(context,getResources().getString(R.string.please_select_date_range),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(reportType==ReportType.DetailedSale) {
                    Intent i = new Intent(context, RpDetailedSaleActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.SaleByDate){
                    Intent i = new Intent(context, RpSaleByDateActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.SaleByCategory){
                    Intent i = new Intent(context, RpSaleByCategoryActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.TopSaleCategory){
                    Intent i = new Intent(context, RpTopSaleCategoryActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.SaleByProduct){
                    String selectedCategoryLst="";
                    for (int i = 0; i < lstCategory.size(); i++){
                        if(lstCategory.get(i).getSelected()) {
                            selectedCategoryLst+=lstCategory.get(i).getCategoryId()+",";
                        }
                    }
                    if(selectedCategoryLst.length()!=0)selectedCategoryLst=selectedCategoryLst.substring(0,selectedCategoryLst.length()-1);
                    Intent i = new Intent(context, RpSaleByProductActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    i.putExtra("SelectedCategoryLst",selectedCategoryLst);
                    startActivity(i);
                }else if(reportType==ReportType.TopSaleProduct){
                    Intent i = new Intent(context, RpTopSaleProductActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.DetailedPurchase){
                    Intent i = new Intent(context, RpDetailedPurchaseActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }else if(reportType==ReportType.ProductQuantity){
                    Intent i = new Intent(context, RpProductQuantityActivity.class);
                    i.putExtra("FromDate", fromDate);
                    i.putExtra("ToDate", toDate);
                    startActivity(i);
                }
                showDialog.dismiss();
            }
        });
    }

    private void showYearPickerDialog(){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_year_picker, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final NumberPicker picker_year=v.findViewById(R.id.picker_year);
        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        picker_year.setMinValue(1900);
        picker_year.setMaxValue(3500);
        picker_year.setValue(year);

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
                Intent i = new Intent(context, RpSaleByMonthActivity.class);
                i.putExtra("Year", picker_year.getValue());
                startActivity(i);
                showDialog.dismiss();
            }
        });
    }

    private void setLayoutResource(){
        tvRpSaleByDate=findViewById(R.id.tvRpSaleByDate);
        tvRpDetailedSale=findViewById(R.id.tvRpDetailedSale);
        tvRpDayEndSale=findViewById(R.id.tvRpDayEndSale);
        tvRpSaleByMonth=findViewById(R.id.tvRpSaleByMonth);
        tvRpSaleByCategory=findViewById(R.id.tvRpSaleByCategory);
        tvRpSaleByProduct=findViewById(R.id.tvRpSaleByProduct);
        tvRpTopSaleCategory=findViewById(R.id.tvRpTopSaleCategory);
        tvRpTopSaleProduct=findViewById(R.id.tvRpTopSaleProduct);
        tvRpDetailedPurchase=findViewById(R.id.tvRpDetailedPurchase);
        tvRpStockQuantity=findViewById(R.id.tvRpStockQuantity);
    }
}
