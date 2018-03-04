package com.neu.edu.numad17s_finalproject_ab_vs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0000;
    public static MainActivity instance;
    Toolbar toolbar;
    private ProgressDialog progressDialog;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    public String userName;
    public String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.toolbarTitle);
        setSupportActionBar(toolbar);

        instance = this;
        Router.getInstance().initialize(this);
        Intent intent = getIntent();

        // Creating new user node, which returns the unique key value
        // new user node would be /users/$userid/


        if (intent != null && intent.getExtras() != null) {

            if (intent.getStringExtra(Constants.CLICK_ACTION) != null) {
                // check if someone has challenged
                if (intent.getStringExtra(Constants.CLICK_ACTION).equals(Constants.ACCEPT_CHALLENGE)) {

                    Utils.saveChallengerRegId(intent.getStringExtra(Constants.BODY), getApplicationContext());
//                mainViewModel.showLiveGameBoard(false);
                }
                if (intent.getStringExtra(Constants.CLICK_ACTION).equals(Constants.LIVE_ACCEPT_CHALLENGE)) {

                    Utils.saveAsyncChallengeKey(intent.getStringExtra(Constants.BODY), getApplicationContext());
//                mainViewModel.showAsyncGameBoard(false);
                }
                if (intent.getStringExtra(Constants.CLICK_ACTION).equals(Constants.LIVE_CHALLENGE_ACCEPTED)) {

                    Utils.saveAsyncChallengeKey(intent.getStringExtra(Constants.BODY), getApplicationContext());
//                MainActivity.instance.transact(AsyncGameBoardFragment.newInstance(true));

                }

            } else
                ;//  mainViewModel.showHome();

        } else
            ;//mainViewModel.showHome();

        if (Utils.isUserPresent(getApplicationContext()))
            Router.getInstance().showHome();
        else
            Router.getInstance().showCreateAccount();

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();
        else
            showAlert(getString(R.string.quit_message));
    }

    public void replace(Fragment fragment) {
        transact(fragment, false);
    }

    public void transact(Fragment fragment) {
        transact(fragment, true);
    }

    public void swapFragment(Fragment fragment) {

        getSupportFragmentManager().popBackStack();
        transact(fragment, true);

    }

    private void transact(Fragment fragment, boolean addToStack) {


        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.container, fragment);
        if (addToStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
//        dismissLoading();


    }

    public void showDialogLoading() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

    }

    public void dismissDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

    }


    public void showAlert(String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }).setMessage(message)
                .show();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Result Failed", connectionResult.toString());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
