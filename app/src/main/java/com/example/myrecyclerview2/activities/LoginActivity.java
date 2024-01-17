package com.example.myrecyclerview2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.classes.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;//variabile
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//initializare activitate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//interfața de utilizare

        usernameEditText = findViewById(R.id.username);//pentru a prelua widget-urile din interfață
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        //actiunea butonului de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//cand se da click se prelueaza username si parola introduse
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidCredentials(username, password)) {
                    // Verifică dacă sunt valide
                    User loggedInUser = authenticate(username, password);

                    if (loggedInUser != null) {
                        if (loggedInUser.getRole().equals("user")) {
                            Toast.makeText(LoginActivity.this, "Autentificare reușită!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(intent);
                        } else if (loggedInUser.getRole().equals("admin")) {
                            Toast.makeText(LoginActivity.this, "Autentificare reușită ca admin!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }

                        // Pastram rolul in "userRole" pentru a fi folosi mai departe in aplicatie
                        // Storing user role in SharedPreferences during login
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userRole", loggedInUser.getRole());
                        editor.putInt("userId", loggedInUser.getUser_id());  // pastram id ul pentru a-l accesa si salva in REZERVARE
                        editor.apply();

                    } else {
                        Toast.makeText(LoginActivity.this, "Credențiale incorecte!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Completați username-ul și parola!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // veridica daca username si parola sunt completate
    private boolean isValidCredentials(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    // Metoda de autentificare a utilizatorului
    private User authenticate(String username, String password) {

        // Citesti bd si verifica daca credidentialele se potrivesc
        Database database = Database.getInstance(this);
        Cursor cursor = database.readAllUsers(); // metoda de citire a users din tabelul din bd ce returneaza un cursor, pe rand fiecare linie

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String dbUsername = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_USERNAME));   //returneaza din bd username
                String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_ROLE));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_ID));

                if (username.equals(dbUsername) && password.equals(dbPassword)) {   // verifica potrivirea
                    // Returnează utilizatorul dacă se potrivesc
                    return new User("", "", username, password, "", role, user_id, 0);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return null;
    }
}
