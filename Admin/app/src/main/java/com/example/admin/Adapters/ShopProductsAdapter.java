package com.example.admin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopProductsAdapter extends RecyclerView.Adapter<ShopProductsAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;
    public static Product selectedProduct;

    public ShopProductsAdapter(List<Product> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_products_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product item = items.get(i);
        viewHolder.productName.setText(item.getName());
        viewHolder.productPrice.setText("Rs " + item.getPrice()+" /-");
        if (!item.getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(item.getImage()).into(viewHolder.productImage);
         //   Picasso.get().load(item.getImage()).into(viewHolder.productImage);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView_product);
            productName = itemView.findViewById(R.id.textView_title);
            productPrice = itemView.findViewById(R.id.textView_price);

        }
    }


}
