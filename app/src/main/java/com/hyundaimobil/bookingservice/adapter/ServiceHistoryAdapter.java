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
import com.hyundaimobil.bookingservice.data.ServiceHistoryData;

import java.util.List;

/**
 * Created by User HMI on 12/20/2017.
 */

public class ServiceHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ServiceHistoryData> serviceHistoryItems;
    private ImageLoader imageLoader;

    public ServiceHistoryAdapter(Activity activity, List<ServiceHistoryData> serviceHistoryItems) {
        this.activity               = activity;
        this.serviceHistoryItems    = serviceHistoryItems;
    }

    @Override
    public int getCount() {
        return serviceHistoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return serviceHistoryItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_service_history, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor          = convertView.findViewById(R.id.no);
        TextView wo             = convertView.findViewById(R.id.wo);
        TextView woDate         = convertView.findViewById(R.id.woDate);
        TextView repairCode     = convertView.findViewById(R.id.repairCode);
        TextView description    = convertView.findViewById(R.id.description);

        ServiceHistoryData ambil = serviceHistoryItems.get(position);

        nomor.setText(ambil.getNo());
        wo.setText(ambil.getWo());
        woDate.setText(ambil.getWoDate());
        repairCode.setText(ambil.getRepairCode());
        description.setText(ambil.getDescription());

        return convertView;
    }
}
