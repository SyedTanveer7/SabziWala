package com.example.admin.Adapters;

import android.app.Activity;
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

import com.example.admin.Fragments.ShopProductsFragment;
import com.example.admin.Fragments.UpdateVendorFragment;
import com.example.admin.Fragments.VendorsFragment;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Vendor;
import com.example.admin.Webservices.Repositories.VendorRepository;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.admin.MainActivity.toolbarTitle;

public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.ViewHolder> {

    private List<Vendor> items;

    private Context context;
    public static Vendor selectedVendor;
    VendorRepository repository;

    public VendorsAdapter(List<Vendor> items, Context context) {
        this.items = items;
        this.context = context;
        repository = new VendorRepository(context);
    }

    @NonNull
    @Override
    public VendorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendors_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsAdapter.ViewHolder viewHolder, int i) {
        Vendor item = items.get(i);
        viewHolder.shopName.setText(item.getShopName());
        viewHolder.vendorName.setText(item.getVendorName());
        viewHolder.location.setText(item.getLocation());
        if (!item.getImage().equalsIgnoreCase("")) {

            Picasso.get().load(item.getImage()).into(viewHolder.vendorImage);
        }
        viewHolder.comission.setText(item.getComission() + " %");
        viewHolder.password.setText(item.getPassword());
        viewHolder.mobile.setText(item.getMobile());
        //    viewHolder.vendorImage.setText(item.getShopName());
        //  holder.image.setImageBitmap(null);
        //  Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        // Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vendorImage;
        public TextView shopName;
        public TextView vendorName;
        public TextView location;
        public TextView comission;
        public TextView password;
        public TextView mobile;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            vendorImage = itemView.findViewById(R.id.imageView2);
            shopName = itemView.findViewById(R.id.textView42);
            vendorName = itemView.findViewById(R.id.textView44);
            location = itemView.findViewById(R.id.textView23);
            comission = itemView.findViewById(R.id.textView46);
            mobile = itemView.findViewById(R.id.textView50);
            password = itemView.findViewById(R.id.textView48);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedVendor = items.get(getLayoutPosition());
                    ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new ShopProductsFragment())
                            .commit();

                    toolbarTitle.setText(R.string.title_shop_products);
                }
            });

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
                                        selectedVendor = items.get(position);
                                        ((FragmentActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                                .replace(R.id.main_content, new UpdateVendorFragment())
                                                .commit();
                                    } else if (which == 1) {
                                        Vendor vendor = new Vendor();
                                        vendor.setShopId(items.get(position).getShopId());
                                        items.get(position).getVendorName();

                                        repository.deleteVendorInServer(vendor, items, position);
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
