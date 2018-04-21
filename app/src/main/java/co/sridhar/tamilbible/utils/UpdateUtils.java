package co.sridhar.tamilbible.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Update utils
 * Created by sridharrajs on 9/29/17.
 */

public class UpdateUtils {

    public interface Callback {
        void onResponse(Boolean isUpdateAvailable);

        void onError(Exception ex);
    }

    public static void checkNow(Context mContext, String currentVersion, final Callback callback) {
        String API_URL = "http://api.onebible.in/?app_version=";
        String url = API_URL + currentVersion;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean isUpdateAvailable = response.getBoolean("is_update_available");
                    callback.onResponse(isUpdateAvailable);
                } catch (Exception ex) {
                    callback.onError(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(jsonObjReq);
    }


}
