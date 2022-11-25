package com.example.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.adapter.TripListAdapter;
import com.example.mobile.databinding.FragmentMainBinding;
import com.example.mobile.model.TripEntity;
import com.example.mobile.sqlite.TripDatabaseAssessObject;

import java.util.Calendar;

public class MainFragment extends Fragment implements TripListAdapter.ListTripListener {
    FragmentMainBinding binding;
    private TripDatabaseAssessObject tripDao;
    private TripListAdapter adapter;
    private EditText searchView;
    @Override
    public void onResume() {
        super.onResume();
        tripDao.getTrips();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        tripDao = new TripDatabaseAssessObject(getContext());
        RecyclerView rv  = binding.recyclerView;
        // fixed size for each row
        rv.setHasFixedSize(true);
        // deco a line in each row
        searchView = binding.searchView;
        searchView.clearFocus();
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tripDao.search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext()).getOrientation())
        ));
        tripDao.tripList.observe(
            getViewLifecycleOwner(),
                tripList -> {

                    adapter = new TripListAdapter(tripList, this);
                    binding.recyclerView.setAdapter(adapter);

                    if (tripList.size() == 0){
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
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("tripId",0);
                bundle.putString("name","");
                bundle.putString("destination","");
                bundle.putSerializable("date",Calendar.getInstance().getTime());
                bundle.putInt("risk", 0);
                bundle.putString("description"," ");
                bundle.putString("transportation", "");
                bundle.putInt("participant", 0);
                Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
            }
        });
        binding.translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.translatorFragment);
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onItemClicked(TripEntity trip) {
        TripEntity aTrip = tripDao.getById(String.valueOf(trip.getId()));
        if(trip != null){
            Bundle bundle = new Bundle();
            bundle.putInt("tripId",trip.getId());
            bundle.putString("name",trip.getName());
            bundle.putString("destination",trip.getDestination());
            System.out.println("Date: " + trip.getDate());
            bundle.putSerializable("date",trip.getDate());
            bundle.putInt("risk",trip.isRisk());
            bundle.putString("description",trip.getDescription());
            bundle.putString("transportation", trip.getTransportation());
            bundle.putInt("participant", trip.getParticipant());
            Navigation.findNavController(getView()).navigate(R.id.editorFragment,bundle);
        }
    }

    @Override
    public void onItemDelete(TripEntity trip){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Delete..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_delete_24);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure to delete this trip ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                tripDao.delete(String.valueOf(trip.getId()));
                tripDao.getTrips();
            }
        });
        alertDialogBuilder.setNegativeButton("No", null);
        alertDialogBuilder.show();
    }


}