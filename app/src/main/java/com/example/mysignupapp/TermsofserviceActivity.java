package com.example.mysignupapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsofserviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsofservice);

        TextView tv_title = findViewById(R.id.terms_title);
        TextView tv_contents = findViewById(R.id.terms_contents);

        Intent intent = getIntent();
        String agreement = intent.getStringExtra("agreement");

        if(agreement.equals("terms")){
            tv_title.setText(R.string.terms_title);
            tv_contents.setText(R.string.terms_contents);
        }else{
            tv_title.setText(R.string.privacy_title);
            tv_contents.setText(R.string.privacy_contests);
        }

        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
