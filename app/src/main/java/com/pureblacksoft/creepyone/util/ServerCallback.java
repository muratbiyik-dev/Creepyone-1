package com.pureblacksoft.creepyone.util;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ServerCallback
{
    void onSuccess(JSONObject response);

    void onFailure(VolleyError error);
}

//PureBlack Software / Murat BIYIK