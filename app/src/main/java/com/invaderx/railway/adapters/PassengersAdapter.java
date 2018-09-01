package com.invaderx.railway.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.invaderx.railway.R;
import com.invaderx.railway.models.Passengers;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;

public class PassengersAdapter extends ArrayAdapter<Passengers> {
    List<Passengers> objects;
    public PassengersAdapter(@NonNull Context context, int resource, @NonNull List<Passengers> objects) {
        super(context, resource, objects);
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view==null)
            view= ((Activity) getContext()).getLayoutInflater().inflate(R.layout.passeneger_model, parent, false);

        TextView passSex,passName,passAge;
        Button deletePassengers;

        Passengers passengers = getItem(position);
        passAge=view.findViewById(R.id.passAge);
        passName=view.findViewById(R.id.passName);
        passSex=view.findViewById(R.id.passSex);
        deletePassengers=view.findViewById(R.id.deletePassengers);
        deletePassengers.setTag(position);

        //deleting the selected passenger with alert dialog box
        deletePassengers.setOnClickListener(view1 -> {

            new LovelyStandardDialog(getContext(), LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(android.R.color.holo_red_dark)
                    .setButtonsColorRes(android.R.color.black)
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .setTitle("Delete Passenger")
                    .setMessage("Are you sure you want to delete it?")
                    .setPositiveButton(android.R.string.ok, v -> {
                        Integer index = (Integer) view1.getTag();
                        objects.remove(index.intValue());
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        });

        passAge.setText(String.valueOf(passengers.getpAge()));
        passName.setText(passengers.getpName());
        passSex.setText(passengers.getpSex());

        return view;
    }
}
