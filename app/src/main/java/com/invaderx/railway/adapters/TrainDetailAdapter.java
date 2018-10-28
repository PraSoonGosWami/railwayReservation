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

import static android.support.v4.content.ContextCompat.startActivity;

public class TrainDetailAdapter extends RecyclerView.Adapter<TrainDetailAdapter.TrainDetailAdapterViewHolder> {

    private String[] mWeatherData = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public TrainDetailAdapter() {

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
        String weatherForThisDay = mWeatherData[position];
        traindetailAdapterViewHolder.mTrainName.setText(weatherForThisDay);
        traindetailAdapterViewHolder.mTrainNumber.setText(weatherForThisDay);
        traindetailAdapterViewHolder.mTrainStartTime.setText(weatherForThisDay);
        traindetailAdapterViewHolder.mTrainDestinationTime.setText(weatherForThisDay);
        traindetailAdapterViewHolder.mTrainStart.setText(weatherForThisDay);
        traindetailAdapterViewHolder.mTrainDestiation.setText(weatherForThisDay);

        traindetailAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TrainAddActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }


    public class TrainDetailAdapterViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        public TextView mTrainName;
        public TextView mTrainNumber;
        public TextView mTrainStartTime;
        public TextView mTrainDestinationTime;
        public TextView mTrainStart;
        public TextView mTrainDestiation;


        public TrainDetailAdapterViewHolder(View view) {
            super(view);
            mTrainName = (TextView) view.findViewById(R.id.trainname);
            mTrainNumber = (TextView) view.findViewById(R.id.trainnumber);
            mTrainStartTime = (TextView) view.findViewById(R.id.trainstarttime);
            mTrainDestinationTime = (TextView) view.findViewById(R.id.traindestinationtime);
            mTrainStart = (TextView) view.findViewById(R.id.trainstart);
            mTrainDestiation = (TextView) view.findViewById(R.id.destination);
            context = view.getContext();


        }

    }

}