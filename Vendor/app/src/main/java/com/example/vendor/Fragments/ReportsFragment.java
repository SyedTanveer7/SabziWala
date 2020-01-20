package com.example.vendor.Fragments;


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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Invoice;
import com.example.vendor.Webservices.Models.Report;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;

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
    private TextView shop, orders, sales, profit, comision;
    private TextView shopText, ordersText, salesText, profitText, comisionText;

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
        // tableLayout = view.findViewById(R.id.tableInvoices);

        shop = view.findViewById(R.id.textView39);
        sales = view.findViewById(R.id.textView43);
        orders = view.findViewById(R.id.textView41);
        comision = view.findViewById(R.id.textView45);
        profit = view.findViewById(R.id.textView47);

        shopText = view.findViewById(R.id.textView38);
        salesText = view.findViewById(R.id.textView42);
        ordersText = view.findViewById(R.id.textView40);
        comisionText = view.findViewById(R.id.textView44);
        profitText = view.findViewById(R.id.textView46);

        startCalender = view.findViewById(R.id.imageView10);
        startDate = view.findViewById(R.id.textView14);
        endCalender = view.findViewById(R.id.imageView11);
        endDate = view.findViewById(R.id.textView16);
        viewReport = view.findViewById(R.id.button);
     //   tableLayout.setStretchAllColumns(true);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startCalender.setOnClickListener(this);
        endCalender.setOnClickListener(this);
        viewReport.setOnClickListener(this);

        shopText.setVisibility(View.GONE);
        salesText.setVisibility(View.GONE);
        ordersText.setVisibility(View.GONE);
        profitText.setVisibility(View.GONE);
        comisionText.setVisibility(View.GONE);


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
           //     tableLayout.removeAllViews();
            //    tableLayout.setVisibility(View.VISIBLE);
                Report report = new Report();
                report.setStartDate(startDate.getText().toString().trim());
                report.setEndDate(endDate.getText().toString().trim());
                report.setShopId(vendor.getShopId());
                getSalesReport(report);
            }
        }
    }


    public void loadData(List<Report> reports) {


        TableRow t1 = new TableRow(getActivity());
        t1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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


        TextView price = new TextView(getActivity());
        price.setMaxLines(1);
        price.setSingleLine();
        price.setText("Orders");
        price.setTypeface(null, Typeface.BOLD);
        price.setGravity(Gravity.CENTER);
        price.setTextSize(18);
        price.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        price.setWidth(90);
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
        sales.setWidth(90);
        sales.setPadding(10, 10, 10, 10);
        sales.setBackgroundResource(R.drawable.table_row);

        t1.addView(sales);

        TextView comission = new TextView(getActivity());
        comission.setMaxLines(1);
        comission.setSingleLine();
        comission.setText("Comision");
        comission.setTypeface(null, Typeface.BOLD);
        comission.setGravity(Gravity.CENTER);
        comission.setTextSize(18);
        comission.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        comission.setWidth(90);
        comission.setPadding(10, 10, 10, 10);
        comission.setBackgroundResource(R.drawable.table_row);

        t1.addView(comission);

        TextView productName = new TextView(getActivity());
        productName.setMaxLines(1);
        productName.setSingleLine();
        productName.setTypeface(null, Typeface.BOLD);
        productName.setText("Profit");
        productName.setGravity(Gravity.CENTER);
        productName.setTextSize(18);
        productName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        productName.setWidth(90);
        productName.setPadding(10, 10, 10, 10);
        productName.setBackgroundResource(R.drawable.table_row);

        t1.addView(productName);


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


            TextView order = new TextView(getActivity());
            order.setMaxLines(1);
            order.setSingleLine();
            order.setText(reports.get(i).getOrders());
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            order.setGravity(Gravity.CENTER);
            order.setTextSize(18);
            order.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            order.setWidth(90);
            order.setPadding(10, 10, 10, 10);
            order.setBackgroundResource(R.drawable.table_row);


            t3.addView(order);

            TextView sale = new TextView(getActivity());
            sale.setMaxLines(1);
            sale.setSingleLine();
            sale.setText("Rs " + reports.get(i).getSales());
            //   pric.setText("Rs " + productsInvoice.get(i).getPrice() + "/-");
            sale.setGravity(Gravity.CENTER);
            sale.setTextSize(18);
            sale.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sale.setWidth(90);
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
            comm.setWidth(90);
            comm.setPadding(10, 10, 10, 10);
            comm.setBackgroundResource(R.drawable.table_row);


            t3.addView(comm);

            TextView name = new TextView(getActivity());
            name.setMaxLines(1);
            name.setSingleLine();
            name.setText("Rs " + reports.get(i).getProfit());
            //    name.setText(productsInvoice.get(i).getName());
            name.setGravity(Gravity.CENTER);
            name.setTextSize(18);
            name.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            name.setWidth(90);
            name.setPadding(10, 10, 10, 10);
            name.setBackgroundResource(R.drawable.table_row);

            t3.addView(name);


            tableLayout.addView(t3);
        }
        // mProgressBar.dismiss();
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

                        // loadData(response.body());

                        shopText.setVisibility(View.VISIBLE);
                        salesText.setVisibility(View.VISIBLE);
                        ordersText.setVisibility(View.VISIBLE);
                        profitText.setVisibility(View.VISIBLE);
                        comisionText.setVisibility(View.VISIBLE);

                        shop.setText(vendor.getShopName());

                        sales.setText("Rs "+response.body().get(0).getSales());
                        orders.setText(response.body().get(0).getOrders());
                        profit.setText("Rs "+response.body().get(0).getProfit());
                        comision.setText("Rs "+response.body().get(0).getCommission());

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
