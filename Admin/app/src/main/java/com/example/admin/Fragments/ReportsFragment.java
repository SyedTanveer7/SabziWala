package com.example.admin.Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.Webservices.Models.Report;
import com.example.admin.Webservices.Models.Vendor;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment implements View.OnClickListener {

    private TableLayout tableLayout;
    private ImageView startCalender;
    private ImageView endCalender;
    private TextView startDate;
    private TextView endDate;
    private Button viewReport;
    public static int mYear, mMonth, mDay;
    private WebAPIs webAPIs;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        initializeEvents(view);
        return view;
    }

    public void initializeEvents(View view) {
        tableLayout = view.findViewById(R.id.tableInvoices);
        startCalender = view.findViewById(R.id.imageView10);
        startDate = view.findViewById(R.id.textView14);
        endCalender = view.findViewById(R.id.imageView11);
        endDate = view.findViewById(R.id.textView16);
        viewReport = view.findViewById(R.id.button);
        tableLayout.setStretchAllColumns(true);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startCalender.setOnClickListener(this);
        endCalender.setOnClickListener(this);
        viewReport.setOnClickListener(this);


        viewReport.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    viewReport.setBackgroundResource(R.drawable.btn_shape);
                    viewReport.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    viewReport.setBackgroundResource(R.drawable.btn_selected);
                    viewReport.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }

    public void loadData(List<Report> reports) {


        TableRow t1 = new TableRow(getActivity());
        t1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        t1.setHorizontalScrollBarEnabled(true);
//        TextView serial = new TextView(getActivity());
//        serial.setMaxLines(1);
//        serial.setSingleLine();
//        serial.setText("Sr #");
//        serial.setGravity(Gravity.CENTER);
//        serial.setTypeface(null, Typeface.BOLD);
//        serial.setTextSize(18);
//        serial.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        serial.setWidth(90);
//        serial.setPadding(10, 10, 10, 10);
//        serial.setBackgroundResource(R.drawable.table_row);
//
//        t1.addView(serial);

        TextView productName = new TextView(getActivity());
        productName.setMaxLines(1);
        productName.setSingleLine();
        productName.setTypeface(null, Typeface.BOLD);
        productName.setText("Shop");
        productName.setGravity(Gravity.CENTER);
        productName.setTextSize(18);
        productName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        productName.setWidth(150);
        productName.setPadding(10, 10, 10, 10);
        productName.setBackgroundResource(R.drawable.table_row);

        t1.addView(productName);


        TextView price = new TextView(getActivity());
        price.setMaxLines(1);
        price.setSingleLine();
        price.setText("Orders");
        price.setTypeface(null, Typeface.BOLD);
        price.setGravity(Gravity.CENTER);
        price.setTextSize(18);
        price.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        price.setWidth(40);
        price.setPadding(10, 10, 10, 10);
        price.setBackgroundResource(R.drawable.table_row);

        t1.addView(price);

        TextView sales = new TextView(getActivity());
        sales.setMaxLines(1);
        sales.setSingleLine();
        sales.setText("Sales");
        sales.setTypeface(null, Typeface.BOLD);
        sales.setGravity(Gravity.CENTER);
        sales.setTextSize(18);
        sales.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        sales.setWidth(40);
        sales.setPadding(10, 10, 10, 10);
        sales.setBackgroundResource(R.drawable.table_row);

        t1.addView(sales);

        TextView comission = new TextView(getActivity());
        comission.setMaxLines(1);
        comission.setSingleLine();
        comission.setText("Comission");
        comission.setTypeface(null, Typeface.BOLD);
        comission.setGravity(Gravity.CENTER);
        comission.setTextSize(18);
        comission.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        comission.setWidth(40);
        comission.setPadding(10, 10, 10, 10);
        comission.setBackgroundResource(R.drawable.table_row);

        t1.addView(comission);

        tableLayout.addView(t1);


        for (int i = 0; i < reports.size(); i++) {

            TableRow t3 = new TableRow(getActivity());
            t3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//            TextView textview = new TextView(getActivity());
//            textview.setMaxLines(1);
//            textview.setSingleLine();
//            textview.setText((i + 1) + "");
//            textview.setGravity(Gravity.CENTER);
//            textview.setTextSize(18);
//            textview.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//            textview.setWidth(90);
//            textview.setPadding(10, 10, 10, 10);
//            textview.setBackgroundResource(R.drawable.table_row);
//
//            t3.addView(textview);


            TextView name = new TextView(getActivity());
            name.setMaxLines(1);
            name.setSingleLine();
            name.setText(reports.get(i).getShopName());
            name.setGravity(Gravity.CENTER);
            name.setTextSize(18);
            name.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            name.setWidth(150);
            name.setPadding(10, 10, 10, 10);
            name.setBackgroundResource(R.drawable.table_row);

            t3.addView(name);

            TextView order = new TextView(getActivity());
            order.setMaxLines(1);
            order.setSingleLine();
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            order.setText(reports.get(i).getOrders());
            order.setGravity(Gravity.CENTER);
            order.setTextSize(18);
            order.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            order.setWidth(40);
            order.setPadding(10, 10, 10, 10);
            order.setBackgroundResource(R.drawable.table_row);


            t3.addView(order);

            TextView sale = new TextView(getActivity());
            sale.setMaxLines(1);
            sale.setSingleLine();
            sale.setText("Rs "+reports.get(i).getSales());
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            sale.setGravity(Gravity.CENTER);
            sale.setTextSize(18);
            sale.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sale.setWidth(40);
            sale.setPadding(10, 10, 10, 10);
            sale.setBackgroundResource(R.drawable.table_row);


            t3.addView(sale);

            TextView comm = new TextView(getActivity());
            comm.setMaxLines(1);
            comm.setSingleLine();
            comm.setText("Rs " + reports.get(i).getCommission());
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            comm.setGravity(Gravity.CENTER);
            comm.setTextSize(18);
            comm.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            comm.setWidth(40);
            comm.setPadding(10, 10, 10, 10);
            comm.setBackgroundResource(R.drawable.table_row);


            t3.addView(comm);


            tableLayout.addView(t3);
        }
        // mProgressBar.dismiss();
    }


    private void createReport() {
        for (int i = 0; i < 3; i++) {
            TableRow tableRow = new TableRow(getActivity());

            //   tableRow.setWeightSum(4);
            tableRow.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView serial = new TextView(getActivity());
            serial.setText("1");
//            serial.setTextSize(18);

            // serial.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));


            serial.setGravity(Gravity.CENTER);
            serial.setBackgroundResource(R.drawable.table_row);
            tableRow.addView(serial);

            TextView shopName = new TextView(getActivity());

            shopName.setText("Imtiaz");

            shopName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            shopName.setGravity(Gravity.CENTER);
            shopName.setBackgroundResource(R.drawable.table_row);
            tableRow.addView(shopName);

            TextView totalOrders = new TextView(getActivity());

            totalOrders.setText("20");
            //   totalOrders.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            totalOrders.setGravity(Gravity.CENTER);
            totalOrders.setBackgroundResource(R.drawable.table_row);
            tableRow.addView(totalOrders);

            TextView totalSales = new TextView(getActivity());

            totalSales.setText("20,000");
            //   totalSales.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));

            totalSales.setGravity(Gravity.CENTER);
            totalSales.setBackgroundResource(R.drawable.table_row);
            tableRow.addView(totalSales);


            tableLayout.addView(tableRow);

        }

    }


    @Override
    public void onClick(View v) {

        if (v == startCalender || v == startDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (v == endCalender || v == endDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (v == viewReport) {
            if (startDate.getText().toString().trim().equalsIgnoreCase("Start Date") || endDate.getText().toString().trim().equalsIgnoreCase("End Date")) {

                Toast.makeText(getActivity(), "Please Select Dates", Toast.LENGTH_LONG).show();

            } else {

                tableLayout.removeAllViews();
                tableLayout.setVisibility(View.VISIBLE);

                Report report = new Report();
                report.setStartDate(startDate.getText().toString().trim());
                report.setEndDate(endDate.getText().toString().trim());
                getSalesReport(report);

            }
        }
    }

    public void getSalesReport(final Report report) {

        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Report>> apiCall = webAPIs.getSalesReport(report);
        apiCall.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful()) {

                    if (response.body().get(0).getOrders().equalsIgnoreCase("0")) {
                        Toast.makeText(getActivity(), "No Orders Delivered", Toast.LENGTH_SHORT).show();
                    } else {

                        loadData(response.body());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                Toast.makeText(getActivity(), "Internet Problem! Please Try Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}



