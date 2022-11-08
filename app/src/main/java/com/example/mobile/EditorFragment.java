package com.example.mobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobile.databinding.FragmentEditorBinding;
import com.example.mobile.model.TripEntity;
import com.example.mobile.sqlite.TripDatabaseAssessObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditorFragment extends Fragment {
    FragmentEditorBinding binding;
    EditText date_time;
    String dateTime;
    String[] names = new String[] {"Conference","Client meeting", "Holiday", "Visiting", "Discovery", "Interview"};
    String[] destinationList = new String[]{"Manchester United", "Arsenal", "Tottenham Hotspur", "Liverpool", "Chelsea", "Manchester City"};
    TripDatabaseAssessObject tripDao;
    int id;



    AutoCompleteTextView autoCompleteTripName;
    AutoCompleteTextView autoCompleteDestination;

    ArrayAdapter<String> adapterTripName;
    ArrayAdapter<String> adapterDestination;

    RadioGroup radioGroup;
    int radioID;

    String tripDesc;
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditorBinding.inflate(inflater,container,false);

        tripDao = new TripDatabaseAssessObject(getContext());

        radioGroup = binding.riskAssessment;

        tripDesc = binding.desc.getText().toString();

        autoCompleteTripName = binding.nameTrip;
        autoCompleteDestination = binding.destinationTrip;
        id = getArguments().getInt("tripId");

        String nameTrip = getArguments().getString("name");
        String destination = getArguments().getString("destination");

        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        Date date = (Date) bundle.getSerializable("date");




        date_time = binding.dateTime;
        date_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setDateTime(date_time);
            }
        });

        adapterTripName = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, names);
        adapterDestination = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, destinationList);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
                    case R.id.radioButtonNo:
                        radioID = 0;
                        break;
                    case R.id.radioButtonYes:
                        radioID = 1;
                        break;
                }
            }
        });

        tripDao.trip.observe(
                getViewLifecycleOwner(),
                trip ->{
                    autoCompleteTripName.setText(nameTrip);
                    autoCompleteDestination.setText(destination);
                    autoCompleteTripName.setAdapter(adapterTripName);
                    autoCompleteDestination.setAdapter(adapterDestination);
                    binding.riskAssessment.check(radioID);
                    binding.desc.setText(tripDesc);
                }
        );


        tripDao.getTripByID(String.valueOf(id));
        calendar.setTime(date);
        binding.addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigateUp();
            }
        });
        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(Gravity.CENTER);
//                Bundle bundle = new Bundle();
//                bundle.putInt("tripId",id);
//                Navigation.findNavController(getView()).navigate(R.id.expenseFragment, bundle);
            }
        });

        if (id == 0){
            binding.addExpense.setVisibility(View.GONE);

        }
        else binding.addExpense.setVisibility(View.VISIBLE);

        return binding.getRoot();
    }


    private void setDateTime(EditText dateTime){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                binding.dateTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    private void openDialog(int gravity){
        final Dialog dialog = new Dialog(getActivity());

    }

    private boolean saveAndReturn(){
        String name = binding.nameTrip.getText().toString();
        String destination = binding.destinationTrip.getText().toString();
        Date dateTime = calendar.getTime();
        int risk = radioID;
        String desc = binding.desc.getText().toString();

        TripEntity updateTrip
                = new TripEntity(String.valueOf(id)  != null ? id: Constant.NEW_ENTITY_ID, name, destination, dateTime, risk, desc);
        if(id == Constant.NEW_ENTITY_ID){
            tripDao.insert(updateTrip);
        }
        else tripDao.update(updateTrip);
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

}