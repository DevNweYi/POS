package com.developerstar.pos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import common.SystemInfo;
import database.DatabaseAccess;
import model.ShopSettingModel;

public class ShopSettingActivity extends AppCompatActivity {

    ImageView imgLogo;
    TextInputLayout input_name,input_mobile,input_other_mobile,input_address,input_currency;
    EditText etName,etMobile,etOtherMobile,etAddress,etCurrency,etDescription;
    Button btnSave,btnBrowse;
    TextView tvTitle;
    DatabaseAccess db;
    Context context=this;
    private int SHOP_LOGO=1;
    Bitmap bitmap;
    byte[] byteImage;
    boolean isNewUser;
    SystemInfo systemInfo=new SystemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_setting);
        db=new DatabaseAccess(context);
        setLayoutResource();
        setTitle(R.string.sub_shop_setting);

        Intent i = getIntent();
        isNewUser = i.getBooleanExtra("IsNewUser",false);

        if(isNewUser){
            getSupportActionBar().hide();
        }
        else{
            ActionBar actionBar=getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            btnSave.setText(getResources().getString(R.string.save));
            tvTitle.setVisibility(View.GONE);
        }

        fillData();

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Shop Logo"), SHOP_LOGO);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateControls()){
                    if(bitmap != null) {
                        bitmap =getResizedBitmap(bitmap,300);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteImage = stream.toByteArray();
                        bitmap=null;
                    }
                    String shopName=etName.getText().toString();
                    String mobile=etMobile.getText().toString();
                    String otherMobile=etOtherMobile.getText().toString();
                    String address=etAddress.getText().toString();
                    String currencyKeyword=etCurrency.getText().toString();
                    String description=etDescription.getText().toString();
                    db.insertUpdateShopSetting(byteImage,shopName,mobile,address,currencyKeyword,description,otherMobile);
                    clearControls();
                    Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                    if(isNewUser){
                        db.insertSalePurVoucherNumber(systemInfo.StartSaleVoucherNumber,systemInfo.StartPurchaseVoucherNumber);
                        saveTestPrintImageToStorage();
                        Intent i = new Intent(context, SignUpActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SHOP_LOGO) {
                Uri selectedImageUri = data.getData();
                try {
                    getBitmapFromUri(selectedImageUri);
                } catch (IOException e) {
                }
            }
        }
    }

    private void fillData(){
        ShopSettingModel shopSettingModel = db.getShopSettingTableData();
        byteImage=shopSettingModel.getLogo();
        if(byteImage != null) bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        imgLogo.setImageBitmap(bitmap);
        etName.setText(shopSettingModel.getShopName());
        etDescription.setText(shopSettingModel.getDescription());
        etMobile.setText(shopSettingModel.getMobile());
        etAddress.setText(shopSettingModel.getAddress());
        etCurrency.setText(shopSettingModel.getCurrencySymbol());
        etOtherMobile.setText(shopSettingModel.getOtherMobile());
    }

    private void clearControls(){
        input_name.setErrorEnabled(false);
        input_mobile.setErrorEnabled(false);
        input_address.setErrorEnabled(false);
        input_currency.setErrorEnabled(false);
    }

    private void getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        imgLogo.setImageBitmap(bitmap);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void saveTestPrintImageToStorage() {
        BufferedInputStream bis58 = null;
        BufferedInputStream bis80 = null;
        try {
            bis58 = new BufferedInputStream(getAssets().open("print58.png"));
            bis80 = new BufferedInputStream(getAssets().open("print80.png"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Bitmap bitmap58 = BitmapFactory.decodeStream(bis58);
        Bitmap bitmap80 = BitmapFactory.decodeStream(bis80);
        File directory = new File(Environment.getExternalStorageDirectory().getPath()+context.getResources().getString(R.string.folder_name), "TestPrint");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File logoPath58 = new File(directory, "print58.png");
        File logoPath80 = new File(directory, "print80.png");

        FileOutputStream fos58 = null;
        FileOutputStream fos80 = null;
        try {
            fos58 = new FileOutputStream(logoPath58);
            fos80 = new FileOutputStream(logoPath80);
            bitmap58.compress(Bitmap.CompressFormat.PNG, 100, fos58);
            bitmap80.compress(Bitmap.CompressFormat.PNG, 100, fos80);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos58.close();
                fos80.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateControls(){
        if(etName.getText().toString().length()==0){
            input_name.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }else if(etMobile.getText().toString().length()==0) {
            input_mobile.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }else if(etAddress.getText().toString().length()==0) {
            input_address.setError(getResources().getString(R.string.please_enter_value));
            return false;
        }
        return true;
    }

    private void setLayoutResource(){
        imgLogo=findViewById(R.id.imgLogo);
        input_name=findViewById(R.id.input_name);
        input_mobile=findViewById(R.id.input_mobile);
        input_other_mobile=findViewById(R.id.input_other_mobile);
        input_address=findViewById(R.id.input_address);
        input_currency=findViewById(R.id.input_currency);
        etName=findViewById(R.id.etName);
        etMobile=findViewById(R.id.etMobile);
        etOtherMobile=findViewById(R.id.etOtherMobile);
        etAddress=findViewById(R.id.etAddress);
        etCurrency=findViewById(R.id.etCurrency);
        etDescription=findViewById(R.id.etDescription);
        btnSave=findViewById(R.id.btnSave);
        btnBrowse=findViewById(R.id.btnBrowse);
        tvTitle=findViewById(R.id.tvTitle);
    }
}
