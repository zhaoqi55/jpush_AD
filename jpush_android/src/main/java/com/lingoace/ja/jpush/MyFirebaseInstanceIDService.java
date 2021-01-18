package com.lingoace.ja.jpush;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by HongJay on 2016/12/10.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("======","开始运行服务");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("aaaa", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshToken) {
        Log.i("IDService", "token:"+refreshToken);
    }

}
