package com.invaderx.railway.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.invaderx.railway.R;
import com.invaderx.railway.activity.TrainAddActivity;
import com.invaderx.railway.models.Trains;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class TrainDetailAdapter extends RecyclerView.Adapter<TrainDetailAdapter.TrainDetailAdapterViewHolder> {

    private List<Trains> trainsAdapterList;
    private Context context;
    private String addTrain = "Hello";

    public TrainDetailAdapter(List<Trains> trainsAdapterList, Context context) {
        this.trainsAdapterList = trainsAdapterList;
        this.context = context;
    }

    @Override
    public TrainDetailAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.train_menu;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrainDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainDetailAdapterViewHolder traindetailAdapterViewHolder, int position) {
        final Trains list = trainsAdapterList.get(position);
        traindetailAdapterViewHolder.mTrainName.setText(list.gettName());
        traindetailAdapterViewHolder.mTrainNumber.setText(list.gettNumber());
        traindetailAdapterViewHolder.mTrainStartTime.setText(list.getTime());
        traindetailAdapterViewHolder.mTrainStart.setText(list.getStations().get(0));
        traindetailAdapterViewHolder.mTrainDestiation.setText(list.getStations().get(list.getStations().size()-1));
        traindetailAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TrainAddActivity.class);
                intent.putExtra("add", addTrain);
                intent.putExtra("TrainNum",list.gettNumber());
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return trainsAdapterList.size();
    }


    public class TrainDetailAdapterViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        public TextView mTrainName;
        public TextView mTrainNumber;
        public TextView mTrainStartTime;
        public TextView mTrainStart;
        public TextView mTrainDestiation;


        public TrainDetailAdapterViewHolder(View view) {
            super(view);
            mTrainName = view.findViewById(R.id.trainname);
            mTrainNumber = view.findViewById(R.id.trainnumber);
            mTrainStartTime =  view.findViewById(R.id.trainstarttime);
            mTrainStart =  view.findViewById(R.id.trainstart);
            mTrainDestiation =  view.findViewById(R.id.destination);
            context = view.getContext();


        }

    }

}