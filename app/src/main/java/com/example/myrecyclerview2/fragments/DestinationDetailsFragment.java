package com.example.myrecyclerview2.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.activities.DestinationUpdateActivity;
import com.example.myrecyclerview2.activities.ReservationActivity;
import com.example.myrecyclerview2.classes.Destination;
import com.squareup.picasso.Picasso;

public class DestinationDetailsFragment extends Fragment {

    private ImageView imageViewDestination;
    private TextView textViewDestinationName, textViewDescription, textViewPrice;
    private Button buttonReserve, buttonAddReview, buttonEdit;

    public DestinationDetailsFragment() {
        // Required empty public constructor
    }

    public static DestinationDetailsFragment newInstance(String param1, String param2) {
        DestinationDetailsFragment fragment = new DestinationDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_destination_details, container, false);

        // Initializează elementele vizuale din fragment_destination_details.xml
        imageViewDestination = view.findViewById(R.id.imageViewDestination);
        textViewDestinationName = view.findViewById(R.id.textViewDestinationName);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewPrice = view.findViewById(R.id.textViewPrice);
        buttonReserve = view.findViewById(R.id.buttonReserve);
        buttonEdit = view.findViewById(R.id.buttonEdit);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userRole = preferences.getString("userRole", "");

        //despachetam bundle-ul pentru a obține destinatia selectată
        Bundle args = getArguments();
        if (args != null) {
            Destination destination = (Destination) (args.getSerializable("destination"));
            //String userRole = args.getString("userRole", "");

            SharedPreferences preferencess = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferencess.edit();
            editor.putInt("destinationId", destination.getId());
            editor.putInt("destinationPrice", destination.getPrice());
            // pastram id ul pentru a-l accesa si salva in REZERVARE
            editor.apply();

            // Afișează informațiile destinatiei în elementele vizuale
            if (destination != null) {
                Picasso.get().load(destination.getPhoto()).into(imageViewDestination);

                textViewDestinationName.setText(destination.getName());
                textViewDescription.setText(destination.getDescription());
                textViewPrice.setText(String.valueOf(destination.getPrice()));

                //ascundem butonul de booking daca user-ul este admin
                if ("admin".equals(userRole)) {
                    buttonReserve.setVisibility(View.GONE);
                }

                buttonReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Deschide activitatea de rezervare
                        Intent intent = new Intent(getActivity(), ReservationActivity.class);
                        startActivity(intent);

                    }
                });

                if ("user".equals(userRole)) {
                    buttonEdit.setVisibility(View.GONE);
                }

                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DestinationUpdateActivity.class);
                        // save the destination in the intent to pass to the update page text boxes
                        intent.putExtra("destination", destination);
                        startActivity(intent);



                    }
                });

            }
        }

        return view;
    }
}
