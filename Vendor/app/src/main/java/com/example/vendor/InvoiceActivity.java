package com.example.vendor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.OrderDetailsAdapter;
import com.example.vendor.Webservices.Models.Invoice;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Adapters.OrdersAdapter.selectedOrder;
import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.toolbarTitle;

public class InvoiceActivity extends AppCompatActivity implements OnMapReadyCallback {

    Invoice orderedProducts;

    RecyclerView recyclerView;
    private TextView textView_invoiceNumber, textView_customerName, textView_date, textView_time,
            textView_amount, textView_status, textView_shopName;
    private ImageView imageBack;
    MapView mapView;
    GoogleMap map;
    public static CamomileSpinner progressBar;
    public static RelativeLayout transparent_layer, progressDialog;
    private static final LatLng LHR = new LatLng(32.5336678, 74.3631504);
    Double latitude = 0.0, longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);
        initializeEvents();
    }

    public void initializeEvents() {
        progressBar = findViewById(R.id.restaurantProgress);
        progressBar.start();
        progressDialog = findViewById(R.id.progressDialog);
        transparent_layer = findViewById(R.id.transparent_layer);

        textView_invoiceNumber = findViewById(R.id.textView22);
        textView_customerName = findViewById(R.id.textView24);
        textView_date = findViewById(R.id.textView28);
        textView_time = findViewById(R.id.textView30);
        textView_amount = findViewById(R.id.textView34);
        textView_status = findViewById(R.id.textView32);
        textView_shopName = findViewById(R.id.textView11);

        imageBack = findViewById(R.id.image_back);

        toolbarTitle.setText(R.string.title_order_invoice);

        // mTableLayout = view.findViewById(R.id.tableInvoices);
        recyclerView = findViewById(R.id.recyclerView3);
        //  mTableLayout.setStretchAllColumns(true);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceActivity.this, MainActivity.class));
                finish();
            }
        });


    }

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
                    latitude = Double.valueOf(response.body().getCustomerLatitude());
                    longitude = Double.valueOf(response.body().getCustomerLongitude());


                    textView_invoiceNumber.setText(orderedProducts.getId() + "");
                    textView_customerName.setText(orderedProducts.getName());
                    textView_date.setText(orderedProducts.getDate());
                    textView_time.setText(orderedProducts.getTime());
                    textView_amount.setText("Rs " + orderedProducts.getTotalBill() + "/-");
                    textView_status.setText(orderedProducts.getStatus());
                    textView_shopName.setText(vendor.getShopName());


                    map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InvoiceActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(orderedProducts.getProducts(), InvoiceActivity.this);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);


                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Toast.makeText(InvoiceActivity.this, "Error while fetching Invoice", Toast.LENGTH_SHORT).show();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        //  map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


    }

    @Override
    public void onResume() {
        getInvoice();

        super.onResume();
        mapView.onResume();
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
