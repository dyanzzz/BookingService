package com.hyundaimobil.bookingservice.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hyundaimobil.bookingservice.R;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.data.ListInboxData;

import java.util.List;

public class ListInboxAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListInboxData> listInboxItems;
    private ImageLoader imageLoader;

    public ListInboxAdapter(Activity activity, List<ListInboxData> listInboxProgressItems) {
        this.activity           = activity;
        this.listInboxItems   = listInboxProgressItems;
    }

    @Override
    public int getCount() {
        return listInboxItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listInboxItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_inbox, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor      = convertView.findViewById(R.id.nomor);
        TextView date       = convertView.findViewById(R.id.date);
        TextView title      = convertView.findViewById(R.id.title);

        ListInboxData ambil = listInboxItems.get(position);

        nomor.setText(ambil.getNomor());
        date.setText(ambil.getDate());
        title.setText(ambil.getTitle());

        return convertView;
    }
}
