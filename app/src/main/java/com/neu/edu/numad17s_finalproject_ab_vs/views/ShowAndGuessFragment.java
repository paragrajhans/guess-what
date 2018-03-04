package com.neu.edu.numad17s_finalproject_ab_vs.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neu.edu.numad17s_finalproject_ab_vs.R;


/**
 * Created by ashish on 12/01/17.
 * This fragment will present the image to friends drawn by user and will let the friends to guess it.
 * A maximum of three guess are allowed
 */

public class ShowAndGuessFragment extends Fragment {

    private View rootView;


    public static ShowAndGuessFragment newInstance() {

        Bundle args = new Bundle();
        ShowAndGuessFragment fragment = new ShowAndGuessFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {

            //TODO layout pending . Mocked by HomeFragment
            rootView = inflater.inflate(R.layout.homefragment,container,false);


        }

        return rootView;
    }





}
