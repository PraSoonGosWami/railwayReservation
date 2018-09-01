package com.invaderx.railway.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.invaderx.railway.activity.BookingActivity;
import com.invaderx.railway.R;
import com.invaderx.railway.activity.TrainSearchActivity;
import com.invaderx.railway.pojoClasses.Trains;

import java.util.List;

public class TrainsAdapter extends RecyclerView.Adapter<TrainsAdapter.ViewHolder> {
    private List<Trains> trainsAdapterList;
    private Context context;
    int mExpandedPosition=-1;
    RecyclerView recyclerView;
    int tClass =0;

    public TrainsAdapter() {
    }

    public TrainsAdapter(List<Trains> trainsAdapterList, Context context, RecyclerView recyclerView) {
        this.trainsAdapterList = trainsAdapterList;
        this.context = context;
        this.recyclerView=recyclerView;
    }

    @NonNull
    @Override
    public TrainsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.train_model,parent,false);
        return new TrainsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainsAdapter.ViewHolder holder, int position) {
        final Trains list = trainsAdapterList.get(position);
        holder.trainNameNumber.setText(list.gettName()+"\t\t("+list.gettNumber()+")");
        String stations=list.getStations();
        holder.srcDest.setText(TrainSearchActivity.source+" -> "+TrainSearchActivity.destination);
        holder.trainTime.setText(list.getTime());
        holder.oneA.setBackgroundColor(Color.GRAY);
        holder.twoA.setBackgroundColor(Color.GRAY);
        holder.threeA.setBackgroundColor(Color.GRAY);
        holder.sleeper.setBackgroundColor(Color.GRAY);
        holder.cc.setBackgroundColor(Color.GRAY);
        holder.book.setEnabled(false);
        holder.book.setBackgroundResource(R.drawable.rounded_button_disabled);

        //class 1A
        if((list.getClass1A()).equals("0") || list.getSeat1A()==0){
            holder.oneA.setEnabled(true);
            holder.oneA.setAlpha(0.4f);
        }
        else {
            holder.oneA.setOnClickListener(v-> {
                holder.seats.setText(""+list.getSeat1A());
                holder.fare.setText("₹"+list.getClass1A());
                holder.oneA.setBackgroundResource(R.color.colorPrimaryDark);
                holder.twoA.setBackgroundColor(Color.GRAY);
                holder.threeA.setBackgroundColor(Color.GRAY);
                holder.sleeper.setBackgroundColor(Color.GRAY);
                holder.cc.setBackgroundColor(Color.GRAY);
                tClass=1;
                holder.book.setEnabled(true);
                holder.book.setBackgroundResource(R.drawable.rounded_button_solid);
                holder.book.setTextColor(Color.WHITE);

            });

        }

        //class 2A
        if((list.getClass2A().equals("0")) || list.getSeat2A()==0){
            holder.twoA.setEnabled(false);
            holder.twoA.setAlpha(0.4f);
        }
        else {
            holder.twoA.setOnClickListener(v -> {
                holder.seats.setText(""+list.getSeat2A());
                holder.fare.setText("₹" + list.getClass2A());
                holder.oneA.setBackgroundColor(Color.GRAY);
                holder.twoA.setBackgroundResource(R.color.colorPrimaryDark);
                holder.threeA.setBackgroundColor(Color.GRAY);
                holder.sleeper.setBackgroundColor(Color.GRAY);
                holder.cc.setBackgroundColor(Color.GRAY);
                tClass=2;
                holder.book.setEnabled(true);
                holder.book.setBackgroundResource(R.drawable.rounded_button_solid);
                holder.book.setTextColor(Color.WHITE);
            });

        }

        //class 3A
        if((list.getClass3A().equals("0")) || list.getSeat3A()==0) {
            holder.threeA.setEnabled(false);
            holder.threeA.setAlpha(0.4f);
        }
        else {
            holder.threeA.setOnClickListener(v -> {
                holder.seats.setText("" + list.getSeat3A());
                holder.fare.setText("₹" + list.getClass3A());
                holder.oneA.setBackgroundColor(Color.GRAY);
                holder.twoA.setBackgroundColor(Color.GRAY);
                holder.threeA.setBackgroundResource(R.color.colorPrimaryDark);
                holder.sleeper.setBackgroundColor(Color.GRAY);
                holder.cc.setBackgroundColor(Color.GRAY);
                tClass=3;
                holder.book.setEnabled(true);
                holder.book.setBackgroundResource(R.drawable.rounded_button_solid);
                holder.book.setTextColor(Color.WHITE);
            });

        }

        //class sleeper
        if((list.getClassSL().equals("0")) || list.getSeatSL()==0){
            holder.sleeper.setEnabled(false);
            holder.sleeper.setAlpha(0.4f);
        }
        else {
            holder.sleeper.setOnClickListener(v ->{
                    holder.seats.setText(""+list.getSeatSL());
            holder.fare.setText("₹"+list.getClassSL());
                holder.oneA.setBackgroundColor(Color.GRAY);
                holder.twoA.setBackgroundColor(Color.GRAY);
                holder.threeA.setBackgroundColor(Color.GRAY);
                holder.sleeper.setBackgroundResource(R.color.colorPrimaryDark);
                holder.cc.setBackgroundColor(Color.GRAY);
                tClass=4;
                holder.book.setEnabled(true);
                holder.book.setBackgroundResource(R.drawable.rounded_button_solid);
                holder.book.setTextColor(Color.WHITE);
            });

        }

        //class CC
        if((list.getClassCC().equals("0")) || list.getSeatCC()==0) {
            holder.cc.setAlpha(0.4f);
            holder.cc.setEnabled(false);
        }
        else {
            holder.cc.setOnClickListener(v -> {
                holder.seats.setText(""+list.getSeatCC());
                holder.fare.setText("₹" + list.getClassCC());
                holder.oneA.setBackgroundColor(Color.GRAY);
                holder.twoA.setBackgroundColor(Color.GRAY);
                holder.threeA.setBackgroundColor(Color.GRAY);
                holder.sleeper.setBackgroundColor(Color.GRAY);
                holder.cc.setBackgroundResource(R.color.colorPrimaryDark);
                tClass=5;
                holder.book.setEnabled(true);
                holder.book.setBackgroundResource(R.drawable.rounded_button_solid);
                holder.book.setTextColor(Color.WHITE);
            });

        }
        final boolean isExpanded = position==mExpandedPosition;
        holder.visView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });
        //book
        holder.book.setOnClickListener(v->{
            String trainNumber = list.gettNumber();
            Intent intent = new Intent(context,BookingActivity.class);
            intent.putExtra("trainNumber",trainNumber);
            intent.putExtra("trainClass",tClass);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return trainsAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Declaration of views
        public TextView trainNameNumber,srcDest,trainTime,seats,fare;
        public Button oneA,twoA,threeA,cc,sleeper,book;
        public LinearLayout visView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainNameNumber=itemView.findViewById(R.id.trainNameNumber);
            srcDest=itemView.findViewById(R.id.srcDest);
            trainTime=itemView.findViewById(R.id.trainTime);
            seats=itemView.findViewById(R.id.seats);
            fare=itemView.findViewById(R.id.fare);
            oneA=itemView.findViewById(R.id.oneA);
            twoA=itemView.findViewById(R.id.twoA);
            threeA=itemView.findViewById(R.id.threeA);
            sleeper=itemView.findViewById(R.id.sleeper);
            cc=itemView.findViewById(R.id.cc);
            book=itemView.findViewById(R.id.book);
            visView=itemView.findViewById(R.id.expandable);



        }

    }
}
