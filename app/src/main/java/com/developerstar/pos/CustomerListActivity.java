package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomerListAdapter;
import database.DatabaseAccess;
import listener.CustomerListListener;
import model.CustomerModel;

public class CustomerListActivity extends AppCompatActivity implements CustomerListListener {

    DatabaseAccess db;
    Context context=this;
    FloatingActionButton fab;
    EditText etSearch;
    ImageButton btnClose,btnDelete;
    RelativeLayout layoutDelete;
    TextView tvDeletedCustomer;
    ListView lvCustomer;
    List<CustomerModel> lstCustomer=new ArrayList<>();
    View oldView;
    int deleteCustomerId;
    CustomerListAdapter customerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_customer);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,CustomerSetupActivity.class);
                startActivity(i);
            }
        });
        lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int customerId = lstCustomer.get(position).getCustomerId();
                Intent i=new Intent(context,CustomerSetupActivity.class);
                i.putExtra("CustomerID",customerId);
                startActivity(i);
            }
        });
        lvCustomer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                deleteCustomerId=lstCustomer.get(i).getCustomerId();
                tvDeletedCustomer.setText(lstCustomer.get(i).getCustomerName()+ " " +getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCustomerId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteCustomer(deleteCustomerId)) {
                    deleteCustomerId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_customer, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.no_delete_customer, Toast.LENGTH_SHORT).show();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.getText().toString().length() != 0){
                            getCustomerByFilter(etSearch.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getCustomer();
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

    @Override
    public void onMobileClickListener(int position){
        String mobileNum = lstCustomer.get(position).getMobileNumber();
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNum ));
        startActivity(i);
    }

    private void getCustomer(){
        lstCustomer = db.getCustomerTableData();
        customerListAdapter = new CustomerListAdapter(this, lstCustomer);
        lvCustomer.setAdapter(customerListAdapter);
        customerListAdapter.setOnEventListener(this);
    }

    private void getCustomerByFilter(String customerName){
        lstCustomer = db.getCustomerByFilter(customerName);
        customerListAdapter = new CustomerListAdapter(this, lstCustomer);
        lvCustomer.setAdapter(customerListAdapter);
        customerListAdapter.setOnEventListener(this);
    }

    private void updateAdapter(){
        lstCustomer = db.getCustomerTableData();
        customerListAdapter.updateResults(lstCustomer);
    }

    private void clearControls(){
        layoutDelete.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        etSearch.setText("");
        getCustomer();
    }

    private void setLayoutResource(){
        lvCustomer=findViewById(R.id.lvCustomer);
        etSearch=findViewById(R.id.etSearch);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        layoutDelete=findViewById(R.id.layoutDelete);
        tvDeletedCustomer=findViewById(R.id.tvDeletedCustomer);
        fab=findViewById(R.id.fab);
    }
}
