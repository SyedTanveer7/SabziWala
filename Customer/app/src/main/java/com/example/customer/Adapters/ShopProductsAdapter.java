package com.example.customer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.R;
import com.example.customer.Webservices.Models.OrderedProducts;
import com.example.customer.Webservices.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.customer.Fragments.HomeFragment.shopProductss;

public class ShopProductsAdapter extends RecyclerView.Adapter<ShopProductsAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;
    public static List<Product> selectedProducts = new ArrayList<>();

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
        viewHolder.productPrice.setText("Rs " + item.getPrice() + " /-");
        if (!item.getImage().equalsIgnoreCase("")) {
            Picasso.get().load(item.getImage()).into(viewHolder.productImage);
        }
        Log.i("Item", item.isSelected() + "");
        if (item.isSelected()) {
            viewHolder.addToCart.setText("Added");
            viewHolder.addToCart.setEnabled(false);
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
        public Button addToCart;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView_product);
            productName = itemView.findViewById(R.id.textView_title);
            productPrice = itemView.findViewById(R.id.textView_price);
            addToCart = itemView.findViewById(R.id.button3);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getLayoutPosition();
                    Product product = items.get(position);
                    product.setQuantity("1");
                    product.setSelected(true);
                    selectedProducts.add(product);
                    Toast.makeText(context, items.get(position).getName() + " added to Cart", Toast.LENGTH_SHORT).show();
                    addToCart.setText("Added");
                    addToCart.setEnabled(false);
                }
            });


        }
    }


}
