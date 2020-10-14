package com.plannet.apps.diarybook.SyncManager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormater {

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("username", "superuser");
            jsonObject.put("password", "superuser");
            Log.d("orderjson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
