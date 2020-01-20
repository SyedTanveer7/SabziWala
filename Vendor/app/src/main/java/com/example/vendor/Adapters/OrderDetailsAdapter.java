package com.example.vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;

    public OrderDetailsAdapter(List<Product> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_detail_item, viewGroup, false);
        return new OrderDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product item = items.get(i);
        viewHolder.productName.setText(item.getName());
        viewHolder.productPrice.setText("Rs " + item.getPrice() + " /-");
        if (!item.getImage().equalsIgnoreCase("")) {

            Glide.with(context).load(item.getImage()).into(viewHolder.productImage);
        //    Picasso.get().load(item.getImage()).into(viewHolder.productImage);
        }
       viewHolder.productQuantity.setText(item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView productQuantity;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView2);
            productName = itemView.findViewById(R.id.textView);
            productPrice = itemView.findViewById(R.id.textView2);
            productQuantity = itemView.findViewById(R.id.textView4);


        }
    }

}
