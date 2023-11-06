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
    private List<Hike> hikes;
    private List<Hike> hikesFind;
    private Animation translate_anim;

    public HikeAdapter(Activity activity, Context context, List<Hike> hikes){
        this.activity = activity;
        this.context = context;
        this.hikes = hikes;
        this.hikesFind = hikes;
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
        Hike hike = hikes.get(position);
        holder.name_hike_txt.setText(String.valueOf(hike.getHike_name()));
        holder.date_hike_txt.setText(String.valueOf(hike.getDate_hike()));

        holder.img_edit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateHikeActivity.class);
            intent.putExtra("hike_id", String.valueOf(hike.getHike_id()));
            intent.putExtra("hike_name", String.valueOf(hike.getHike_name()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocation_hike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDate_hike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParking_available()));
            intent.putExtra("length_hike", String.valueOf(hike.getHike_length()));
            intent.putExtra("level_hike", String.valueOf(hike.getHike_level()));
            intent.putExtra("des_hike", String.valueOf(hike.getHike_description()));
            activity.startActivityForResult(intent,1);
        });
        holder.img_remove.setOnClickListener(v -> {
            holder.alertDialog.setTitle("Delete " + String.valueOf(hike.getHike_name()))
                    .setMessage("Are you sure you want to delete item "+ String.valueOf(hike.getHike_name()))
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteHike(hike.getHike_id());
                            activity.startActivityForResult(new Intent(context, MainActivity.class),1);
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
            intent.putExtra("hike_id", String.valueOf(hike.getHike_id()));
            intent.putExtra("hike_name", String.valueOf(hike.getHike_name()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocation_hike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDate_hike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParking_available()));
            intent.putExtra("length_hike", String.valueOf(hike.getHike_length()));
            intent.putExtra("level_hike", String.valueOf(hike.getHike_level()));
            intent.putExtra("des_hike", String.valueOf(hike.getHike_description()));
            activity.startActivityForResult(intent,1);
        });
    }
    private void deleteHike(int hike_id){
        MyDatabaseHelper myBD = new MyDatabaseHelper(context);
        myBD.deleteOneRow(String.valueOf(hike_id));
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    hikes = hikesFind;
                }else {
                    List<Hike> list = new ArrayList<>();
                    for(Hike hike:hikesFind){
                        if(hike.getHike_name().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(hike);
                        }
                    }
                    hikes = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hikes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hikes = (List<Hike>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name_hike_txt, date_hike_txt;
        private ImageView img_edit, img_remove;
        private LinearLayout mainLayout;
        private AlertDialog.Builder alertDialog;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_hike_txt = itemView.findViewById(R.id.name_hike_txt);
            date_hike_txt = itemView.findViewById(R.id.date_hike_text);
            img_edit = itemView.findViewById(R.id.image_edit);
            img_remove = itemView.findViewById(R.id.image_remove);
            alertDialog = new AlertDialog.Builder(context);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
