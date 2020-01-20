package com.example.customer.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customer.R;

import static com.example.customer.Fragments.LoginFragment.loggedinUser;
import static com.example.customer.MainActivity.toolbarTitle;


public class ProfileFragment extends Fragment {

    private Button editButton;
    private ImageView imageBack;
    private TextView userName;
    private TextView name;
    private TextView password;
    private TextView contact;
    private TextView address;
    private ProgressDialog dialog;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initialzieEvents(view);
        return view;
    }

    private void initialzieEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        editButton = v.findViewById(R.id.button_edit);
        imageBack = v.findViewById(R.id.image_back);
        name = v.findViewById(R.id.textView);
        userName = v.findViewById(R.id.textView3);
        password = v.findViewById(R.id.textView6);
        contact = v.findViewById(R.id.textView7);
        address = v.findViewById(R.id.textView9);


        name.setText(loggedinUser.getName());
        userName.setText(loggedinUser.getUsername());
        password.setText(loggedinUser.getPassword());
        contact.setText(loggedinUser.getMobile());
        address.setText(loggedinUser.getAddress());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new UpdateProfileFragment());
                toolbarTitle.setText(R.string.edit_Profile);
            }
        });


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_content, new SettingsFragment())
                        .commit();
                toolbarTitle.setText(R.string.my_account);
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



}
