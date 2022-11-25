package com.example.mobile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.example.mobile.databinding.FragmentLayoutDialogBinding;
import com.example.mobile.model.TripEntity;
import com.example.mobile.sqlite.TripDatabaseAssessObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditorFragment extends Fragment {
    FragmentEditorBinding binding;
    FragmentLayoutDialogBinding layoutDialogBinding;
    EditText date_time;
    String[] names = new String[] {"Conference","Client meeting", "Holiday", "Visiting", "Discovery", "Interview"};
    String[] transportationList = new String[]{"Bus", "Taxi", "Air plane"};
    TripDatabaseAssessObject tripDao;
    int id;
    boolean check = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    boolean flag = false;
    AutoCompleteTextView autoCompleteTripName;
    AutoCompleteTextView autoCompleteTransportation;

    ArrayAdapter<String> adapterTripName;
    ArrayAdapter<String> adapterTransportation;

    String destination;
    String participant;

    RadioGroup radioGroup;
    int radioID;
    String radioText;
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
        destination = binding.destinationTrip.getText().toString();
        participant = binding.participant.getText().toString();
        autoCompleteTripName = binding.nameTrip;
        autoCompleteTransportation = binding.transportationTrip;
        id = getArguments().getInt("tripId");

        String nameTrip = getArguments().getString("name");
        String destination = getArguments().getString("destination");
        String transportTrip = getArguments().getString("transportation");
        int participantTrip = getArguments().getInt("participant");
        int riskAAAA = getArguments().getInt("risk");
        System.out.println(radioID);
        tripDesc = getArguments().getString("description");
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
        adapterTransportation = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, transportationList);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
                    case R.id.radioButtonNo:
                        radioID = 0;
                        flag = true;
                        radioText = "No";
                        break;
                    case R.id.radioButtonYes:
                        flag = true;
                        radioID = 1;
                        radioText = "Yes";
                        break;
                }
            }
        });

        tripDao.trip.observe(
                getViewLifecycleOwner(),
                trip ->{
                    autoCompleteTripName.setText(nameTrip);
                    autoCompleteTransportation.setText(destination);
                    autoCompleteTripName.setAdapter(adapterTripName);
                    autoCompleteTransportation.setAdapter(adapterTransportation);
                    binding.riskAssessment.check(checkRadioID(riskAAAA));
                    binding.desc.setText(tripDesc);

                    binding.transportationTrip.setText(transportTrip);
                    binding.destinationTrip.setText(destination);
                    binding.participant.setText(Integer.toString(participantTrip));
                    binding.dateTime.setText(simpleDateFormat.format(date));
                }
        );


        tripDao.getTripByID(String.valueOf(id));
        calendar.setTime(date);
        binding.addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = CheckAllFields();
//                saveAndReturn();
                if(check){
                    openDialog();
                }
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigateUp();
            }
        });

        if (id == 0){
            binding.addExpense.setVisibility(View.GONE);

        }
        else {
            binding.addExpense.setVisibility(View.VISIBLE);
            binding.addTripTitle.setText("Trip Detail");
            binding.addTripText.setText("Save");
            binding.addExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(getView()).navigate(R.id.expenseFragment, bundle);
                }
            });
        }

        return binding.getRoot();
    }

    private int checkRadioID(int riskAAAA) {
        return riskAAAA==1?R.id.radioButtonYes:R.id.radioButtonNo;
    }

    private boolean CheckAllFields() {
        if(binding.nameTrip.length() == 0){
            binding.nameTrip.setError("This field is required");
            return false;
        }
        else if (binding.destinationTrip.length() == 0){
            binding.destinationTrip.setError("This field is required");
            return false;
        }
        else if (binding.dateTime.length() == 0){
            binding.dateTime.setError("This field is required");
            return false;
        }
        else if(binding.transportationTrip.length() == 0){
            binding.transportationTrip.setError("This field is required");
            return false;
        }
        else if(binding.participant.length() == 0){
            binding.participant.setError("This field is required");
            return false;
        }
        else if (flag == false){
            binding.radioButtonYes.setError("This field is required");
            binding.radioButtonNo.setError("This field is required");
            return false;
        }
        return true;
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


    private void openDialog(){
        builder = new AlertDialog.Builder(getContext());
        layoutDialogBinding = FragmentLayoutDialogBinding.inflate(LayoutInflater.from(getContext()),null,false);
        layoutDialogBinding.nameTrip.setText("Name: " + binding.nameTrip.getText().toString());
        layoutDialogBinding.destinationTrip.setText("Destination: " + binding.destinationTrip.getText().toString());
        layoutDialogBinding.date.setText("Date: " + binding.dateTime.getText().toString());
        layoutDialogBinding.risk.setText("Risk: " + radioText);
        layoutDialogBinding.description.setText("Description: " + binding.desc.getText().toString());
        layoutDialogBinding.transportationTrip.setText("Transportation: " + binding.transportationTrip.getText().toString());
        layoutDialogBinding.participant.setText("Participant: " + binding.participant.getText().toString());
        layoutDialogBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
                dialog.dismiss();
            }
        });

        layoutDialogBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        builder.setView(layoutDialogBinding.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private boolean saveAndReturn(){
        String name = binding.nameTrip.getText().toString();
        String destination = binding.destinationTrip.getText().toString();
        Date dateTime = calendar.getTime();
        int risk = radioID;
        String desc = binding.desc.getText().toString();
        String transportation = binding.transportationTrip.getText().toString();
        int participant = Integer.parseInt(binding.participant.getText().toString());
        TripEntity updateTrip
                = new TripEntity(String.valueOf(id)  != null ? id: Constant.NEW_ENTITY_ID, name, destination, transportation, participant, dateTime, risk, desc);
        if(id == Constant.NEW_ENTITY_ID){
            tripDao.insert(updateTrip);
        }
        else tripDao.update(updateTrip);
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

}