package com.neu.edu.numad17s_finalproject_ab_vs.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by vaibhavshukla on 3/16/17.
 */

public class OnlineGameFragment extends Fragment {


    @OnClick(R.id.host_game)
    void HostGame(){
        Router.getInstance().showHostFragment();
    }

    @OnClick(R.id.join_game)
    void JoinGame(){
        Router.getInstance().showJoinFragment();
    }

    @BindView(R.id.host_game_IV)
    ImageView host_game_imageview;

    @BindView(R.id.join_game_IV)
    ImageView join_game_IV;

    @BindView(R.id.player_name)
    TextView player_name;

    View rootview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootview == null){

            rootview = inflater.inflate(R.layout.chose_game_fragment,container,false);
            ButterKnife.bind(this,rootview);
            player_name.setText("Welcome, "+ Utils.getUserName(getContext()));
            host_game_imageview.setImageResource(R.drawable.play);
            join_game_IV.setImageResource(R.drawable.link);

        }


        return rootview;
    }
}
