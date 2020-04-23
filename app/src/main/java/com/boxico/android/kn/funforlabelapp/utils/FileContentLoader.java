package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class FileContentLoader {

    public void loadFileContent(Context ctx){

        new Gson().fromJson(getContentFromCountryFile(ctx), new TypeToken<Map<String, List<String>>>() { }.getType());
    }

    private String getContentFromCountryFile(Context ctx) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(ctx.getAssets().open("paisProvinciaLocalidad.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // do reading, usually loop until end of file reading
        String mLine = null;
        try {
            mLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mLine;
    }

}
