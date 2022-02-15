package com.developerstar.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import adapter.ViewPagerAdapter;
import common.ServiceURL;
import common.SystemInfo;
import database.DatabaseAccess;

public class InfoSlideActivity extends AppCompatActivity {

    Context context=this;
    DatabaseAccess db;
    Button btnNext,btnFreeTrail;
    ViewPager vpSlideImage;
    LinearLayout layoutDot,layoutInfoSlide;
    int[] images={R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4};
    ViewPagerAdapter viewPagerAdapter;
    TextView[] dot;
    SystemInfo systemInfo=new SystemInfo();
    String mobileNumber,macAddress;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_slide);
        db=new DatabaseAccess(context);
        getSupportActionBar().hide();
        setLayoutResource();

        Intent i=getIntent();
        mobileNumber=i.getStringExtra("MobileNumber");

        viewPagerAdapter=new ViewPagerAdapter(context,images);
        vpSlideImage.setAdapter(viewPagerAdapter);
        //vpSlideImage.setPageMargin(20);
        addDot(0);

        vpSlideImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* if(position==0)layoutInfoSlide.setBackgroundColor(getResources().getColor(R.color.colorInfoS1));
                else if(position==1)layoutInfoSlide.setBackgroundColor(getResources().getColor(R.color.colorInfoS2));
                else if(position==2)layoutInfoSlide.setBackgroundColor(getResources().getColor(R.color.colorInfoS3));
                else if(position==3)layoutInfoSlide.setBackgroundColor(getResources().getColor(R.color.colorInfoS4));*/
                addDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InfoSlideActivity.this, ShopSettingActivity.class);
                i.putExtra("IsNewUser",true);
                startActivity(i);
                finish();
            }
        });
        btnFreeTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                macAddress= systemInfo.getMacAddress();
                UploadEndUserSetting uploadEndUserSetting=new UploadEndUserSetting();
                uploadEndUserSetting.execute("");
            }
        });
    }

    public class UploadEndUserSetting extends AsyncTask<String, String, String> {

        Boolean isSuccess=false;
        String message;

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url = new URL (ServiceURL.ENDUSERSETTING);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"MacAddress\":"+"\""+macAddress+"\",\"MobileNumber\":"+"\""+mobileNumber+"\"}";
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }
                isSuccess=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(!isSuccess) Toast.makeText(context,r,Toast.LENGTH_LONG).show();
            else{
                if(db.insertTrial(1,systemInfo.TrialAllowDay,systemInfo.getTodayDate())){
                    Intent i = new Intent(InfoSlideActivity.this, ShopSettingActivity.class);
                    i.putExtra("IsNewUser",true);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    public void addDot(int page_position){
        dot=new TextView[images.length];
        layoutDot.removeAllViews();

        for(int i=0;i<dot.length;i++){
            dot[i]=new TextView(context);
            dot[i].setText(Html.fromHtml("&#9673;"));
            dot[i].setTextSize(35);
            dot[i].setTextColor(getResources().getColor(R.color.colorGrayLight));
            layoutDot.addView(dot[i]);
        }

        dot[page_position].setTextColor(getResources().getColor(R.color.colorYellow));
    }

    private void setLayoutResource(){
        btnNext=findViewById(R.id.btnNext);
        btnFreeTrail=findViewById(R.id.btnFreeTrail);
        vpSlideImage=findViewById(R.id.vpSlideImage);
        layoutDot=findViewById(R.id.layoutDot);
        layoutInfoSlide=findViewById(R.id.layoutInfoSlide);

        progressDialog =new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        if(systemInfo.EndUserType==systemInfo.DirectCustomer) btnFreeTrail.setVisibility(View.GONE);
        else if(systemInfo.EndUserType==systemInfo.InDirectCustomer){
            //btnFreeTrail.setText("Start " + systemInfo.TrialAllowDay + "-days Free Trial");
            btnNext.setVisibility(View.GONE);
        }
    }
}
