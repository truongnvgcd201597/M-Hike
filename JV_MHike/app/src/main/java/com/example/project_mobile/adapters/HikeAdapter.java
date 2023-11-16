package com.example.project_mobile.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.DetailHikeActivity;
import com.example.project_mobile.activities.MainActivity;
import com.example.project_mobile.activities.UpdateHikeActivity;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private Activity activity;
    private List<Hike> hikeList;
    private List<Hike> hikeListFiltered;

    public HikeAdapter(Activity activity, Context context, List<Hike> hikes) {
        this.activity = activity;
        this.context = context;
        this.hikeList = hikes;
        this.hikeListFiltered = hikes;
    }

    @NonNull
    @Override
    public HikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_hike_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.MyViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        holder.nameHikeTxt.setText(String.valueOf(hike.getHikeName()));
        holder.dateHikeTxt.setText(String.valueOf(hike.getDateHike()));

        holder.imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateHikeActivity.class);
            intent.putExtra("hike_id", String.valueOf(hike.getHikeId()));
            intent.putExtra("hike_name", String.valueOf(hike.getHikeName()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocationHike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDateHike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParkingAvailable()));
            intent.putExtra("length_hike", String.valueOf(hike.getHikeLength()));
            intent.putExtra("level_hike", String.valueOf(hike.getHikeLevel()));
            intent.putExtra("des_hike", String.valueOf(hike.getHikeDescription()));
            activity.startActivityForResult(intent, 1);
        });
        holder.imgRemove.setOnClickListener(v -> {
            holder.alertDialog.setTitle("Delete " + String.valueOf(hike.getHikeName()))
                    .setMessage("Are you sure you want to delete item " + String.valueOf(hike.getHikeName()))
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteHike((int) hike.getHikeId());
                            activity.startActivityForResult(new Intent(context, MainActivity.class), 1);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        });

        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailHikeActivity.class);
            intent.putExtra("hike_id", String.valueOf(hike.getHikeId()));
            intent.putExtra("hike_name", String.valueOf(hike.getHikeName()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocationHike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDateHike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParkingAvailable()));
            intent.putExtra("length_hike", String.valueOf(hike.getHikeLength()));
            intent.putExtra("level_hike", String.valueOf(hike.getHikeLevel()));
            intent.putExtra("des_hike", String.valueOf(hike.getHikeDescription()));
            activity.startActivityForResult(intent, 1);
        });
    }

    private void deleteHike(int hikeId) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        myDB.deleteOneRow(String.valueOf(hikeId));
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    hikeList = hikeListFiltered;
                } else {
                    List<Hike> filteredList = new ArrayList<>();
                    for (Hike hike : hikeListFiltered) {
                        if (hike.getHikeName().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredList.add(hike);
                        }
                    }
                    hikeList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hikeList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hikeList = (List<Hike>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameHikeTxt, dateHikeTxt;
        private ImageView imgEdit, imgRemove;
        private LinearLayout mainLayout;
        private AlertDialog.Builder alertDialog;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameHikeTxt = itemView.findViewById(R.id.name_hike_txt);
            dateHikeTxt = itemView.findViewById(R.id.date_hike_text);
            imgEdit = itemView.findViewById(R.id.image_edit);
            imgRemove = itemView.findViewById(R.id.image_remove);
            alertDialog = new AlertDialog.Builder(context);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}