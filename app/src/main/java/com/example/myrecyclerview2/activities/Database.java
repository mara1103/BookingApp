package com.example.myrecyclerview2.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.classes.Destination;
import com.example.myrecyclerview2.classes.Reservation;

public class Database extends SQLiteOpenHelper {

    private Context context;
    public  static final String DATABASE_NAME = "Booking.db";
    public  static final int DATABASE_VERSION = 1;
    public static final String TABLE_DESTINATIONS = "Destinations";
    public static final String COLUMN_ID = "Destination_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PHOTO = "Photo";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_PRICE = "Price";

    // Constants for User table
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "User_id";
    public static final String COLUMN_FIRST_NAME = "First_Name";
    public static final String COLUMN_LAST_NAME = "Last_Name";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_ROLE = "Role";
    public static final String COLUMN_PHONE = "Phone";
    //Reservation table
    public static final String TABLE_RESERVATIONS = "Reservations";
    public static final String COLUMN_RESERVATION_ID = "Reservation_id";

    public static final String COLUMN_DESTINATION_ID_FK = "Destination_id"; // Foreign key referencing Destinations table
    public static final String COLUMN_USER_ID_FK = "User_id"; // Foreign key referencing Users table
    //public static final String COLUMN_PAYMENT_METHOD = "Payment_method";
    public static final String COLUMN_NUMBER_OF_ROOMS = "Number_of_rooms";
    public static final String COLUMN_TOTAL_PRICE = "Total_Price";
    //public static final String COLUMN_RESERVATION_DATE = "Reservation_date";
    public static final String COLUMN_CHECK_IN = "Check_in";
    public static final String COLUMN_CHECK_OUT = "Check_out";



    private static Database instance;

    //SQLiteDatabase sqLiteDatabase;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 =
                "CREATE TABLE " + TABLE_DESTINATIONS +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PHOTO + " TEXT," +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_PRICE + " INTEGER);";
        db.execSQL(query1);

        // Create Users table
        String queryUsers =
                "CREATE TABLE " + TABLE_USERS +
                        " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_FIRST_NAME + " TEXT, " +
                        COLUMN_LAST_NAME + " TEXT, " +
                        COLUMN_USERNAME + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_ROLE + " TEXT, " +
                        COLUMN_PHONE + " INTEGER);";
        db.execSQL(queryUsers);

        // Create Reservations table
        String queryReservations =
                "CREATE TABLE " + TABLE_RESERVATIONS +
                        " (" + COLUMN_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        COLUMN_NAME + " TEXT, " +
//                        COLUMN_EMAIL + " TEXT, " +
//                        COLUMN_PHONE + " TEXT, " +
                        COLUMN_DESTINATION_ID_FK + " INTEGER, " +
                        COLUMN_USER_ID_FK + " INTEGER, " +
                        //COLUMN_PAYMENT_METHOD + " TEXT, " +
                        COLUMN_NUMBER_OF_ROOMS + " INTEGER, " +
                        COLUMN_TOTAL_PRICE + " REAL, " +
                        //COLUMN_RESERVATION_DATE + " DATETIME, " +
                        COLUMN_CHECK_IN + " STRING, " +
                        COLUMN_CHECK_OUT + " STRING);";
        db.execSQL(queryReservations);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);

    }

    boolean addDestination(Destination destinationClass){
        //store all our data in content values to pass to our table
        ContentValues cv = new ContentValues();
        cv.put(Database.COLUMN_NAME, destinationClass.getName());
        cv.put(Database.COLUMN_PHOTO, destinationClass.getPhoto());
        cv.put(Database.COLUMN_DESCRIPTION, destinationClass.getDescription());
        cv.put(Database.COLUMN_PRICE, destinationClass.getPrice());

        // create sqlite database object refering to the openhelper class, to get writable database so we can write to our table
        SQLiteDatabase db = this.getWritableDatabase();

        // insert the data inside our database table
        long result = db.insert(TABLE_DESTINATIONS, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            sendNotification(context);
        }

        return false;
    }


    // Funcția pentru a emite notificarea
    private void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Verificăm dacă suntem pe o versiune de Android care suportă canale de notificare
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ID", "Name", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Construiește notificarea
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ID")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Destinație adăugată")
                .setContentText("O nouă destinație a fost adăugată în baza de date!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Trimite notificarea
        if (notificationManager != null) {
            notificationManager.notify(/*id_notificare*/ 1, builder.build());
        }
    }


    public void addReservation(Reservation reservation) {
        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_NAME, reservation.getName());
//        cv.put(COLUMN_EMAIL, reservation.getEmail());
//        cv.put(COLUMN_PHONE, reservation.getPhone());
        cv.put(COLUMN_NUMBER_OF_ROOMS, reservation.getNumber_of_rooms());
        cv.put(COLUMN_TOTAL_PRICE, reservation.getTotal_price());
        cv.put(COLUMN_DESTINATION_ID_FK, reservation.getDestinationId());
        cv.put(COLUMN_USER_ID_FK, reservation.getUserId());
//        cv.put(COLUMN_CHECK_IN, reservation.getCheck_in()); // tb sa caut asta cu nano
//        cv.put(COLUMN_CHECK_OUT, reservation.getCheck_out());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String checkInString = reservation.getCheck_in().format(formatter);
//        String checkOutString = reservation.getCheck_out().format(formatter);   IMPOSIBIL ASA  CEVA, NO DATETIME TYPE IN SQLITE
        cv.put(COLUMN_CHECK_IN, reservation.getCheck_in());
        cv.put(COLUMN_CHECK_OUT, reservation.getCheck_out());



        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(TABLE_RESERVATIONS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add reservation", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Reservation added successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    //cursor - process each row of a result set one at a time
    public Cursor readAllData(){ //read all destinations
        String query = "SELECT * FROM " + TABLE_DESTINATIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        //cursor will contain all the data from our table
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    // Method to read all users from the Users table
    public Cursor readAllUsers(){
        String query = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateDestination (int destinationId, String newName, String newPhotoLink, String newDescription, int newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        values.put(COLUMN_PHOTO, newPhotoLink);
        values.put(COLUMN_DESCRIPTION, newDescription);
        values.put(COLUMN_PRICE, newPrice);

        String whereClause = COLUMN_ID +" = ?";
        String[] whereArgs = {String.valueOf(destinationId)};

        db.update(TABLE_DESTINATIONS, values, whereClause, whereArgs);
    }

    public boolean deleteDestination(int destinationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(destinationId)};

        db.beginTransaction();
        try {
            int rowsDeleted = db.delete(TABLE_DESTINATIONS, whereClause, whereArgs);

            db.setTransactionSuccessful();

            // Returnează true dacă s-a șters cu succes cel puțin o înregistrare
            return rowsDeleted > 0;
        } catch (Exception e) {
            Log.e("Database", "Error deleting destination from database: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
        }
    }

}