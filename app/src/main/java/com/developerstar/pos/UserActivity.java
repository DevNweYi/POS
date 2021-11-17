package com.developerstar.pos;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.UserAdapter;
import database.DatabaseAccess;
import model.UserModel;

public class UserActivity extends AppCompatActivity {

    UserAdapter userAdapter;
    RelativeLayout layoutDelete;
    ListView lvUser;
    EditText etSearch;
    ImageButton btnClose,btnDelete;
    TextView tvUserCount;
    DatabaseAccess db;
    Context context=this;
    List<UserModel> lstUser=new ArrayList<>();
    int save=1,edit=2,type,editUserId;
    String editUserName,editMobileNumber,editPassword;
    View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        db=new DatabaseAccess(context);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setLayoutResource();
        setTitle(R.string.sub_user);

        getUser();

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editUserId =lstUser.get(i).getUserId();
                editUserName =lstUser.get(i).getUserName();
                editMobileNumber =lstUser.get(i).getMobileNumber();
                editPassword =lstUser.get(i).getPassword();
                type=edit;
                showUserDialog(type);
            }
        });
        lvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(oldView != null) oldView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                layoutDelete.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                editUserId=lstUser.get(i).getUserId();
                tvUserCount.setText(lstUser.get(i).getUserName()+ " " + getResources().getString(R.string.remove_something));
                view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                oldView=view;
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUserId=0;
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteUser(editUserId)) {
                    editUserId = 0;
                    layoutDelete.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                    updateAdapter();
                    Toast.makeText(context, R.string.deleted_user, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.no_delete_user, Toast.LENGTH_SHORT).show();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etSearch.getText().toString().length() != 0) getCategoryByFilter(etSearch.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                type=save;
                showUserDialog(type);
            case R.id.action_refresh:
                layoutDelete.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.setText("");
                getUser();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUser(){
        lstUser=new ArrayList<>();
        lstUser = db.getUserTableData();
        userAdapter=new UserAdapter(context,lstUser);
        lvUser.setAdapter(userAdapter);
    }

    private void getCategoryByFilter(String userName){
        lstUser=new ArrayList<>();
        lstUser = db.getUserByFilter(userName);
        userAdapter=new UserAdapter(context,lstUser);
        lvUser.setAdapter(userAdapter);
    }

    private void updateAdapter(){
        lstUser = db.getUserTableData();
        userAdapter.updateResults(lstUser);
    }

    private void setLayoutResource(){
        lvUser=findViewById(R.id.lvUser);
        etSearch=findViewById(R.id.etSearch);
        layoutDelete=findViewById(R.id.layoutDelete);
        btnClose=findViewById(R.id.btnClose);
        btnDelete=findViewById(R.id.btnDelete);
        tvUserCount=findViewById(R.id.tvUserCount);
    }

    private void showUserDialog(final int typeSaveEdit){
        final LayoutInflater reg=LayoutInflater.from(context);
        View v=reg.inflate(R.layout.dialog_add_user, null);
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(context);
        dialog.setView(v);

        final Button btnCancel=v.findViewById(R.id.btnCancel);
        final Button btnOk=v.findViewById(R.id.btnOk);
        final EditText etUserName=v.findViewById(R.id.etUserName);
        final EditText etMobileNumber=v.findViewById(R.id.etMobileNumber);
        final EditText etPassword=v.findViewById(R.id.etPassword);
        final TextInputLayout input_user_name=v.findViewById(R.id.input_user_name);
        final TextInputLayout input_mobile_number=v.findViewById(R.id.input_mobile_number);
        final TextInputLayout input_password=v.findViewById(R.id.input_password);

        if(typeSaveEdit ==edit ){
            etUserName.setText(editUserName);
            etMobileNumber.setText(editMobileNumber);
            etPassword.setText(editPassword);
        }

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
                if(etUserName.getText().length()==0){
                    input_user_name.setError(getResources().getString(R.string.please_enter_value));
                }else if(etPassword.getText().length() == 0){
                    input_password.setError(getResources().getString(R.string.please_enter_value));
                }else{
                    if(typeSaveEdit==save) {
                        if (db.insertUser(etUserName.getText().toString(),etMobileNumber.getText().toString(),etPassword.getText().toString())) {
                            updateAdapter();
                            etUserName.setText("");
                            etMobileNumber.setText("");
                            etPassword.setText("");
                            input_user_name.setErrorEnabled(false);
                            input_password.setErrorEnabled(false);
                            Toast.makeText(context, R.string.saved_user, Toast.LENGTH_SHORT).show();
                        }
                    }else if(typeSaveEdit==edit){
                        if (db.updateUser(editUserId,etUserName.getText().toString(),etMobileNumber.getText().toString(),etPassword.getText().toString())) {
                            updateAdapter();
                            LoginActivity.UserName=etUserName.getText().toString();
                            Toast.makeText(context, R.string.edit_success, Toast.LENGTH_SHORT).show();
                            showDialog.dismiss();
                        }
                    }
                }
            }
        });
    }
}
