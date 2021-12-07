package com.skipservices;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {
    TextView txt_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        txt_message=findViewById(R.id.txt_message);
    }
}
