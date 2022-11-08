package com.example.mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile.adapter.ExpenseListAdapter;
import com.example.mobile.databinding.FragmentEditorBinding;
import com.example.mobile.databinding.FragmentExpenseBinding;
import com.example.mobile.model.ExpenseEntity;
import com.example.mobile.sqlite.ExpenseDatabaseAssessObject;

import java.util.Calendar;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.ExpenseListListener{
    FragmentExpenseBinding binding;
    int tripId;
    private ExpenseDatabaseAssessObject expenseDao;
    private ExpenseListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExpenseBinding.inflate(inflater,container,false);
        tripId = getArguments().getInt("tripId");
        System.out.println("trip id: " +tripId);
        expenseDao = new ExpenseDatabaseAssessObject(getContext(),tripId);
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext()).getOrientation())
        ));
        System.out.println("List expense: "+ expenseDao.getAll().size());
        expenseDao.expenseList.observe(
                getViewLifecycleOwner(),
                expenseList ->{
                    adapter = new ExpenseListAdapter(expenseList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );

        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("tripId",tripId);
                bundle.putInt("expenseId",0);
                bundle.putString("type", "");
                bundle.putFloat("amount", 0);
                bundle.putSerializable("time", Calendar.getInstance().getTime());
                bundle.putString("comment", "");
                Navigation.findNavController(getView()).navigate(R.id.addExpenseFragment, bundle);
            }
        });
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
         expenseDao.getAll();
        System.out.println("List expense: "+ expenseDao.getAll().size());
    }
    @Override
    public void onItemClicked(ExpenseEntity expense) {
        ExpenseEntity anExpense = expenseDao.getByID(String.valueOf(expense.getId()));
        if (expense != null){
            Bundle bundle = new Bundle();
            bundle.putInt("tripId", tripId);
            bundle.putInt("expenseId", expense.getId());
            bundle.putString("type", expense.getType());
            bundle.putFloat("amount", expense.getAmount());
            bundle.putSerializable("time", expense.getTime());
            bundle.putString("comment", expense.getComment());
            Navigation.findNavController(getView()).navigate(R.id.addExpenseFragment, bundle);
        }
    }
    @Override
    public void onItemDelete(ExpenseEntity expense) {
        expenseDao.delete(String.valueOf(expense.getId()));
        expenseDao.getAll();
    }
}