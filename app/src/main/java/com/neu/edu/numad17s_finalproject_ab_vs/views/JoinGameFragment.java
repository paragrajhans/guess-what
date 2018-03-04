package com.neu.edu.numad17s_finalproject_ab_vs.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.adapters.SketchAdapter;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Sketch;

import java.util.ArrayList;


/**
 * Created by vaibhavshukla on 3/16/17.
 */

public class JoinGameFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ListView mSketchView;
    private ArrayList<Sketch> Sketches = new ArrayList<Sketch>();
    private SketchAdapter mGamaesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_game_fragment, container, false);
        mSketchView = (ListView) view.findViewById(R.id.live_game_list);
        populateGames();
        return  view;
    }


    public void populateGames()
    {
        Sketches.clear();
        mDatabase.child("Sketches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnap  = dataSnapshot.getChildren();
                for (DataSnapshot key : dataSnap)
                {

                    if(key.getValue(Sketch.class).getHosted())
                        Sketches.add(key.getValue(Sketch.class));
                }
                mGamaesAdapter = new SketchAdapter(getActivity(),Sketches);
                mSketchView.setAdapter(mGamaesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
