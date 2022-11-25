package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.adapter.ExpenseListAdapter;
import com.example.mobile.databinding.FragmentExpenseBinding;
import com.example.mobile.model.ExpenseEntity;
import com.example.mobile.model.TripEntity;
import com.example.mobile.sqlite.ExpenseDatabaseAssessObject;
import com.example.mobile.sqlite.TripDatabaseAssessObject;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.ExpenseListListener{
    FragmentExpenseBinding binding;
    int tripId;
    private ExpenseDatabaseAssessObject expenseDao;
    private TripDatabaseAssessObject tripDao;
    private ExpenseListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExpenseBinding.inflate(inflater,container,false);
        tripId = getArguments().getInt("tripId");
        tripDao = new TripDatabaseAssessObject(getContext());
        expenseDao = new ExpenseDatabaseAssessObject(getContext(),tripId);
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext()).getOrientation())
        ));
        expenseDao.expenseList.observe(
                getViewLifecycleOwner(),
                expenseList ->{
                    adapter = new ExpenseListAdapter(expenseList, this);
                    binding.recyclerView.setAdapter(adapter);
                    if (expenseList.size() == 0){
                        binding.nothing.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                    else{
                        binding.nothing.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );
//        System.out.println("TRIP ID ==============" + tripId);
        TripEntity aTrip = tripDao.getById(String.valueOf(tripId));
        System.out.println("TRIP =============" + aTrip.getDate());
        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("tripId",tripId);
                bundle.putInt("expenseId",0);
                bundle.putString("type", "");
                bundle.putFloat("amount", 0);
                bundle.putSerializable("date", aTrip.getDate());
                bundle.putString("comment", "");
                Navigation.findNavController(getView()).navigate(R.id.addExpenseFragment, bundle);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate((R.id.mainFragment));
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
    }
    @Override
    public void onItemClicked(ExpenseEntity expense) {
        ExpenseEntity anExpense = expenseDao.getByID(String.valueOf(expense.getId()));
        System.out.println("===============" + expense);
        if (expense != null){
            Bundle bundle = new Bundle();
            bundle.putInt("tripId", tripId);
            bundle.putInt("expenseId", expense.getId());
            bundle.putString("type", expense.getType());
            bundle.putFloat("amount", expense.getAmount());
            bundle.putSerializable("date", expense.getTime());
            bundle.putString("comment", expense.getComment());
            System.out.println("Bundle: " + bundle);
            Navigation.findNavController(getView()).navigate(R.id.addExpenseFragment, bundle);
        }
    }
    @Override
    public void onItemDelete(ExpenseEntity expense) {
        expenseDao.delete(String.valueOf(expense.getId()));
        expenseDao.getAll();
    }
}