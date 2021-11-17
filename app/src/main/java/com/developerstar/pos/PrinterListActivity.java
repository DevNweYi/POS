package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.PrinterListAdapter;
import database.DatabaseAccess;
import model.PrinterModel;

public class PrinterListActivity extends AppCompatActivity {

    DatabaseAccess db;
    Context context=this;
    FloatingActionButton fab;
    ListView lvPrinter;
    ImageButton btnClose,btnDelete;
    RelativeLayout layoutDelete;
    TextView tvDeletedPrinter;
    List<PrinterModel> lstPrinter=new ArrayList<>();
    View oldView;
    int deletePrinterId;
    PrinterListAdapter printerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_printer);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,PrinterSetupActivity.class);
                startActivity(i);
            }
        });
        lvPrinter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int id = lstPrinter.get(position).getId();
                Intent i=new Intent(context,PrinterSetupActivity.class);
                i.putExtra("PrinterID",id);
                startActivity(i);
            }
        });
        lvPrinter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                deletePrinterId=lstPrinter.get(i).getId();
                tvDeletedPrinter.setText(lstPrinter.get(i).getPrinterName()+ " " + getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePrinterId=0;
                layoutDelete.setVisibility(View.GONE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deletePrinter(deletePrinterId)) {
                    deletePrinterId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_printer, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getPrinter();
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

    private void getPrinter(){
        lstPrinter = db.getPrinter();
        printerListAdapter = new PrinterListAdapter(this, lstPrinter);
        lvPrinter.setAdapter(printerListAdapter);
    }

    private void clearControls(){
        layoutDelete.setVisibility(View.GONE);
        getPrinter();
    }

    private void updateAdapter(){
        lstPrinter = db.getPrinter();
        printerListAdapter.updateResults(lstPrinter);
    }

    private void setLayoutResource(){
        lvPrinter=findViewById(R.id.lvPrinter);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        layoutDelete=findViewById(R.id.layoutDelete);
        tvDeletedPrinter=findViewById(R.id.tvDeletedPrinter);
        fab=findViewById(R.id.fab);
    }
}
