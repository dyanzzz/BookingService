package com.hyundaimobil.bookingservice.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hyundaimobil.bookingservice.R;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.data.ListTipeKendaraanData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User HMI on 1/16/2018.
 */

public class    ListTipeKendaraanAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<ListTipeKendaraanData> listTipeKendaraanItems;
    private ImageLoader imageLoader;

    public ListTipeKendaraanAdapter(Activity activity, List<ListTipeKendaraanData> listTipeKendaraanItems) {
        this.activity           = activity;
        this.listTipeKendaraanItems   = listTipeKendaraanItems;
    }

    @Override
    public int getCount() {
        return listTipeKendaraanItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listTipeKendaraanItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_item_tipe_kendaraan, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor          = convertView.findViewById(R.id.nomor);
        TextView namaKendaraan  = convertView.findViewById(R.id.nama_kendaraan);
        TextView kdKendaraan    = convertView.findViewById(R.id.kd_kendaraan);
        //NetworkImageView image  = convertView.findViewById(R.id.image);
        ImageView image  = convertView.findViewById(R.id.image);

        ListTipeKendaraanData ambil = listTipeKendaraanItems.get(position);

        //image.setImageUrl(ambil.getImage(), imageLoader);
        Picasso.with(parent.getContext()).load(ambil.getImage()).into(image);
        nomor.setText(ambil.getNomor());
        namaKendaraan.setText(ambil.getNamaKendaraan());
        kdKendaraan.setText(ambil.getKdKendaraan());

        return convertView;
    }
}
