package com.example.myrecyclerview2.fragments;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.activities.Database;
import com.example.myrecyclerview2.adapters.DestinationAdapter;
import com.example.myrecyclerview2.classes.Destination;
import com.example.myrecyclerview2.interfaces.OnItemsClickedListener;

import java.util.ArrayList;

public class DestinationListFragment extends Fragment {
    private RecyclerView recyclerView;
    private DestinationAdapter destinationAdapter;
    private ArrayList<Destination> destinationArrayList =new ArrayList<Destination>();

    public DestinationListFragment() {
        // Required empty public constructor
    }

    public static DestinationListFragment newInstance(String param1, String param2) {
        DestinationListFragment fragment = new DestinationListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //pas 1 EXTRAG VIEW-UL DIN LAYOUTUL FRAGMENTULUI MEU
        View view=inflater.inflate(R.layout.fragment_destination_list,container,false);

        //pas 2 BINDINGUL PENTRU RECYCLERVIEW
        recyclerView=view.findViewById(R.id.recycleView);

        //pas 3 INITIALIZEZ LISTA DE TARI CU NICTE VALORI
        //destinationArrayList = XMLParser.parseDestinationXml(requireContext(),"destinations.xml");
        Cursor cursor = Database.getInstance(requireContext()).readAllData();

        destinationArrayList = getDestinationFromCursor(cursor);

        //pas 4 CONSTRUIESC ADAPTORUL CU LISTA DE TARI SI EVENIMENTUL DE ON CLICK
        destinationAdapter=new DestinationAdapter(destinationArrayList, new OnItemsClickedListener() {
            @Override
            public void onItemClick(Destination destination) {

                //declarare noul fragment
                DestinationDetailsFragment destinationDetailsFragment=new DestinationDetailsFragment();

                //impachetam destinatia selectata ca si bundle
                Bundle bundle=new Bundle();
                bundle.putSerializable("destination", destination);

                //atasam la fragment acest bundle
                destinationDetailsFragment.setArguments(bundle);

                //incepem tranzactia de fragment
                FragmentTransaction transaction=requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,destinationDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //pas 5 RECYCLER VIEW PRIMESTE ADAPTORUL MEU
        recyclerView.setAdapter(destinationAdapter);

        //pas 6 RECYCLER VIEW O SA II SETAM LAYOUTUL, O SA O LUAM CA ATARE
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }
    private ArrayList<Destination> getDestinationFromCursor(Cursor cursor){
        ArrayList<Destination> destinations = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            do {
                // Log the column names available in the cursor
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    int columnIndex = cursor.getColumnIndex(columnName);
                    String columnValue = cursor.getString(columnIndex);
                    // Log column names and values
                    Log.d("CursorInfo", "Column: " + columnName + ", Value: " + columnValue);
                }

                // Extract data from the cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DESCRIPTION));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_PRICE));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PHOTO));

                // Create a Destination object and add it to the list
                destinations.add(new Destination(id, name, photo, description, price));
            } while (cursor.moveToNext());

            cursor.close();

        }
        return destinations;
    }

    public void updateDestinationList() {
        // Update your destinationArrayList with the latest data from the database
        destinationArrayList = getDestinationFromCursor(Database.getInstance(requireContext()).readAllData());
        destinationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (destinationAdapter != null) {
                    destinationAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}