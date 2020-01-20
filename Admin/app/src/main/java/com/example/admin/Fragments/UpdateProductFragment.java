package com.example.admin.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.Webservices.Models.Product;
import com.example.admin.Webservices.Repositories.ProductRepository;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.admin.Adapters.ProductsAdapter.selectedProduct;
import static com.example.admin.Fragments.NewProductFragment.scaleDown;
import static com.example.admin.Fragments.NewProductFragment.selectedShop;
import static com.example.admin.Fragments.NewProductFragment.selectedShopPosition;
import static com.example.admin.Fragments.NewProductFragment.shops;
import static com.example.admin.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProductFragment extends Fragment implements View.OnClickListener {

    private ImageView imageBack, imageUploaded;
    private Button buttonUpdateProduct, buttonUploadImage;
    private EditText productName, productPrice;
    private Uri productImageURI;
    ProductRepository repository;
    String imagePath = "";
    String encodedImage = "";
    private Spinner spinnerVendors;
    int selectedShopID;


    public UpdateProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_product, container, false);
        initializeEvents(v);

        return v;
    }

    public void initializeEvents(View v) {

        repository = new ProductRepository(getActivity());

        buttonUpdateProduct = v.findViewById(R.id.button_update_product);
        imageBack = v.findViewById(R.id.image_back);

        imageUploaded = v.findViewById(R.id.product_image);
        buttonUploadImage = v.findViewById(R.id.uploadImage);

        productName = v.findViewById(R.id.update_product_name);
        spinnerVendors = v.findViewById(R.id.spinner);
        productPrice = v.findViewById(R.id.update_product_price);


        imageBack.setOnClickListener(this);
        buttonUpdateProduct.setOnClickListener(this);
        buttonUploadImage.setOnClickListener(this);


        productName.setText(selectedProduct.getName());
        productPrice.setText(selectedProduct.getPrice());
        spinnerVendors.setSelection(selectedShopPosition);


        if (!selectedProduct.getImage().equalsIgnoreCase("")) {
            Picasso.get().load(selectedProduct.getImage()).into(imageUploaded);

        }


        ArrayAdapter<String> spinner2Adapter = new ArrayAdapter<String>(getActivity(), R.layout.sample_spinner_item, shops);


        spinnerVendors.setAdapter(spinner2Adapter);
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).equalsIgnoreCase(selectedProduct.getShopName())) {
                selectedShopID = i;
            }
        }

        spinnerVendors.setSelection(selectedShopID);
        spinnerVendors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShop = parent.getItemAtPosition(position).toString();
                selectedShopPosition = position;

                //      Toast.makeText(getApplicationContext(),priorityName, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonUploadImage.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonUploadImage.setBackgroundResource(R.drawable.btn_shape);
                    buttonUploadImage.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonUploadImage.setBackgroundResource(R.drawable.btn_selected);
                    buttonUploadImage.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


        buttonUpdateProduct.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonUpdateProduct.setBackgroundResource(R.drawable.btn_shape);
                    buttonUpdateProduct.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonUpdateProduct.setBackgroundResource(R.drawable.btn_selected);
                    buttonUpdateProduct.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }

    @Override
    public void onClick(View v) {
        if (v == imageBack) {
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new ProductsFragment())
                    .commit();
            toolbarTitle.setText(R.string.title_products);
        } else if (v == buttonUpdateProduct) {
            validateProduct();
        } else if (v == buttonUploadImage) {
            uploadImage();
        }

    }

    public void validateProduct() {

        String name = productName.getText().toString().trim();
        String price = productPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            productName.setError("Product Name required!");
        } else if (TextUtils.isEmpty(price)) {
            productPrice.setError("Product Price required!");
        } else if (selectedShop.equalsIgnoreCase("") || selectedShop.equalsIgnoreCase("Select Shop")) {
            Toast.makeText(getActivity(), "Please Select Product Shop!", Toast.LENGTH_LONG).show();

        } else {

            Product product = new Product();
            product.setId(selectedProduct.getId());
            product.setName(name);
            product.setPrice(price);
            product.setShopName(selectedShop);


            //encode image to base64 strin
            if (!imagePath.equalsIgnoreCase("")) {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), productImageURI);
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                product.setImage(encodedImage);

            } else {
                product.setImage(selectedProduct.getImage());
            }

            repository.updateProductInServer(product);
            imagePath = "";
            productName.setText("");
            productPrice.setText("");
            spinnerVendors.setSelection(0);
            imageUploaded.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));


        }


    }

    public void uploadImage() {


        if (!checkIfAlreadyhavePermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"), 305);

        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 305) {
                productImageURI = data.getData();
                imageUploaded.setImageURI(productImageURI);
                imagePath = productImageURI.getPath();

                Bitmap bitmap1 = null;
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), productImageURI);
                    Bitmap scale = scaleDown(bitmap1, 400, true);
                    imageUploaded.setImageBitmap(scale);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scale.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }


    }


}


