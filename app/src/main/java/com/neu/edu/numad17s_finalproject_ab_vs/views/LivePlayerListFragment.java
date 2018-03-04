package com.neu.edu.numad17s_finalproject_ab_vs.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;
import com.neu.edu.numad17s_finalproject_ab_vs.adapters.LivePlayerAdapter;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Player;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ashish on 11/04/17.
 * This fragment will present the list of friends to the user
 */

public class LivePlayerListFragment extends Fragment implements ValueEventListener {

    private View rootView;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private ArrayList<Player> players = new ArrayList<>();

    @OnClick(R.id.challenge_players)
    public void challengePlayers(){

        LivePlayerAdapter adapter = (LivePlayerAdapter)recyclerView.getAdapter();
        if(adapter!=null){

            Map<Integer,String> selectedPlayers = adapter.getSelectedPlayerMap();
            for(int key:selectedPlayers.keySet())
                sendRequest(selectedPlayers.get(key));
        }
        Router.getInstance().showShowAndGuessFragment();
    }

    public void sendRequest(String regId){
        Utils.sendPushNotification( "Challenge From : "+Utils.getUserName(getContext())+ ". Click to accept.",
                Utils.getRegId(getContext()),
                regId,
                Constants.ACCEPT_CHALLENGE);
    }

    public static LivePlayerListFragment newInstance() {

        Bundle args = new Bundle();
        LivePlayerListFragment fragment = new LivePlayerListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.live_player_list_fragment,container,false);
            ButterKnife.bind(this.rootView);

        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(this);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        players.clear();
        String myUsername = Utils.getUserName(getContext());
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Player player = postSnapshot.getValue(Player.class);
            if(player.status==1 && player.getName()!=null && !player.getName().equals(myUsername))
                this.players.add(player);

        }
        recyclerView.setAdapter(new LivePlayerAdapter(players));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
