package com.example.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Contact4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact4);
        Button buttonCall = findViewById(R.id.buttonCall4);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:0776531458";
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(callIntent);
            }
        });
    }
}
