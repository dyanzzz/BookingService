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
import com.hyundaimobil.bookingservice.data.ListBookingData;

import java.util.List;

/**
 * Created by User HMI on 10/30/2017.
 */

public class ListBookingAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListBookingData> listBookingItems;
    private ImageLoader imageLoader;

    public ListBookingAdapter(Activity activity, List<ListBookingData> listBookingProgressItems) {
        this.activity           = activity;
        this.listBookingItems   = listBookingProgressItems;
    }

    @Override
    public int getCount() {
        return listBookingItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listBookingItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_booking, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor      = convertView.findViewById(R.id.nomor);
        TextView namaDealer = convertView.findViewById(R.id.namaDealer);
        TextView date       = convertView.findViewById(R.id.date);
        TextView time       = convertView.findViewById(R.id.time);
        TextView remarks    = convertView.findViewById(R.id.remarks);
        TextView note       = convertView.findViewById(R.id.note);
        ImageView icon      = convertView.findViewById(R.id.icon);

        ListBookingData ambil = listBookingItems.get(position);

        nomor.setText(ambil.getNomor());
        namaDealer.setText(ambil.getNamaDealer());
        date.setText(ambil.getDate());
        time.setText(ambil.getTime());
        remarks.setText(ambil.getRemarks());
        note.setText(ambil.getNote());

        if(ambil.getStatusBooking().equals("00")){
            icon.setImageResource(R.mipmap.booking_date_sukses);
        }else{
            icon.setImageResource(R.mipmap.booking_date_sukses);
        }

        return convertView;
    }
}
