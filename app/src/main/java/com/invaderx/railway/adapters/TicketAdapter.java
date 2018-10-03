package com.invaderx.railway.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.invaderx.railway.R;
import com.invaderx.railway.models.Ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<Ticket> ticketAdapterList;
    private Context context;
    final private ListItemClickListner listItemClickListner;



    public interface ListItemClickListner{
        void onListItemClick(int clickedItemIndex);
    }

    public TicketAdapter(List<Ticket> ticketAdapterList, Context context,ListItemClickListner listItemClickListner) {
        this.ticketAdapterList = ticketAdapterList;
        this.context = context;
        this.listItemClickListner = listItemClickListner;
    }

    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_model,parent,false);
        return new TicketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.ViewHolder holder, int i) {
        final Ticket list = ticketAdapterList.get(i);
        holder.ticketPNR.setText(list.getPnr());
        holder.ticketSeatNo.setText(list.getSeatNo());
        holder.ticketTrainNameNum.setText(list.getTicketNameNumber());
        holder.ticketSrcDest.setText("From: "+list.getSrc()+"\n"+"To: "+list.getDest());
        holder.ticketDate.setText(list.getDate());
        holder.ticketTime.setText(list.getTime());
        holder.ticketFare.setText("Fare: ₹"+list.getFare());
        holder.ticketClass.setText(list.getTravelClass());

    }

    @Override
    public int getItemCount() {
        return ticketAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ticketPNR,ticketSeatNo,ticketTrainNameNum,ticketSrcDest,ticketDate,ticketTime,ticketFare,ticketClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketPNR=itemView.findViewById(R.id.ticketPNR);
            ticketSeatNo=itemView.findViewById(R.id.ticketSeatNo);
            ticketTrainNameNum=itemView.findViewById(R.id.ticketTrainNameNum);
            ticketSrcDest=itemView.findViewById(R.id.ticketSrcDest);
            ticketTime=itemView.findViewById(R.id.ticketTime);
            ticketFare=itemView.findViewById(R.id.ticketFare);
            ticketDate=itemView.findViewById(R.id.ticketDate);
            ticketClass=itemView.findViewById(R.id.ticketClass);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            listItemClickListner.onListItemClick(getAdapterPosition());
        }
    }


}
