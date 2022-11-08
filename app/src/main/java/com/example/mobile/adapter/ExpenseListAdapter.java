package com.example.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.databinding.ListItemBinding;
import com.example.mobile.model.ExpenseEntity;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    public interface  ExpenseListListener{
        void onItemClicked(ExpenseEntity expense);
        void onItemDelete(ExpenseEntity expense);
    }

    private ExpenseListListener listener;

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        private final ListItemBinding itemViewBinding;
        public ExpenseViewHolder(View itemView){
            super(itemView);
            itemViewBinding = ListItemBinding.bind(itemView);
        }

        public void bindData(ExpenseEntity tData){
            itemViewBinding.name.setText(tData.getType());
            itemViewBinding.getRoot().setOnClickListener(v -> listener.onItemClicked(tData));
            itemViewBinding.fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemDelete(tData);
                }
            });
        }
    }

    private List<ExpenseEntity> expenseList;

    public ExpenseListAdapter(List<ExpenseEntity> expenseList, ExpenseListListener listener){
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.list_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseEntity eData = expenseList.get(position);
        holder.bindData(eData);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
