package com.example.myrecyclerview2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.classes.Destination;

public class DestinationAddActivity extends AppCompatActivity {

    EditText name_input, photo_input, description_input, price_input;
    Button add_button, view_destinations_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination);//definește aspectul vizual al acestei activități.

        name_input = findViewById(R.id.textName);
        photo_input = findViewById(R.id.photo);
        description_input = findViewById(R.id.textDescription);
        price_input = findViewById(R.id.price);
        add_button = findViewById(R.id.addDestinationButton);
        view_destinations_button = findViewById(R.id.viewButton);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create database object
                Database myDB = new Database(DestinationAddActivity.this);
                //pass addDestination method
                //get text, convert to string, pass to addDestination which will insert to sqlite db
//                myDB.addDestination(name_input.getText().toString().trim(),
//                        photo_input.getText().toString().trim(),
//                        description_input.getText().toString().trim(),
//                        Integer.valueOf(price_input.getText().toString().trim())
//                        );
                Destination destination = new Destination(
                        name_input.getText().toString().trim(),
                        photo_input.getText().toString().trim(),
                        description_input.getText().toString().trim(),
                        Integer.valueOf(price_input.getText().toString().trim())
                );
                myDB.addDestination(destination);

            }
        });

        view_destinations_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationAddActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

    }
}
