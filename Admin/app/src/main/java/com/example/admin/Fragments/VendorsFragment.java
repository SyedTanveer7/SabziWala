package com.example.admin.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.admin.R;
import com.example.admin.Webservices.Repositories.VendorRepository;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import static com.example.admin.Fragments.ProductsFragment.progressBar;
import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorsFragment extends Fragment {

    private FloatingActionButton buttonNewVendor;
    private RecyclerView recyclerView;
    VendorRepository repository;
    public static CamomileSpinner vprogressBar;
    public static RelativeLayout vtransparent_layer, vprogressDialog;


    public VendorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vendors, container, false);
        initializeEvents(v);
        repository = new VendorRepository(getActivity());

        if (checkIfAlreadyhavePermission()) {
            repository.getVendorsFromServer(recyclerView);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        return v;
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    public void initializeEvents(View v) {

        vprogressBar = v.findViewById(R.id.restaurantProgress);
        vprogressBar.start();
        vprogressDialog = v.findViewById(R.id.progressDialog);
        vtransparent_layer = v.findViewById(R.id.transparent_layer);
        buttonNewVendor = v.findViewById(R.id.button_new_vendor);
        recyclerView = v.findViewById(R.id.recyclerView);

        buttonNewVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new NewVendorFragment());
                toolbarTitle.setText(R.string.title_new_vendor);
            }
        });
    }


}
