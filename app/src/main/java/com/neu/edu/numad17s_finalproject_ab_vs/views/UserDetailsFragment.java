package com.neu.edu.numad17s_finalproject_ab_vs.views;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.MainActivity;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.model.User;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vaibhavshukla on 3/15/17.
 */

public class UserDetailsFragment extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment,container,false);

        final EditText email = (EditText) view.findViewById(R.id.emial_id);
        final EditText userName = (EditText) view.findViewById(R.id.userName);
        Button register = (Button) view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName!=null || !userName.getText().toString().equals("") || !userName.getText().toString().equals(" ") || email!=null || !email.getText().toString().equals("") || !email.getText().toString().equals(" "))

                {
                    User user = new User();
                    user.setActive(true);
                    user.setEmailId(email.getText().toString());
                    user.setGameId(0);
                    user.setName(userName.getText().toString());
                    user.setTokenId(FirebaseInstanceId.getInstance().getToken());
                    mDatabase.child("Users").child(mDatabase.child("Users").push().getKey()).setValue(user);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit();
                    editor.putString(Constants.PLAYER_NAME, user.getName());
                    editor.putString(Constants.PLAYER_EMAIL, user.getEmailId());
                    ((MainActivity)getActivity()).userName = user.getName();
                    ((MainActivity)getActivity()).email = user.getEmailId();
                    editor.commit();
                    Router.getInstance().showGameSelectionFragment();
                }
                else
                {
                    Toast.makeText(getActivity(),"Email or user name cannot be blank",Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }


}
