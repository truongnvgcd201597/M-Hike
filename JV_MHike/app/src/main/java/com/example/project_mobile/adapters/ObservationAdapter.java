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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.observation.ObservationActivity;
import com.example.project_mobile.activities.observation.ObservationDetailActivity;
import com.example.project_mobile.activities.observation.ObservationUpdateActivity;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private Activity activity;
    private List<Observation> observations;
    private List<Observation> observationsFind;
    private Animation translate_anim;
    private Hike hike;

    public ObservationAdapter(Activity activity, Context context, List<Observation> observations, Hike hike){
        this.activity = activity;
        this.context = context;
        this.observations = observations;
        this.hike = hike;
        this.observationsFind = observations;
    }
    @NonNull
    @Override
    public ObservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_obs_row, parent, false);
        return new ObservationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.MyViewHolder holder, int position) {
        Observation observation = observations.get(position);
        holder.name_obs_txt.setText(String.valueOf(observation.getObsName()));
        holder.date_obs_txt.setText(String.valueOf(observation.getObsTime()));

        holder.img_edit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ObservationUpdateActivity.class);
            intent.putExtra("hike_name", String.valueOf(hike.getHikeName()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocationHike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDateHike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParkingAvailable()));
            intent.putExtra("length_hike", String.valueOf(hike.getHikeLength()));
            intent.putExtra("level_hike", String.valueOf(hike.getHikeLevel()));
            intent.putExtra("des_hike", String.valueOf(hike.getHikeDescription()));

            intent.putExtra("hike_id", String.valueOf(observation.getHikeId()));
            intent.putExtra("obs_name", String.valueOf(observation.getObsName()));
            intent.putExtra("obs_time", String.valueOf(observation.getObsTime()));
            intent.putExtra("obs_comment", String.valueOf(observation.getObsComment()));
            intent.putExtra("obs_id", String.valueOf(observation.getObsId()));
            activity.startActivityForResult(intent,1);
        });
        holder.img_remove.setOnClickListener(v -> {
            holder.alertDialog.setTitle("Delete " + String.valueOf(observation.getObsName()))
                    .setMessage("Are you sure you want to delete item "+ String.valueOf(observation.getObsName())+ " ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, ObservationActivity.class);
                            intent.putExtra("hike_id", String.valueOf(observation.getHikeId()));
                            intent.putExtra("hike_name", String.valueOf(hike.getHikeName()));
                            intent.putExtra("hike_location", String.valueOf(hike.getLocationHike()));
                            intent.putExtra("hike_date", String.valueOf(hike.getDateHike()));
                            intent.putExtra("parking_available", String.valueOf(hike.getParkingAvailable()));
                            intent.putExtra("length_hike", String.valueOf(hike.getHikeLength()));
                            intent.putExtra("level_hike", String.valueOf(hike.getHikeLevel()));
                            intent.putExtra("des_hike", String.valueOf(hike.getHikeDescription()));
                            deleteObservation((int) observation.getHikeId());
                            activity.startActivityForResult(intent,1);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        });

        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ObservationDetailActivity.class);
            intent.putExtra("hike_name", String.valueOf(hike.getHikeName()));
            intent.putExtra("hike_location", String.valueOf(hike.getLocationHike()));
            intent.putExtra("hike_date", String.valueOf(hike.getDateHike()));
            intent.putExtra("parking_available", String.valueOf(hike.getParkingAvailable()));
            intent.putExtra("length_hike", String.valueOf(hike.getHikeLength()));
            intent.putExtra("level_hike", String.valueOf(hike.getHikeLevel()));
            intent.putExtra("des_hike", String.valueOf(hike.getHikeDescription()));

            intent.putExtra("hike_id", String.valueOf(observation.getHikeId()));
            intent.putExtra("obs_name", String.valueOf(observation.getObsName()));
            intent.putExtra("obs_time", String.valueOf(observation.getObsTime()));
            intent.putExtra("obs_comment", String.valueOf(observation.getObsComment()));
            intent.putExtra("obs_id", String.valueOf(observation.getObsId()));
            activity.startActivityForResult(intent,1);
        });
    }
    private void deleteObservation(int obs_id){
        MyDatabaseHelper myBD = new MyDatabaseHelper(context);
        myBD.deleteOneRowObs(String.valueOf(obs_id));
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    observations = observationsFind;
                }else {
                    List<Observation> list = new ArrayList<>();
                    for(Observation observation :observationsFind){
                        if(observation.getObsName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(observation);
                        }
                    }
                    observations = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = observations;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                observations = (List<Observation>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name_obs_txt, date_obs_txt;
        private ImageView img_edit, img_remove;
        private LinearLayout mainLayout;
        private AlertDialog.Builder alertDialog;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_obs_txt = itemView.findViewById(R.id.name_obs_row);
            date_obs_txt = itemView.findViewById(R.id.date_obs_row);
            img_edit = itemView.findViewById(R.id.edit_obs_icon);
            img_remove = itemView.findViewById(R.id.remove_obs_icon);
            alertDialog = new AlertDialog.Builder(context);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
