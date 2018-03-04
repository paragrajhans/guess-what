package com.neu.edu.numad17s_finalproject_ab_vs.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.MainActivity;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ashish on 12/01/17.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.play_game)
    TextView playGame;
    @OnClick(R.id.play_game)
    void openGameFragment(){

        Router.getInstance().showGameSelectionFragment();
    }

    @OnClick(R.id.quit_game)
    void quit(){
        MainActivity.instance.onBackPressed();
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.homefragment,container,false);
            ButterKnife.bind(this,rootView);
            imageView.setImageResource(R.drawable.ic_laptop);


        }

        return rootView;
    }








}
