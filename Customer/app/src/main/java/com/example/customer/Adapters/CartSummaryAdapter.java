package com.example.customer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.R;
import com.example.customer.Webservices.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;
import static com.example.customer.Fragments.CartSummaryFragment.bill;
import static com.example.customer.Fragments.CartSummaryFragment.cartEmptyText;
import static com.example.customer.Fragments.CartSummaryFragment.productItemsSize;
import static com.example.customer.Fragments.CartSummaryFragment.productsQty;
import static com.example.customer.Fragments.CartSummaryFragment.totalBill;

public class CartSummaryAdapter extends RecyclerView.Adapter<CartSummaryAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;

    public CartSummaryAdapter(List<Product> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CartSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
        return new CartSummaryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product item = items.get(i);
        viewHolder.productName.setText(item.getName());
        viewHolder.productPrice.setText("Rs " + item.getPrice() + " /-");
        if (!item.getImage().equalsIgnoreCase("")) {
            Picasso.get().load(item.getImage()).into(viewHolder.productImage);
        }

        viewHolder.shopName.setText(item.getShopName());
        if (item.getQuantity() == null) {
            item.setQuantity("1");
        }
        viewHolder.productQty.setText(item.getQuantity());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName, shopName, productPrice, productQty;
        public ImageView addQty, minusQty, productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView2);
            productName = itemView.findViewById(R.id.textView);
            productPrice = itemView.findViewById(R.id.textView2);
            productQty = itemView.findViewById(R.id.textView4);
            shopName = itemView.findViewById(R.id.textView3);
            addQty = itemView.findViewById(R.id.imageView3);
            minusQty = itemView.findViewById(R.id.imageView4);

            addQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getLayoutPosition();
                    Product product = items.get(position);

                    int qty = Integer.parseInt(product.getQuantity());
                    qty = qty + 1;
                    if (qty == 16) {
                        Toast.makeText(context, "Limit Exceeds: maximum quanity 15", Toast.LENGTH_SHORT).show();
                    } else {
                        product.setQuantity(String.valueOf(qty));
                        bill = bill + Double.valueOf(product.getPrice());
                        totalBill.setText("Rs " + bill + "/-");
                        notifyDataSetChanged();
                    }

                }
            });


            minusQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getLayoutPosition();
                    Product product = items.get(position);
                    int qty = Integer.parseInt(product.getQuantity());
                    qty = qty - 1;
                    if (qty == 0) {
                        product.setQuantity(String.valueOf(qty));
                        items.remove(position);
                        productItemsSize--;
                        if (productItemsSize == 0) {
                            cartEmptyText.setVisibility(View.VISIBLE);
                            bill = 0.0;
                        } else {
                            productsQty.setText(productItemsSize + "");
                            bill = bill - Double.valueOf(product.getPrice());
                            totalBill.setText("Rs " + bill + "/-");
                        }
                        notifyDataSetChanged();

                    } else {
                        product.setQuantity(String.valueOf(qty));
                        bill = bill - Double.valueOf(product.getPrice());
                        totalBill.setText("Rs " + bill + "/-");
                        notifyDataSetChanged();
                    }
                }
            });


        }
    }

}
