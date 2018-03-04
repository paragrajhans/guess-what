package com.neu.edu.numad17s_finalproject_ab_vs.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.neu.edu.numad17s_finalproject_ab_vs.MainActivity;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.Router;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ashish on 12/01/17.
 */
public class CreateAccountFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private View rootView;

    @BindView(R.id.name)
    EditText username;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.btn_sign_in)
    SignInButton btnSignIn;
    @BindView(R.id.btn_logout_from_google)
    Button btnSignOut;


    GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private ProgressDialog mProgressDialog;

    public static CreateAccountFragment newInstance() {

        Bundle args = new Bundle();
        CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.create_account, container, false);
            ButterKnife.bind(this, rootView);
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            btnSignIn.setOnClickListener(this);
            btnSignOut.setOnClickListener(this);
        }

        return rootView;
    }

    @OnClick(R.id.start_game)
    void startGame() {
        String name = username.getText().toString();
        String user_email = email.getText().toString();
        saveCredentials(name, user_email);

    }

    private void saveCredentials(String name, String user_email) {
        if (verifyCredentials(name, user_email)) {
            Utils.saveUserName(name, getContext());
            Utils.saveUserEmail(user_email, getContext());
            Router.getInstance().showHome();
        } else
            Toast.makeText(getContext(), R.string.INVALID_CREDENTIALS, Toast.LENGTH_LONG).show();
    }

    private boolean verifyCredentials(String name, String user_email) {
        return !name.equals("") && !user_email.equals("");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Error", "ConnectionResult" + connectionResult.toString());
    }


    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        GoogleSignInAccount acct = result.getSignInAccount();
        if(acct!=null) {
            Log.e("Error", "display name: " + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            saveCredentials(personName, email);
        }

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_logout_from_google:
                signOut();
                break;

        }
    }
}
