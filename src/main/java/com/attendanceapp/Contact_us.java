package com.attendanceapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class Contact_us extends Activity implements View.OnClickListener{
    RelativeLayout callus,visitus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        callus=(RelativeLayout) findViewById(R.id.callus);
        visitus=(RelativeLayout) findViewById(R.id.visitus);
        callus.setOnClickListener(this);
        visitus.setOnClickListener(this);
    }

    public void gotoBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.callus:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1-844-668-4391"));
                startActivity(intent);
                break;
            case R.id.visitus:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kzoomobile.com/notify/"));
                startActivity(browserIntent);
                break;
        }
    }
}
