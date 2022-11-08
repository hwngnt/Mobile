package com.example.mobile;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobile.databinding.FragmentAddExpenseBinding;
import com.example.mobile.model.ExpenseEntity;
import com.example.mobile.sqlite.ExpenseDatabaseAssessObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addExpenseFragment extends Fragment{
    FragmentAddExpenseBinding binding;
    String[] types = new String[] {"Food","Travel"};
    ExpenseDatabaseAssessObject expenseDao;
    EditText time;
    int tripId;
    int expenseId;

    AutoCompleteTextView autoCompleteType;
    ArrayAdapter<String> adapterExpenseType;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss aaa");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater,container,false);

        String typeExpense = getArguments().getString("types");
        tripId = getArguments().getInt("tripId");
        expenseId = getArguments().getInt("expenseId");
        expenseDao = new ExpenseDatabaseAssessObject(getContext(),tripId);
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        Date date = (Date) bundle.getSerializable("date");
        time = binding.time;
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setTime(time);
            }
        });
        autoCompleteType = binding.typeExpense;
        adapterExpenseType = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, types);

        expenseDao.expense.observe(
                getViewLifecycleOwner(),
                expense ->{
                    autoCompleteType.setText(typeExpense);
                    autoCompleteType.setAdapter( adapterExpenseType);
                    binding.addAmount.setText(binding.addAmount.getText().toString());
                    binding.comments.setText(binding.comments.getText().toString());
                }
        );
        expenseDao.getExpenseByID(String.valueOf(expenseId));
        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
            }
        });


        return binding.getRoot();
    }

    private void setTime(EditText time){
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                binding.time.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new TimePickerDialog(requireContext(), timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }


    private boolean saveAndReturn(){
        String type = binding.typeExpense.getText().toString();
        float amount = Float.valueOf(binding.addAmount.getText().toString());
        Date time = calendar.getTime();
        String comment = binding.comments.getText().toString();

        ExpenseEntity updateExpense
                = new ExpenseEntity(expenseId != 0 ? expenseId: Constant.NEW_ENTITY_ID, type, amount, time, comment,tripId);
        if (expenseId == Constant.NEW_ENTITY_ID){
            expenseDao.insert(updateExpense);
        }
        else expenseDao.update(updateExpense);
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }
}

