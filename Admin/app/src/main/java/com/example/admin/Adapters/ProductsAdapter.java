package com.example.admin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.admin.Fragments.ProductsFragment;
import com.example.admin.Fragments.UpdateProductFragment;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Product;
import com.example.admin.Webservices.Repositories.ProductRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;
    public static Product selectedProduct;
    ProductRepository repository;

    public ProductsAdapter(List<Product> items, Context context) {
        this.items = items;
        this.context = context;
        repository = new ProductRepository(context);
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
        viewHolder.productPrice.setText("Rs " + item.getPrice());
        if (!item.getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(item.getImage()).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.productImage);
            //     Picasso.get().load(item.getImage()).into(viewHolder.productImage);
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


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = getLayoutPosition();

                    new AlertDialog.Builder(context)
                            .setTitle("Select Option")
                            .setItems(R.array.Options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (which == 0) {

                                        selectedProduct = items.get(position);
                                        ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                                .replace(R.id.main_content, new UpdateProductFragment())
                                                .commit();
                                    } else if (which == 1) {

                                        Product product = new Product();
                                        product.setId(items.get(position).getId());
                                        repository.deleteProductInServer(product);
                                    }

                                }
                            })

                            .show();


                    return true;
                }
            });

        }
    }


}



