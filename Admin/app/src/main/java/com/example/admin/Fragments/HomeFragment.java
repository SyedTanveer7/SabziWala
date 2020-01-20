package com.example.admin.Fragments;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.Webservices.Models.Overview;
import com.example.admin.Webservices.Models.Product;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button productsButton;
    private Button vendorsButton;
    private Button reportsButton;
    private Button settingsButton;
    TextView textView, textView1, textView2, textView3, textView4;
    WebAPIs webAPIs;
    public CamomileSpinner progressBar;
    public RelativeLayout transparent_layer, progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initializeEvents(v);

        return v;
    }

    public void initializeEvents(View v) {
        progressBar = v.findViewById(R.id.restaurantProgress);
        progressBar.start();
        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);
        textView = v.findViewById(R.id.textView40);
        textView1 = v.findViewById(R.id.textView32);
        textView2 = v.findViewById(R.id.textView34);
        textView3 = v.findViewById(R.id.textView36);
        textView4 = v.findViewById(R.id.textView38);
        productsButton = v.findViewById(R.id.button2);
        vendorsButton = v.findViewById(R.id.button3);
        reportsButton = v.findViewById(R.id.button4);
        settingsButton = v.findViewById(R.id.button5);

        productsButton.setOnClickListener(this);
        vendorsButton.setOnClickListener(this);
        reportsButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        getAdminOverview();

        productsButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    productsButton.setBackgroundResource(R.drawable.btn_shape);
                    productsButton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    productsButton.setBackgroundResource(R.drawable.btn_selected);
                    productsButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


        vendorsButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    vendorsButton.setBackgroundResource(R.drawable.btn_shape);
                    vendorsButton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vendorsButton.setBackgroundResource(R.drawable.btn_selected);
                    vendorsButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


        reportsButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    reportsButton.setBackgroundResource(R.drawable.btn_shape);
                    reportsButton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    reportsButton.setBackgroundResource(R.drawable.btn_selected);
                    reportsButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


        settingsButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    settingsButton.setBackgroundResource(R.drawable.btn_shape);
                    settingsButton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    settingsButton.setBackgroundResource(R.drawable.btn_selected);
                    settingsButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }


    private void startCountAnimation(final TextView textView, int value) {

        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    private void startSalesAnimation(final TextView textView, int value) {

        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("Rs " + animation.getAnimatedValue().toString() + "/-");
            }
        });
        animator.start();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        if (v == productsButton) {
            replaceFragment(new ProductsFragment());
            toolbarTitle.setText(R.string.title_products);
        } else if (v == vendorsButton) {
            replaceFragment(new VendorsFragment());
            toolbarTitle.setText(R.string.title_vendors);
        } else if (v == reportsButton) {
            replaceFragment(new ReportsFragment());
            toolbarTitle.setText(R.string.title_reports);
        } else if (v == settingsButton) {
            replaceFragment(new SettingsFragment());
            toolbarTitle.setText(R.string.title_vendors);
        }
    }

    public void getAdminOverview() {
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Overview> apiCall = webAPIs.getAdminOverview();
        apiCall.enqueue(new Callback<Overview>() {
            @Override
            public void onResponse(Call<Overview> call, Response<Overview> response) {
                if (response.isSuccessful()) {
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);


                    if (response.body().getTotalVendors()==null) {
                        startCountAnimation(textView, 0);
                    } else {
                        startCountAnimation(textView, Integer.parseInt(response.body().getTotalVendors()));
                    }
                    if (response.body().getTotalOrders()==null) {
                        startCountAnimation(textView1, 0);
                    } else {
                        startCountAnimation(textView1, Integer.parseInt(response.body().getTotalOrders()));
                    }
                    if (response.body().getTotalSales()==null) {
                        startSalesAnimation(textView2, 0);
                    } else {
                        int b =  (int) Math.round(Float.parseFloat(response.body().getTotalSales()));

                        startSalesAnimation(textView2, b);
                    }
                    if (response.body().getDeliveredOrders()==null) {
                        startCountAnimation(textView3, 0);
                    } else {
                        startCountAnimation(textView3, Integer.parseInt(response.body().getDeliveredOrders()));
                    }
                    if (response.body().getTotalComission()==null) {
                        startSalesAnimation(textView4, 0);
                    } else {

                        int a =  (int) Math.round(Float.parseFloat(response.body().getTotalComission()));
                        startSalesAnimation(textView4,a);
                    }


                }
            }

            @Override
            public void onFailure(Call<Overview> call, Throwable t) {
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
                startCountAnimation(textView, 0);
                startCountAnimation(textView1, 0);
                startSalesAnimation(textView2, 0);
                startCountAnimation(textView3, 0);
                startSalesAnimation(textView4, 0);
                Toast.makeText(getActivity(), "Error while fetching Overview!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
