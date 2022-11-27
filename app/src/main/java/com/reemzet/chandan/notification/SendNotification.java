package com.reemzet.chandan.notification;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {
    public SendNotification(String message, String heading){
        try {
            JSONObject notificationContent = new JSONObject(
                    "{'app_id':\"cc483741-c16c-4ca7-9f9a-f70077b3c08d\"," +
                            "'contents':{'en':'" + message + "'},"+
                            "'included_segments':[\"Active Users\"]," +
                            "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}