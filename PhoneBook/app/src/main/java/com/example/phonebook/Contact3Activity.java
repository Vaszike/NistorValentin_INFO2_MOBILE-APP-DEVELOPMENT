package com.example.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Contact3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact3);
        Button buttonCall = findViewById(R.id.buttonCall3);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:0753402371";
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(callIntent);
            }
        });
    }
}
