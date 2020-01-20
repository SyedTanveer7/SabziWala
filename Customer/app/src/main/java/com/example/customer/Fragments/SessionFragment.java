package com.example.customer.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.customer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionFragment extends Fragment {

    Button signIn, register;

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {

        signIn = view.findViewById(R.id.button6);
        register = view.findViewById(R.id.button7);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginFragment());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SignUpFragment());
            }
        });


        signIn.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    signIn.setBackgroundResource(R.drawable.cart_shape_style);
                    signIn.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signIn.setBackgroundResource(R.drawable.cart_shape_style_unselected);
                    signIn.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }

        });

        register.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    register.setBackgroundResource(R.drawable.cart_shape_style);
                    register.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    register.setBackgroundResource(R.drawable.cart_shape_style_unselected);
                    register.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }

        });



    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.session_content, fragment);
        fragmentTransaction.commit();
    }


}
