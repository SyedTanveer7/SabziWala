package com.example.vendor.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Fragments.OrderDetailsFragment;
import com.example.vendor.Fragments.OrdersFragment;
import com.example.vendor.InvoiceActivity;
import com.example.vendor.MainActivity;
import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Invoice;
import com.example.vendor.Webservices.Models.Order;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.toolbarTitle;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{


    private List<Order> items;
    private Context context;
    public static Order selectedOrder;

    public OrdersAdapter(List<Order> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order item = items.get(i);
        viewHolder.customerName.setText(item.getCustomerName());
        viewHolder.date.setText(item.getDate());
        viewHolder.time.setText(item.getTime());
        viewHolder.status.setText(item.getStatus());
        viewHolder.bill.setText("Rs " + item.getTotalBill()+" /-");
        viewHolder.address.setText(item.getCustomerAddress());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName;
        public TextView date;
        public TextView time;
        public TextView bill;
        public TextView status;
        public TextView address;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.text_customer_name);
            date = itemView.findViewById(R.id.textView_date);
            bill = itemView.findViewById(R.id.textView_bill);
            time = itemView.findViewById(R.id.textView_time);
            status = itemView.findViewById(R.id.textView_status);
            address = itemView.findViewById(R.id.textView_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedOrder = items.get(getLayoutPosition());
//                   ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
//                            .replace(R.id.main_content, new OrderDetailsFragment())
//                            .commit();
                    itemView.getContext().startActivity(new Intent(context, InvoiceActivity.class));
                    ((FragmentActivity) itemView.getContext()).finish();
                    toolbarTitle.setText(R.string.title_order_invoice);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = getLayoutPosition();

                    new AlertDialog.Builder(context)
                            .setTitle("Change State")
                            .setItems(R.array.Options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        selectedOrder = items.get(position);
                                        Toast.makeText(context,selectedOrder.getId()+"",Toast.LENGTH_SHORT).show(); selectedOrder.setStatus("Pending");
                                        updateOrderState(itemView,selectedOrder);

                                    } else if (which == 1) {
                                        selectedOrder = items.get(position);
                                        selectedOrder.setStatus("In Progress");
                                        updateOrderState(itemView,selectedOrder);
                                    } else if (which == 2) {
                                        selectedOrder = items.get(position);
                                        selectedOrder.setStatus("Delivered");
                                        updateOrderState(itemView,selectedOrder);
                                    }

                                }
                            })

                            .show();
                    return true;
                }
            });




            //j




        }
    }


    public void updateOrderState(final View itemView, Order order) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Order> apiCall = webAPIs.updateOrderState(order);
        apiCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){

                    itemView.getContext().startActivity(new Intent(context, MainActivity.class));
                    ((FragmentActivity) itemView.getContext()).finish();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });

    }


        }
