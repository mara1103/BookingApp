package com.example.myrecyclerview2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.fragments.DestinationListFragment;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);//definește aspectul vizual al acestei activități.
        if(savedInstanceState==null){
            loadInitialFragment();
        }
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