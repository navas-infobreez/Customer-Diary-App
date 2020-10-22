package com.plannet.apps.diarybook.SyncManager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserContacts;
import com.plannet.apps.diarybook.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public JSONObject toUserJson(UserModel userModel) {
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("id", 0);
            jsonObject.put("firstName", userModel.getName());
            jsonObject.put("lastName", "h");
            jsonObject.put("userName", userModel.getUserName());

            jsonObject.put("userContact", null);


            JSONArray payments = new JSONArray();
            List<RoleModel> lines = new ArrayList<>();
            RoleModel line = new RoleModel();
            line.setRoleId( 1 );
            line.setRoleName( "ADMIN" );
            line.setActive( true );
            line.setDescription( "ADMIN" );
            line.setPriority( 1 );
            lines.add( line );

            if(lines != null ) {
                Gson gson = new Gson();
                JSONArray json_lines = new JSONArray();
                Type type = new TypeToken<List<UserModel>>() {}.getType();
                json_lines = new JSONArray(gson.toJson(lines, type));
                jsonObject.put("roles", json_lines);
            }
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<UserContacts>>() {}.getType();
//            List<UserContacts> lines =gson.fromJson(userModel.getUserContacts(),type);
//            if(lines != null && lines.size()>0) {
//                //Gson gson = CustomGsonBuilder.getGson();
//                JSONArray json_lines = new JSONArray();
//                //Type type = new TypeToken<List<CustomerContactsModel>>() {}.getType();
//                json_lines = new JSONArray(gson.toJson(lines, type));
//                jsonObject.put("contacts", json_lines);
//            }

            Log.d("userJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
