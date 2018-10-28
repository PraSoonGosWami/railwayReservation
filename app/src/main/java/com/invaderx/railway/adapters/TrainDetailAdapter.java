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

    // COMPLETED (23) Create a private string array called mWeatherData
    private String[] mWeatherData = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    // COMPLETED (47) Create the default constructor (we will pass in parameters in a later lesson)
    public TrainDetailAdapter() {

    }

    // COMPLETED (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
    // COMPLETED (17) Extend RecyclerView.ViewHolder

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public TrainDetailAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.train_menu;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrainDetailAdapterViewHolder(view);
    }

    // COMPLETED (24) Override onCreateViewHolder
    // COMPLETED (25) Within onCreateViewHolder, inflate the list item xml into a view
    // COMPLETED (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter

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

    // COMPLETED (27) Override onBindViewHolder
    // COMPLETED (28) Set the text of the TextView to the weather for this list item's position

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }

    // COMPLETED (29) Override getItemCount
    // COMPLETED (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param weatherData The new weather data to be displayed.
     */
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    // COMPLETED (31) Create a setWeatherData method that saves the weatherData to mWeatherData
    // COMPLETED (32) After you save mWeatherData, call notifyDataSetChanged

    /**
     * Cache of the children views for a forecast list item.
     */
    public class TrainDetailAdapterViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////
        // COMPLETED (18) Create a public final TextView variable called mWeatherTextView
        public TextView mTrainName;
        public TextView mTrainNumber;
        public TextView mTrainStartTime;
        public TextView mTrainDestinationTime;
        public TextView mTrainStart;
        public TextView mTrainDestiation;


        // COMPLETED (19) Create a constructor for this class that accepts a View as a parameter
        // COMPLETED (20) Call super(view)
        // COMPLETED (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
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
        // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////
    }

}