package com.example.myrecyclerview2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecyclerview2.R;
import com.example.myrecyclerview2.activities.Database;
import com.example.myrecyclerview2.activities.DestinationUpdateActivity;
import com.example.myrecyclerview2.classes.Destination;
import com.example.myrecyclerview2.interfaces.OnItemsClickedListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Un adaptor pentru un RecyclerView pentru a gestiona afișarea listei de țări.
//ADAPTOR care va popula de fapt datele în RecyclerView
public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> implements Filterable{

    private final ArrayList<Destination> destinationArrayList;
    private final ArrayList<Destination> destinationArrayListFull;
    private static OnItemsClickedListener onItemsClickedListener;

    //constructorul adaptorului
    public DestinationAdapter(ArrayList<Destination> destinationArrayList, OnItemsClickedListener onItemsClickedListener) {
        this.destinationArrayList = destinationArrayList;
        destinationArrayListFull = new ArrayList<>(destinationArrayList);
        this.onItemsClickedListener = onItemsClickedListener;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_item, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationArrayList.get(position);
        holder.bind(destination);

        // vizibilitatea butoanelor în funcție de rol
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
        String userRole = preferences.getString("userRole", "");

        if ("user".equals(userRole)) {
            // Ascunde butoanele care nu sunt pentru utilizator
            holder.btnDetails.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        } else if ("admin".equals(userRole)) {
            // Arată butoanele specifice pentru admin
            holder.btnDetails.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destination destination = destinationArrayList.get(position);
                // Deschiderea DestinationUpdateActivity și trimiterea obiectului Destination
                Intent intent = new Intent(view.getContext(), DestinationUpdateActivity.class);
                intent.putExtra("destination", destination);
                view.getContext().startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmare ștergere");
                builder.setMessage("Sunteți sigur că doriți să ștergeți această destinație?");

                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDestination(position, view.getContext());
                    }
                });

                builder.setNegativeButton("Anulează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void deleteDestination(int position, Context context) {
        Destination deletedDestination = destinationArrayList.get(position);

        boolean isDeleted = Database.getInstance(context).deleteDestination(deletedDestination.getId());

        if (isDeleted) {
            destinationArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, destinationArrayList.size());
        } else {
            Toast.makeText(context, "Failed to delete destination", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return destinationArrayList.size();
    }

    //filtrare lista
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Destination> filteredList=new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(destinationArrayListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Destination item : destinationArrayListFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            destinationArrayList.clear();
            destinationArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        // Variabile pentru elementele vizuale ale unui element din listă
        private final ImageView imageView;
        private final TextView textViewName;
        private final TextView textViewPrice;
        private final Button btnDetails, btnEdit, btnDelete;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Destination destination) {
            textViewName.setText(destination.getName());
            textViewPrice.setText(String.valueOf(destination.getPrice()));
            String imageUrl = destination.getPhoto();
            Picasso.get().load(imageUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    // Imaginea s-a încărcat
                }

                @Override
                public void onError(Exception e) {
                    Log.e("eroare la încărcare!", "Eroare");
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onItemsClickedListener != null) {
//                        onItemsClickedListener.onItemClick(destination);
//                    }
//                }
//            });

            btnDetails.setOnClickListener(v -> {
                if (onItemsClickedListener != null) {
                    onItemsClickedListener.onItemClick(destination);
                }
            });
        }
    }

}
