package com.neu.edu.numad17s_finalproject_ab_vs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ashishbulchandani on 02/03/17.
 */

public class Utils {


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    //saves the unique user id generated at the time of generating token
    // this id is used to update the status of the current user i.e. set status to online/off-line (1/0).
    public static void saveMyUserId(String userId, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.USER_ID, userId).apply();

    }

    public static String getMyUserId( Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_ID, "no user name selected");

    }

    public static void saveUserName(String newUsername, Context applicationContext) {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(Constants.USER_NAME, newUsername).apply();
    }

    public static String getUserName( Context applicationContext) {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(Constants.USER_NAME, "");
    }

    public static void setStatusOnline(Context context) {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference("users")
                .child(Utils.getMyUserId(context));
        Map<String,Object> newNameValuePair = new HashMap<>();
        newNameValuePair.put("status",1);
        temp.updateChildren(newNameValuePair);
    }

    public static void setStatusOffline(Context context) {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference("users")
                .child(Utils.getMyUserId(context));
        Map<String,Object> newNameValuePair = new HashMap<>();
        newNameValuePair.put("status",0);
        temp.updateChildren(newNameValuePair);
    }

    public static void createNewUser(Context context,String name,String regID,long status){
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference("users")
                .child(Utils.getMyUserId(context));
        Map<String,Object> newNameValuePair = new HashMap<>();
        newNameValuePair.put("name",name);
        newNameValuePair.put("regID",regID);
        newNameValuePair.put("status",status);
        temp.updateChildren(newNameValuePair);


    }

    public static void saveRegId(String regid, Context applicationContext) {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(Constants.REG_ID, regid).apply();
    }


    public static String getRegId( Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.REG_ID, "");

    }

    public static void sendStateToChallenger(final String state, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pushNotification(Constants.SEND_STATE,state,Utils.getChallengerRegId(context),Constants.UpdateLiveBoard);
                }catch (Exception e){


                }

            }
        }).start();
    }

    public static String getChallengerRegId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.CHALLENGER_REG_ID, "");
    }

    public static void saveChallengerRegId(String regid, Context applicationContext) {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(Constants.CHALLENGER_REG_ID, regid).apply();
    }


    public static void sendPushNotification(final String title,  final String body,final String clientId, final String click_action){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pushNotification(title,body,clientId,click_action);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private static void pushNotification(String title,String body,String client_regid,String click_action) {

        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", title);
            jNotification.put(Constants.BODY, body);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", click_action);


            jPayload.put("to", client_regid);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AAAAVapgyOc:APA91bF13XyyXqulh667Z07DJrkrkm0kW1kuhpdvkRk_wHKUM09ut4G_afPTsIpAs71wtZCcHDcwo4aWDGs77dvGqMfokqkh-zSa3UtDfqesAKufX4gD6RvXGS7BFeMd4M6rS5FM8CzK");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("push", "run: " + resp);
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    public static void createNewGame(String challengee, String challenger, String state,Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("challenges");
        String key = myRef.push().getKey();
        myRef = myRef.child(key);
        Utils.saveChallengeKey(key,context);
        Map<String,Object> newNameValuePair = new HashMap<>();
        newNameValuePair.put("challengee",challengee);
        newNameValuePair.put("challenger",challenger);
        newNameValuePair.put("state",state);
        newNameValuePair.put(Constants.TURN,0);
        newNameValuePair.put(Constants.SCORE_PLAYER1,0);
        newNameValuePair.put(Constants.SCORE_PLAYER2,0);
        myRef.updateChildren(newNameValuePair);
    }

    public static void createNewAsyncGame(String challengee, String challenger, String state,Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("challenges");
        String key = myRef.push().getKey();
        myRef = myRef.child(key);
        Utils.saveAsyncChallengeKey(key,context);
        Map<String,Object> newNameValuePair = new HashMap<>();
        newNameValuePair.put("challengee",challengee);
        newNameValuePair.put("challenger",challenger);
        newNameValuePair.put("state",state);
        newNameValuePair.put(Constants.TURN,0);
        newNameValuePair.put(Constants.SCORE_PLAYER1,0);
        newNameValuePair.put(Constants.SCORE_PLAYER2,0);
        newNameValuePair.put(Constants.TIME_PLAYER0,90000);
        newNameValuePair.put(Constants.TIME_PLAYER1,90000);
        myRef.updateChildren(newNameValuePair);
    }

    public static String getChallengeKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.CHALLENGE_KEY, "");
    }

    public static void saveChallengeKey(String CHALLENGE_KEY, Context applicationContext) {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(Constants.CHALLENGE_KEY, CHALLENGE_KEY).apply();
    }


    public static String getAsyncChallengeKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.ASYNC_CHALLENGE_KEY, "");
    }

    public static void saveAsyncChallengeKey(String CHALLENGE_KEY, Context applicationContext) {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(Constants.ASYNC_CHALLENGE_KEY, CHALLENGE_KEY).apply();
    }


    public void showInternetErrorDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("No Internet Connection. Cannot Proceed");
        builder.setTitle("Challenge");
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });
        builder.show();

    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isUserPresent(Context context) {
        return !"".equals(PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_NAME, ""));

    }

    public static void saveUserEmail(String s, Context context) {

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.EMAIL, s).apply();

    }





}
