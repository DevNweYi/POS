package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.ProductModel;

public class RpTopSaleCategoryActivity extends AppCompatActivity {

    TextView tvDatePeriod,tvTopCat1,tvTopCat1Percent,tvTopCat2,tvTopCat2Percent,tvTopCat3,tvTopCat3Percent,tvTopCat4,tvTopCat4Percent,tvTopCat5,tvTopCat5Percent;
    PieChart pieChart;
    RelativeLayout layoutTopCat1,layoutTopCat2,layoutTopCat3,layoutTopCat4,layoutTopCat5;
    DatabaseAccess db;
    Context context=this;
    String fromDate,toDate;
    List<ProductModel> lstProduct=new ArrayList<>();
    ArrayList<PieEntry> pieEntries=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rp_top_sale_category);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.rp_top_sale_category);

        Intent i=getIntent();
        fromDate=i.getStringExtra("FromDate");
        toDate=i.getStringExtra("ToDate");

        tvDatePeriod.setText("Period from " + fromDate + " to " + toDate);

        lstProduct=db.getTopSaleCategory(fromDate,toDate,5);

        for (int j =0; j < lstProduct.size();j++){
            String categoryName = lstProduct.get(j).getCategoryName();
            int totalSaleQtyByCategory = lstProduct.get(j).getTotalSaleQtyByCategory();
            pieEntries.add(new PieEntry(totalSaleQtyByCategory,categoryName));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueTextSize(16);
        pieDataSet.setValueTextColor(R.color.colorBlackLight);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDrawMarkers(false);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(13);
        legend.setDrawInside(false);
        legend.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        legend.setWordWrapEnabled(true);

        pieChart.animateXY(2000,2000);
        pieChart.invalidate();

        /*pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.setDrawMarkers(true);
                int x=0;
                try {
                    x = pieChart.getData().getDataSetForEntry(e).getEntryIndex((PieEntry) e);
                }catch (Exception ex){
                    ex.getMessage();
                }
                String region = lstProduct.get(x).getCategoryName();
                String sales = String.valueOf(lstProduct.get(x).getTotalSaleQtyByCategory());
                AlertDialog.Builder builder = new AlertDialog.Builder(RpTopSaleCategoryActivity.this);
                builder.setCancelable(true);
                View view = LayoutInflater.from(RpTopSaleCategoryActivity.this).inflate(R.layout.dialog_top_category_data,null);
                TextView regionTxtView = view.findViewById(R.id.tvName);
                TextView salesTxtView = view.findViewById(R.id.tvValue);
                regionTxtView.setText(region);
                salesTxtView.setText(sales);
                builder.setView(view);
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });*/
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

    private void setLayoutResource(){
        pieChart = findViewById(R.id.pieChart);
        tvDatePeriod = findViewById(R.id.tvDatePeriod);
        tvTopCat1 = findViewById(R.id.tvTopCat1);
        tvTopCat1Percent = findViewById(R.id.tvTopCat1Percent);
        tvTopCat2 = findViewById(R.id.tvTopCat2);
        tvTopCat2Percent = findViewById(R.id.tvTopCat2Percent);
        tvTopCat3 = findViewById(R.id.tvTopCat3);
        tvTopCat3Percent = findViewById(R.id.tvTopCat3Percent);
        tvTopCat4 = findViewById(R.id.tvTopCat4);
        tvTopCat4Percent = findViewById(R.id.tvTopCat4Percent);
        tvTopCat5 = findViewById(R.id.tvTopCat5);
        tvTopCat5Percent = findViewById(R.id.tvTopCat5Percent);
        layoutTopCat1 = findViewById(R.id.layoutTopCat1);
        layoutTopCat2 = findViewById(R.id.layoutTopCat2);
        layoutTopCat3 = findViewById(R.id.layoutTopCat3);
        layoutTopCat4 = findViewById(R.id.layoutTopCat4);
        layoutTopCat5 = findViewById(R.id.layoutTopCat5);
    }
}
