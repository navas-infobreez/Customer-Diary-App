package com.plannet.apps.diarybook.SyncManager;

import android.util.Log;

import com.plannet.apps.diarybook.models.RoleModel;

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

    public JSONObject RollCreationJson(RoleModel roleModel) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",roleModel.getId());
            jsonObject.put("name",roleModel.getRoleName() );
            jsonObject.put("description",roleModel.getDescription() );
            jsonObject.put("priority", roleModel.getPriority());
            jsonObject.put("active", roleModel.isActive());
            Log.d("orderjson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
