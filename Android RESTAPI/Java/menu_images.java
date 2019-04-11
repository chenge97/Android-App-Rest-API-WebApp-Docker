package com.example.chenge.app_rest;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class menu_images extends Fragment {


    public menu_images() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_images, container, false);
        Button add_image_btn = view.findViewById(R.id.btn_add_image);
        Button delete_image_btn = view.findViewById(R.id.btn_delete_image);

        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("Add Image");
                add_image img = new add_image();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,img).commit();
            }
        });
        delete_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("Delete Image");
                delete_image del_img = new delete_image();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,del_img).commit();
            }
        });
        return view;


    }

}
