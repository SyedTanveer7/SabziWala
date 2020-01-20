package com.example.vendor.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.vendor.Adapters.OrderDetailsAdapter;
import com.example.vendor.Adapters.OrdersAdapter;
import com.example.vendor.MainActivity;
import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Invoice;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Adapters.OrdersAdapter.selectedOrder;
import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment implements OnMapReadyCallback{

    Invoice orderedProducts;
    private TableLayout mTableLayout;
    RecyclerView recyclerView;
    private ProgressDialog mProgressBar;
    private TextView textView_invoiceNumber, textView_customerName, textView_date, textView_time,
            textView_amount, textView_status, textView_shopName, textView_address, textView_bill;
    private ImageView imageBack;
    MapView mapView;
    GoogleMap map;

    private static final LatLng LHR = new LatLng(32.5336678, 74.3631504);
    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);

        initializeEvents(view);




        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
       map.addMarker(new MarkerOptions().position(LHR));
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(LHR, 13));
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void initializeEvents(View view) {

        getInvoice();
        textView_invoiceNumber = view.findViewById(R.id.textView22);
        textView_customerName = view.findViewById(R.id.textView24);
        textView_date = view.findViewById(R.id.textView28);
        textView_time = view.findViewById(R.id.textView30);
        textView_amount = view.findViewById(R.id.textView34);
        textView_status = view.findViewById(R.id.textView32);
        textView_shopName = view.findViewById(R.id.textView11);
        textView_address = view.findViewById(R.id.textView26);
      //  textView_bill = view.findViewById(R.id.textView36);
        imageBack = view.findViewById(R.id.image_back);

        toolbarTitle.setText(R.string.title_order_invoice);

        // mTableLayout = view.findViewById(R.id.tableInvoices);
        recyclerView = view.findViewById(R.id.recyclerView3);
        //  mTableLayout.setStretchAllColumns(true);

//        SupportMapFragment  fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//
//        fragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//                mMap.clear(); //clear old markers
//
//                if(orderedProducts!=null) {
//                    double lat = Double.valueOf(orderedProducts.getCustomerLatitude());
//                    double lng = Double.valueOf(orderedProducts.getCustomerLongitude());
//
//                    CameraPosition googlePlex = CameraPosition.builder()
//                            .target(new LatLng(lat, lng))
//                            .zoom(10)
//                            .bearing(0)
//                            .tilt(45)
//                            .build();
//
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
//
//                    mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(37.4219999, -122.0862462))
//                            .title("Spider Man"));
//
//                }
//
//
//
//
//            }
//        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });


    }


//    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
//                .build();
//    }

    public void getInvoice() {


        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Invoice invoice = new Invoice();
        invoice.setId(String.valueOf(selectedOrder.getId()));
        Call<Invoice> apiCall = webAPIs.getInvoice(invoice);

        apiCall.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (response.isSuccessful()) {

                    orderedProducts = response.body();


                    textView_invoiceNumber.setText(orderedProducts.getId() + "");
                    textView_customerName.setText(orderedProducts.getName());
                    textView_date.setText(orderedProducts.getDate());
                    textView_time.setText(orderedProducts.getTime());
                    textView_amount.setText("Rs " + orderedProducts.getTotalBill() + "/-");
                    textView_status.setText(orderedProducts.getStatus());
                    textView_shopName.setText(vendor.getShopName());
                    textView_address.setText(orderedProducts.getAddress());
//                    textView_bill.setText("Rs " + orderedProducts.getTotalBill() + "/-");
                    // loadData(orderedProducts);

                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(orderedProducts.getProducts(), getActivity());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());

            }
        });
    }


    public void loadData(List<Invoice> productsInvoice) {


        TableRow t1 = new TableRow(getActivity());
        t1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView serial = new TextView(getActivity());
        serial.setMaxLines(1);
        serial.setSingleLine();
        serial.setText("Serial#");
        serial.setGravity(Gravity.CENTER);
        serial.setTypeface(null, Typeface.BOLD);
        serial.setTextSize(18);
        serial.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        serial.setWidth(90);
        serial.setPadding(10, 10, 10, 10);
        serial.setBackgroundResource(R.drawable.table_row);

        t1.addView(serial);

        TextView productName = new TextView(getActivity());
        productName.setMaxLines(1);
        productName.setSingleLine();
        productName.setTypeface(null, Typeface.BOLD);
        productName.setText("Product");
        productName.setGravity(Gravity.CENTER);
        productName.setTextSize(18);
        productName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        productName.setWidth(90);
        productName.setPadding(10, 10, 10, 10);
        productName.setBackgroundResource(R.drawable.table_row);

        t1.addView(productName);


        TextView price = new TextView(getActivity());
        price.setMaxLines(1);
        price.setSingleLine();
        price.setText("Price");
        price.setTypeface(null, Typeface.BOLD);
        price.setGravity(Gravity.CENTER);
        price.setTextSize(18);
        price.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        price.setWidth(90);
        price.setPadding(10, 10, 10, 10);
        price.setBackgroundResource(R.drawable.table_row);

        t1.addView(price);
        mTableLayout.addView(t1);


        for (int i = 0; i < productsInvoice.size(); i++) {

            TableRow t3 = new TableRow(getActivity());
            t3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textview = new TextView(getActivity());
            textview.setMaxLines(1);
            textview.setSingleLine();
            textview.setText((i + 1) + "");
            textview.setGravity(Gravity.CENTER);
            textview.setTextSize(18);
            textview.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            textview.setWidth(90);
            textview.setPadding(10, 10, 10, 10);
            textview.setBackgroundResource(R.drawable.table_row);

            t3.addView(textview);


            TextView name = new TextView(getActivity());
            name.setMaxLines(1);
            name.setSingleLine();
            name.setText(productsInvoice.get(i).getName());
            name.setGravity(Gravity.CENTER);
            name.setTextSize(18);
            name.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            name.setWidth(90);
            name.setPadding(10, 10, 10, 10);
            name.setBackgroundResource(R.drawable.table_row);

            t3.addView(name);

            TextView pric = new TextView(getActivity());
            pric.setMaxLines(1);
            pric.setSingleLine();
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            pric.setGravity(Gravity.CENTER);
            pric.setTextSize(18);
            pric.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            pric.setWidth(90);
            pric.setPadding(10, 10, 10, 10);
            pric.setBackgroundResource(R.drawable.table_row);


            t3.addView(pric);


            mTableLayout.addView(t3);
        }
        mProgressBar.dismiss();
    }


//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        if(orderedProducts!=null) {
//            double lat = Double.valueOf(orderedProducts.getCustomerLatitude());
//            double lng = Double.valueOf(orderedProducts.getCustomerLongitude());
//            LatLng sydney = new LatLng(lat, lng);
//
//            googleMap.addMarker(new MarkerOptions().position(sydney)
//                    .title("Customer Location"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
//    }



    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
