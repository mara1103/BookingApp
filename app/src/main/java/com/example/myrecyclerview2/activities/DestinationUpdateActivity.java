package com.example.myrecyclerview2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.classes.Destination;
import com.example.myrecyclerview2.fragments.DestinationListFragment;

public class DestinationUpdateActivity extends AppCompatActivity {

    private EditText editTextName;
    private  EditText editTextPhotoLink;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private Button btnUpdate;

    private Destination destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_destination);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextDestinationName);
        editTextPhotoLink = findViewById(R.id.editTextPhoto);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        btnUpdate = findViewById(R.id.buttonSaveChanges);

        // Retrieve destination details from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("destination")) {
            Destination destination = (Destination) intent.getSerializableExtra("destination");

            // Pre-fill the text boxes with existing information
            editTextName.setText(destination.getName());
            editTextDescription.setText(destination.getDescription());
            editTextPhotoLink.setText(destination.getPhoto());
            editTextPrice.setText(String.valueOf(destination.getPrice()));
            // Set other views accordingly
        }else{
            // Handle the case where destination is null
            Toast.makeText(this, "Error: Destination not found", Toast.LENGTH_SHORT).show();
            //finish();  // Finish the activity to prevent further execution
            return;
        }


        // Set click listener for the update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateDestination();
                // Start the main activity
                Intent mainActivityIntent = new Intent(DestinationUpdateActivity.this, AdminActivity.class);
                startActivity(mainActivityIntent);
            }
        });
    }

//    private Destination getDestinationFromIntent(Intent intent) {
//        if (intent != null && intent.hasExtra("destination")) {
//            return (Destination) intent.getSerializableExtra("destination");
//        }
//        return null;
//    }

    private void updateDestination() {
        // Retrieve edited details from EditText fields
        String newName = editTextName.getText().toString();
        String newPhotoLink = editTextPhotoLink.getText().toString();
        String newDescription = editTextDescription.getText().toString();
        //String newPrice = editTextPrice.getText().toString();

        // Ensure that the input is a valid integer
        int newPrice = 0;  // Default value, you can choose another default if needed
        try {
            newPrice = Integer.parseInt(editTextPrice.getText().toString());
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        Destination destination = (Destination) intent.getSerializableExtra("destination");

        // Update destination in the database
        Database.getInstance(this).updateDestination(destination.getId(), newName, newPhotoLink,newDescription, newPrice);

        // Notify the destination list fragment to update the RecyclerView
        DestinationListFragment destinationListFragment = (DestinationListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (destinationListFragment != null) {
            destinationListFragment.updateDestinationList();
        }



        // Finish the activity
        finish();
    }


}
