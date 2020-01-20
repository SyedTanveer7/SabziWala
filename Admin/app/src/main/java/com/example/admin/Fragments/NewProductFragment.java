package com.example.admin.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
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
import com.example.admin.Webservices.Repositories.VendorRepository;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductFragment extends Fragment implements View.OnClickListener {

    private ImageView imageBack, imageUploaded;
    private Button buttonAddProduct, buttonUpload;
    private EditText productName, productPrice;
    private Uri productImageURI;
    private Spinner spinnerVendors;
    String encodedImage = "";
    ProgressDialog dialog;
    ProductRepository repository;
    VendorRepository vendorRepository;
    public static ArrayList<String> shops = new ArrayList<>();
    String imagePath = "";
    public static String selectedShop = "";
    public static int selectedShopPosition;
    private WebAPIs webAPIs;
    View v;


    public NewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_product, container, false);
        initializeEvents(v);
        dialog = new ProgressDialog(getActivity());
        return v;
    }

    public void initializeEvents(View v) {


        repository = new ProductRepository(requireActivity());

        buttonAddProduct = v.findViewById(R.id.button_add_product);
        imageBack = v.findViewById(R.id.image_back);

        imageUploaded = v.findViewById(R.id.imageView5);
        buttonUpload = v.findViewById(R.id.button4);

        productName = v.findViewById(R.id.editText_product_name);
        spinnerVendors = v.findViewById(R.id.spinner);
        productPrice = v.findViewById(R.id.editText_product_price);

        imageBack.setOnClickListener(this);
        buttonAddProduct.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        ArrayAdapter<String> spinner2Adapter = new ArrayAdapter<String>(getActivity(), R.layout.sample_spinner_item, shops);
        spinnerVendors.setAdapter(spinner2Adapter);

        spinnerVendors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShop = parent.getItemAtPosition(position).toString();
                selectedShopPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonUpload.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonUpload.setBackgroundResource(R.drawable.btn_shape);
                    buttonUpload.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonUpload.setBackgroundResource(R.drawable.btn_selected);
                    buttonUpload.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


        buttonAddProduct.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonAddProduct.setBackgroundResource(R.drawable.btn_shape);
                    buttonAddProduct.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonAddProduct.setBackgroundResource(R.drawable.btn_selected);
                    buttonAddProduct.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == imageBack) {
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new ProductsFragment())
                    .commit();
            toolbarTitle.setText(R.string.title_products);
        } else if (v == buttonAddProduct) {
            validateProduct();
        } else if (v == buttonUpload) {
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
            product.setName(name);
            product.setPrice(price);
            product.setShopName(selectedShop);


            //encode image to base64 string
            if (!(imagePath.equalsIgnoreCase(""))) {


//                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
                product.setImage(encodedImage);
                addProductInServer(product);
                imagePath = "";
                productName.setText("");
                productPrice.setText("");
                imageUploaded.setImageDrawable(null);


            } else {
                Toast.makeText(getActivity(), "Please Select Product Image", Toast.LENGTH_SHORT).show();
            }


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
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"), 301);

        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int dataSize = 0;
            if (requestCode == 301) {

                try {
                    productImageURI = data.getData();

                    imagePath = productImageURI.getPath();
                    InputStream fileInputStream = getActivity().getContentResolver().openInputStream(productImageURI);
                    dataSize = fileInputStream.available() / 1024;
                    //  dataSize = dataSize/1024;
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                if(dataSize>1050){
//
//                    imageUploaded.setImageDrawable(null);
//
//                    Toast.makeText(getActivity(), "Select image size upto 1 MB", Toast.LENGTH_SHORT).show();
//
//                }else {
                // imageUploaded.setImageURI(productImageURI);
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
//                }

            }
        }
    }


    public void addProductInServer(Product product) {
        dialog.setMessage("Please Wait");
        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Product> apiCall = webAPIs.addProduct(product);

        apiCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Product Added Successfully!", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();


                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                dialog.dismiss();
            }
        });
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


}
