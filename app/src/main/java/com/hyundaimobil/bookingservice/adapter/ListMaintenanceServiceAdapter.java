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
import com.hyundaimobil.bookingservice.data.ListMaintenanceServiceData;

import java.util.List;

/**
 * Created by User HMI on 2/2/2018.
 */

public class ListMaintenanceServiceAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ListMaintenanceServiceData> listMaintenanceServiceItems;
    private ImageLoader imageLoader;

    public ListMaintenanceServiceAdapter(Activity activity, List<ListMaintenanceServiceData> listMaintenanceItems) {
        this.activity           = activity;
        this.listMaintenanceServiceItems   = listMaintenanceItems;
    }

    @Override
    public int getCount() {
        return listMaintenanceServiceItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listMaintenanceServiceItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_maintenance_service, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor      = convertView.findViewById(R.id.nomor);
        TextView coy        = convertView.findViewById(R.id.coy_spinner_list_item);
        TextView lts        = convertView.findViewById(R.id.lts_spinner_list_item);
        TextView spinner    = convertView.findViewById(R.id.spinner_list_item);

        ListMaintenanceServiceData ambil = listMaintenanceServiceItems.get(position);

        nomor.setText(ambil.getNomor());
        coy.setText(ambil.getCoy());
        lts.setText(ambil.getLts());
        spinner.setText(ambil.getName());

        return convertView;
    }
}
