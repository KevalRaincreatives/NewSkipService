package com.skipservices;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmergencyUpdate extends AppCompatActivity {
    TextView btn_cancle,btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_update);

        btn_update=(TextView)findViewById(R.id.btn_update);
        btn_cancle=(TextView)findViewById(R.id.btn_cancle);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                myWebLink
                        .setData(Uri
                                .parse("https://play.google.com/store/apps/details?id=com.rcjalisapp"));
                startActivity(myWebLink);
            }
        });
    }
}
