package com.example.myrecyclerview2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.fragments.DestinationListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminActivity extends AppCompatActivity {
    FloatingActionButton add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        if(savedInstanceState==null){
            loadInitialFragment();
        }
        add_button = findViewById(R.id.addButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, DestinationAddActivity.class);
                startActivity(intent);
            }
        });

    }
    //metodă privată care încarcă fragmentul inițial în activitate.
    private void loadInitialFragment() {

        //cream un fragment de tipul nostru DestinationListFragment
        DestinationListFragment destinationListFragment =new DestinationListFragment();

        //construim un fragment transcription->folosit pentru operatii cu fragmente(ex adăugarea, înlocuirea sau eliminarea fragmentelor)
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();

        //operatia de adaugare a fragmentului nou
        transaction.replace(R.id.fragment_container, destinationListFragment);

        //finalizam operatiunea prin commit
        transaction.commit();
    }
}