package com.example.mobile.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.databinding.ListItemBinding;
import com.example.mobile.model.TripEntity;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder>{

    public interface ListTripListener {
        void onItemClicked(TripEntity trip);
        void onItemDelete(TripEntity trip);
    }

    private ListTripListener listener;

    public class TripViewHolder extends RecyclerView.ViewHolder{
        private final ListItemBinding itemViewBinding;

        public TripViewHolder(View itemView){
            super(itemView);
            itemViewBinding = ListItemBinding.bind(itemView);
        }

        public void bindData(TripEntity tData){
            itemViewBinding.name.setText(tData.getName());
            itemViewBinding.getRoot().setOnClickListener(v -> listener.onItemClicked(tData));
            itemViewBinding.fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemDelete(tData);
                }
            });
        }
    }

    private List<TripEntity> tripList;

    public TripListAdapter(List<TripEntity> tripList, ListTripListener listener){
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.list_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripEntity tData = tripList.get(position);
        holder.bindData(tData);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
