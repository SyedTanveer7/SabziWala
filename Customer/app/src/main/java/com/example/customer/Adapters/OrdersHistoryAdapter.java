package com.example.customer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.Fragments.OrderDetailsFragment;
import com.example.customer.Fragments.ProfileFragment;
import com.example.customer.R;
import com.example.customer.Webservices.Models.RecentOrder;

import java.util.List;

import static java.security.AccessController.getContext;


public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder> {

    private List<RecentOrder> items;
    private Context context;
    public static String selectedOrderId;

    public OrdersHistoryAdapter(List<RecentOrder> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history_item, viewGroup, false);
        return new OrdersHistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RecentOrder order = items.get(i);
        viewHolder.name.setText(order.getShopName());
        viewHolder.totalProducts.setText(order.getTotalProducts());
        viewHolder.totalBill.setText("Rs " + order.getTotalBill() + " /-");
        viewHolder.date.setText(order.getDate());
        viewHolder.status.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, totalProducts, totalBill, date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView26);
            totalProducts = itemView.findViewById(R.id.textView28);
            totalBill = itemView.findViewById(R.id.textView30);
            date = itemView.findViewById(R.id.textView31);
            status = itemView.findViewById(R.id.textView32);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String position = items.get(getLayoutPosition()).getOrderId();
                    selectedOrderId = position;
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new OrderDetailsFragment())
                            .commit();
                }
            });


        }
    }


}
