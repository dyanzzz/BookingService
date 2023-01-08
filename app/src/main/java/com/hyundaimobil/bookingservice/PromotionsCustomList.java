package com.hyundaimobil.bookingservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by User HMI on 7/13/2017.
 */

public class PromotionsCustomList extends ArrayAdapter<String> {
    private String[] urls, namaImg, deskripsiImg;
    private Bitmap[] bitmaps;
    private Activity context;

    PromotionsCustomList(Activity context, String[] urls, Bitmap[] bitmaps, String[] namaImg, String[] deskripsiImg) {
        super(context, R.layout.list_item_promotions, urls);
        this.context        = context;
        this.urls           = urls;
        this.bitmaps        = bitmaps;
        this.namaImg        = namaImg;
        this.deskripsiImg   = deskripsiImg;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.list_item_promotions, null, true);
        //TextView textViewURL = listViewItem.findViewById(R.id.textViewURL);
        ImageView image = listViewItem.findViewById(R.id.promotions);

        //textViewURL.setText(urls[position]);
        //image.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position],100,50,false));
        image.setImageBitmap(PromotionsGetAllImages.bitmaps[position]);
        return  listViewItem;
    }


}
