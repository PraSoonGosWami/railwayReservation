package com.invaderx.railway.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.invaderx.railway.R;
import com.invaderx.railway.pojoClasses.Passengers;

import java.util.List;

public class PassengersAdapter extends ArrayAdapter<Passengers> {
    public PassengersAdapter(@NonNull Context context, int resource, @NonNull List<Passengers> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view==null)
            view= ((Activity) getContext()).getLayoutInflater().inflate(R.layout.passeneger_model, parent, false);

        TextView passSex,passName,passAge;
        passAge=view.findViewById(R.id.passAge);
        passName=view.findViewById(R.id.passName);
        passSex=view.findViewById(R.id.passSex);

        Passengers passengers = getItem(position);
        passAge.setText(String.valueOf(passengers.getpAge()));
        passName.setText(passengers.getpName());
        passSex.setText(passengers.getpSex());

        return view;
    }
}
