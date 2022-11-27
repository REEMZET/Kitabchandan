package com.reemzet.chandan.notification;

import android.app.Application;


import com.onesignal.OneSignal;

;import org.json.JSONException;
import org.json.JSONObject;


public class NotificationOneSignal extends Application {
    private static final String ONESIGNAL_APP_ID = "cc483741-c16c-4ca7-9f9a-f70077b3c08d";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.promptForPushNotifications();


    }

}
