package com.neu.edu.numad17s_finalproject_ab_vs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.MainActivity;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Sketch;

import java.util.ArrayList;


/**
 * Created by vaibhavshukla on 3/3/17.
 */

public class SketchAdapter extends BaseAdapter {

    private ArrayList<Sketch> mGames;
    private Context mContext;

    public ArrayList<Sketch> getmGames() {
        return mGames;
    }

    public void setmGames(ArrayList<Sketch> mGames) {
        this.mGames = mGames;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public Object getItem(int i) {
        return mGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public SketchAdapter(Context mContext, ArrayList<Sketch> mUsers) {
        this.mContext = mContext;
        this.mGames = mUsers;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.user,viewGroup,false);
        TextView userName = (TextView) v.findViewById(R.id.user_name);
        Button send = (Button) v.findViewById(R.id.send_button);
        userName.setText(""+(i+1)+" " + mGames.get(i).getUser1());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGameJoined(FirebaseDatabase.getInstance().getReference(), mGames.get(i).getSketchID());
                Constants.SketchID = mGames.get(i).getSketchID();


            }
        });

        return v;
    }


    private void onGameJoined(DatabaseReference postRef, final String gameId) {
        postRef
                .child("Sketches")
                .child(gameId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Sketch g = mutableData.getValue(Sketch.class);
                        if (g == null) {
                            return Transaction.success(mutableData);
                        }

                        g.setJoined(true);
                        g.setUser2(Utils.getUserName(getmContext()));
                        mutableData.setValue(g);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Router.getInstance().showSketchGuesserFragment();
                    }
                });
    }
}
