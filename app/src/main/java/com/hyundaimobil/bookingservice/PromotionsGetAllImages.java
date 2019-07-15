package com.hyundaimobil.bookingservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hyundaimobil.bookingservice.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User HMI on 7/13/2017.
 */

public class PromotionsGetAllImages {
    public static String[] imageURLs;
    public static Bitmap[] bitmaps;

    public static String[] namaImage;
    public static String[] deskripsiImage;

    private String json;
    private JSONArray urls;

    public PromotionsGetAllImages(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            urls = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(Config.TAG_ALL_PROMOTIONS));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getAllImages() throws JSONException {
        bitmaps         = new Bitmap[urls.length()];
        imageURLs       = new String[urls.length()];
        namaImage       = new String[urls.length()];
        deskripsiImage  = new String[urls.length()];

        for(int i=0;i<urls.length();i++){
            imageURLs[i]            = urls.getJSONObject(i).getString(Config.TAG_ALL_PROMOTIONS);
            namaImage[i]            = urls.getJSONObject(i).getString(Config.TAG_ALL_NAMA_PROMOTIONS);
            deskripsiImage[i]       = urls.getJSONObject(i).getString(Config.TAG_ALL_DESKRIPSI_PROMOTIONS);
            JSONObject jsonObject   = urls.getJSONObject(i);
            bitmaps[i]              = getImage(jsonObject);
        }
    }
}
