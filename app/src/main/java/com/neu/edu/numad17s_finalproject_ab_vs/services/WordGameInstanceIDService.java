package com.neu.edu.numad17s_finalproject_ab_vs.services;

/**
 * Created by ashishbulchandani on 01/03/17.
 */

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.neu.edu.numad17s_finalproject_ab_vs.Utils;


public class WordGameInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = WordGameInstanceIDService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        // Creating new user node, which returns the unique key value
        // new user node would be /users/$userid/
        String userId = myRef.push().getKey();

        // save the user id to the disk
        Utils.saveMyUserId(userId,getApplicationContext());
        Utils.saveRegId(refreshedToken,getApplicationContext());

        // creating player object
        // pushing player to 'users' node using the userId
        Utils.createNewUser(getApplicationContext(),"no_user_name_selected",refreshedToken,1);



        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

}