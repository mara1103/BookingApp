package com.example.myrecyclerview2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.classes.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editNrRooms, editTextCheckInDate, editTextCheckOutDate;

    private TextView editTotalPrice;
    private int destinationPrice, numberOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Inițializare elemente UI
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editNrRooms = findViewById(R.id.editNrRooms);
        editTotalPrice = findViewById(R.id.textViewTotalPrice);

        //  listener pentru câmpurile de editare pentru datele de check-in și check-out
        editTextCheckInDate = findViewById(R.id.editTextCheckInDate);
        editTextCheckOutDate = findViewById(R.id.editTextCheckOutDate);

        editTextCheckInDate.setOnClickListener(v -> showDatePickerDialog(editTextCheckInDate));

        editTextCheckOutDate.setOnClickListener(v -> showDatePickerDialog(editTextCheckOutDate));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int destinationPrice = preferences.getInt("destinationPrice", 0);

        editTotalPrice.setText("Total Price: " + destinationPrice);

        Button buttonConfirm = findViewById(R.id.buttonConfirm);


        // Listener pentru butonul de finalizare a rezervării
        buttonConfirm.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();


            // Ensure that the input is a valid integer
            int nrRooms = 0;  // Default value, you can choose another default if needed
            try {
                nrRooms = Integer.parseInt(editNrRooms.getText().toString());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                Toast.makeText(this, "Please enter a valid number of rooms", Toast.LENGTH_SHORT).show();
                return;
            }

            String checkInDate = editTextCheckInDate.getText().toString().trim();
            String checkOutDate = editTextCheckOutDate.getText().toString().trim();

            // Ensure that the input is a valid integer



            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && nrRooms!=0 && !checkInDate.isEmpty() && !checkOutDate.isEmpty() &&  destinationPrice!=0) {
                performReservation(name, email, phone, nrRooms, checkInDate, checkOutDate, destinationPrice);
            } else {
                Toast.makeText(ReservationActivity.this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
            }

        });


//        Incercare implementare reservation_dialog_details

//        String checkInDate = editTextCheckInDate.getText().toString().trim();
//        String checkOutDate = editTextCheckOutDate.getText().toString().trim();
//        int numberOfDays = 0;
//        try {
//            numberOfDays = calculateNumberOfDays(checkInDate, checkOutDate);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//
//        int nrRooms = Integer.parseInt(editNrRooms.getText().toString());
//
//        int totalPrice = destinationPrice * numberOfDays * nrRooms;
//        // Set the calculated total price to the TextView
//        editTotalPrice.setText("Total Price: " + totalPrice);


    }

    private int calculateNumberOfDays(String checkIn, String checkOut) throws ParseException {
        //Define the DateTimeFormatter for the given format

        Date check_In=new SimpleDateFormat("d/M/yyyy").parse(checkIn);
        Date check_Out=new SimpleDateFormat("d/M/yyyy").parse(checkOut);


        // Parse the check-in and check-out date strings into LocalDate objects
        //LocalDate check_In = LocalDate.parse(checkIn, formatter);
        //LocalDate check_Out = LocalDate.parse(checkOut, formatter);

        // Convert Date objects to Instant
        Instant instantCheckIn = check_In.toInstant();
        Instant instantCheckOut = check_Out.toInstant();

        // Convert Instant to LocalDate
        LocalDate localDateCheckIn = instantCheckIn.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateCheckOut = instantCheckOut.atZone(java.time.ZoneId.systemDefault()).toLocalDate();


        // Calculate the number of days between check-in and check-out dates
        long daysBetween = ChronoUnit.DAYS.between(localDateCheckIn, localDateCheckOut);  // ChronoUnit - units of time : DAYS, MONTHS, YEARS

        // Return the number of days as an integer
        return (int) daysBetween;
    }

    // Metodă pentru afișarea DatePickerDialog
    private void showDatePickerDialog(final EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private void performReservation(String name, String email, String phone, int nrRooms, String checkInDate, String checkOutDate, int total_price) {

        // Define the DateTimeFormatter for the given format
//        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                .parseCaseInsensitive()
//                .appendPattern("d/M/yyyy")
//                .toFormatter();
//
//        // Parse the checkInDate string into a LocalDate object
//        LocalDate checkIn = LocalDate.parse(checkInDate, formatter);
//        LocalDate checkOut = LocalDate.parse(checkOutDate, formatter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int userId = preferences.getInt("userId", 0);

        int destinationId = preferences.getInt("destinationId", 0);

        Reservation reservation = new Reservation(nrRooms,total_price, userId, destinationId, checkInDate, checkOutDate);

        // Apelul metodei din Database pentru a adăuga rezervarea în baza de date
        Database.getInstance(this).addReservation(reservation);

        // Mesaj de confirmare
        String reservationDetails = "Nume: " + name + "\nE-mail: " + email + "\nTelefon: " + phone +
                "\nCheck-in: " + checkInDate + "\nCheck-out: " + checkOutDate;
        Toast.makeText(this, "Rezervare finalizată!\n" + reservationDetails, Toast.LENGTH_LONG).show();


        // redirectionare catre alt ecran
        Intent intentActivity = new Intent(ReservationActivity.this, UserActivity.class);
        startActivity(intentActivity);
        //finish(); // Pentru a închide activitatea curentă
    }

//    private void showReservationDetailsDialog(String destinationName, int numberOfDays, int fullPrice) {
//        // Inflate the custom layout for the dialog
//        View dialogView = getLayoutInflater().inflate(R.layout.reservation_dialog_details, null);
//
//        // Initialize the TextViews and Button from the dialog layout
//        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
//        // Initialize other TextViews as needed
//        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
//
//        // Set the details in the TextViews
//        textViewTitle.setText("Reservation Details");
//        // Set other details based on your logic
//
//        // Create the dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        AlertDialog dialog = builder.create();
//
//        // Set a click listener for the Save button
//        buttonSave.setOnClickListener(v -> {
//            // Add logic to save the reservation details to the database
//            // For example, you can call your method to save the reservation here
//
//            // Dismiss the dialog after saving
//            dialog.dismiss();
//        });
//
//        // Show the dialog
//        dialog.show();
//    }

}
