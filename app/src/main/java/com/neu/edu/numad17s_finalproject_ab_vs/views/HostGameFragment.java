package com.neu.edu.numad17s_finalproject_ab_vs.views;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.MainActivity;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Sketch;


/**
 * Created by vaibhavshukla on 3/16/17.
 */

public class HostGameFragment extends Fragment {

    private DatabaseReference mDatabase;
    ProgressDialog mProgressDialog;
    Sketch mSketch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_game_fragment, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Waiting for other player to join...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        //adding game logic code here


       mSketch = new Sketch();
        mSketch.setHosted(true);
        mSketch.setUser1(Utils.getUserName(getContext()));
        mSketch.setSketchID(mDatabase.child("Sketches").push().getKey());
        Constants.SketchID=mSketch.getSketchID();
        mDatabase.child("Sketches").child(mSketch.getSketchID()).setValue(mSketch);
        waitForOtherPlayer();
        return view;
    }


    public void waitForOtherPlayer() {
        mDatabase.child("Sketches").child(mSketch.getSketchID()).child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean joined = dataSnapshot.getValue(Boolean.class);
                if (joined!=null && joined) {
                    Toast.makeText(getActivity(), "Player Joined", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    onGameJoined(mDatabase,mSketch.getSketchID());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }




    private void onGameJoined(DatabaseReference postRef, final String sketchId) {
        postRef
                .child("Sketches")
                .child(sketchId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Sketch sketch = mutableData.getValue(Sketch.class);
                        if (sketch == null) {
                            return Transaction.success(mutableData);
                        }

                        sketch.setHosted(false);
                        sketch.setJoined(false);
                        mutableData.setValue(sketch);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Constants.SketchID=sketchId;
                        Router.getInstance().showSketcherFragment();
                    }
                });
    }


}
