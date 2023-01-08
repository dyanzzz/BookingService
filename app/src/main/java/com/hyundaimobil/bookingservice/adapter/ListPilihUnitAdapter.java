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
import com.hyundaimobil.bookingservice.data.ListPilihUnitData;

import java.util.List;

/**
 * Created by User HMI on 2/27/2018.
 */

public class ListPilihUnitAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ListPilihUnitData> listPilihUnitItems;
    private ImageLoader imageLoader;

    public ListPilihUnitAdapter(Activity activity, List<ListPilihUnitData> list_pilihUnitItems) {
        this.activity           = activity;
        this.listPilihUnitItems    = list_pilihUnitItems;
    }

    @Override
    public int getCount() {
        return listPilihUnitItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listPilihUnitItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_item_pilih_unit, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        TextView nomor      = convertView.findViewById(R.id.nomor);
        TextView chasis     = convertView.findViewById(R.id.chasis);
        TextView engine     = convertView.findViewById(R.id.engine);
        TextView nopol      = convertView.findViewById(R.id.nopol);
        TextView type       = convertView.findViewById(R.id.type);
        TextView color      = convertView.findViewById(R.id.color);

        ListPilihUnitData ambil = listPilihUnitItems.get(position);

        nomor.setText(ambil.getNomor());
        chasis.setText(ambil.getChasis());
        engine.setText(ambil.getEngine());
        nopol.setText(ambil.getNopol());
        type.setText(ambil.getType());
        color.setText(ambil.getColor());

        return convertView;
    }
}
