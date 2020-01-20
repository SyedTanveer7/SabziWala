package com.example.admin.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.admin.R;
import com.example.admin.Webservices.Repositories.ProductRepository;
import com.example.admin.Webservices.Repositories.VendorRepository;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    private FloatingActionButton buttonNewProduct;
    private RecyclerView recyclerView;
    ProductRepository repository;
    VendorRepository vendorRepository;
    public static Activity activity;
    public static CamomileSpinner progressBar;
    public static RelativeLayout transparent_layer, progressDialog;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        initializeEvents(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        repository = new ProductRepository(getActivity());
        vendorRepository = new VendorRepository(getActivity());
        vendorRepository.getShopsFromServer();

        repository.getProductsFromServer(recyclerView);

        activity = getActivity();




        return view;

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


    public void initializeEvents(View v) {
        progressBar = v.findViewById(R.id.restaurantProgress);
        progressBar.start();
        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);

        buttonNewProduct = v.findViewById(R.id.button_new_product);
        buttonNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new NewProductFragment());
                toolbarTitle.setText(R.string.title_new_product);
            }
        });
    }


}











