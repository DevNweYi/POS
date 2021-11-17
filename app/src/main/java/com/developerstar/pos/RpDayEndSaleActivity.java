package com.developerstar.pos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import common.SystemInfo;
import database.DatabaseAccess;

public class RpDayEndSaleActivity extends AppCompatActivity {

    TextView tvToday,tvYesterday,tvDate,tvTotalSale,tvTotalAmt,tvDiscountAmt,tvCreditAmt,tvNetAmt,tvTotalQuantity;
    ImageButton btnDate;
    DatabaseAccess db;
    Context context=this;
    private final int DATE_PICKER_DIALOG=1;
    SystemInfo systemInfo=new SystemInfo();
    String currentSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_day_end_sale);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_day_end_sale);

        tvDate.setText(systemInfo.getTodayDate());
        getData(systemInfo.getTodayDate());

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDate.setText(systemInfo.getTodayDate());
                getData(systemInfo.getTodayDate());

                tvToday.setTextColor(getResources().getColor(R.color.colorYellow));
                tvToday.setBackground(getResources().getDrawable(R.drawable.bottom_border));
                tvYesterday.setTextColor(getResources().getColor(R.color.colorGray));
                tvYesterday.setBackgroundResource(0);
            }
        });
        tvYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDate.setText(systemInfo.getYesterdayDate());
                getData(systemInfo.getYesterdayDate());

                tvToday.setTextColor(getResources().getColor(R.color.colorGray));
                tvToday.setBackgroundResource(0);
                tvYesterday.setTextColor(getResources().getColor(R.color.colorYellow));
                tvYesterday.setBackground(getResources().getDrawable(R.drawable.bottom_border));
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_DIALOG);
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

    private void getData(String date){
        tvTotalSale.setText(String.valueOf(db.getTotalSaleCountByDate(date)));
        tvTotalQuantity.setText(String.valueOf(db.getTotalQuantityByDate(date)));
        tvTotalAmt.setText(String.valueOf(systemInfo.df.format(db.getSaleTotalAmountByDate(date)))+" MMK");
        tvNetAmt.setText(String.valueOf(systemInfo.df.format(db.getSaleNetAmountByDate(date)))+" MMK");
        tvDiscountAmt.setText(String.valueOf(systemInfo.df.format(db.getSaleDiscountByDate(date)))+" MMK");
        tvCreditAmt.setText(String.valueOf(systemInfo.df.format(db.getSaleTotalDebtByDate(date))));
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
        DatePickerDialog datePicker=new DatePickerDialog(RpDayEndSaleActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cCalendar.set(Calendar.YEAR,year);
                cCalendar.set(Calendar.MONTH, monthOfYear);
                cCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat=new SimpleDateFormat(systemInfo.DATE_FORMAT);
                currentSelectedDate=dateFormat.format(cCalendar.getTime());
                tvDate.setText(currentSelectedDate);
                getData(currentSelectedDate);

                tvToday.setTextColor(getResources().getColor(R.color.colorGray));
                tvToday.setBackgroundResource(0);
                tvYesterday.setTextColor(getResources().getColor(R.color.colorGray));
                tvYesterday.setBackgroundResource(0);
            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void setLayoutResource(){
        tvToday=findViewById(R.id.tvToday);
        tvYesterday=findViewById(R.id.tvYesterday);
        tvDate=findViewById(R.id.tvDate);
        tvTotalSale=findViewById(R.id.tvTotalSale);
        tvTotalQuantity=findViewById(R.id.tvTotalQuantity);
        tvTotalAmt=findViewById(R.id.tvTotalAmt);
        tvDiscountAmt=findViewById(R.id.tvDiscountAmt);
        tvCreditAmt=findViewById(R.id.tvCreditAmt);
        tvNetAmt=findViewById(R.id.tvNetAmt);
        btnDate=findViewById(R.id.btnDate);
    }
}
