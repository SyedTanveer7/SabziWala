package com.example.admin.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.R;
import com.example.admin.Webservices.Repositories.ProductRepository;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import static com.example.admin.Adapters.VendorsAdapter.selectedVendor;
import static com.example.admin.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView shopName;
    ProductRepository productRepository;
    private ImageView backArrow;
    public static CamomileSpinner sprogressBar;
    public static RelativeLayout stransparent_layer, sprogressDialog;

    public ShopProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_products, container, false);
        initializeViews(view);
        return view;
    }

    public void initializeViews(View v) {

        sprogressBar = v.findViewById(R.id.restaurantProgress);
        sprogressBar.start();
        sprogressDialog = v.findViewById(R.id.progressDialog);
        stransparent_layer = v.findViewById(R.id.transparent_layer);

        productRepository = new ProductRepository(getActivity());

        shopName = v.findViewById(R.id.textView11);
        recyclerView = v.findViewById(R.id.recyclerView1);
        backArrow = v.findViewById(R.id.imageView12);

        shopName.setText(selectedVendor.getShopName());

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new VendorsFragment())
                        .commit();
                toolbarTitle.setText(R.string.title_vendors);
            }
        });


        productRepository.getGridViewProductsFromServer(recyclerView, selectedVendor);

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


}
