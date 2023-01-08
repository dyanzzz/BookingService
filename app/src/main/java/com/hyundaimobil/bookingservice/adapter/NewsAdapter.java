package com.hyundaimobil.bookingservice.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hyundaimobil.bookingservice.R;
import com.hyundaimobil.bookingservice.app.AppController;
import com.hyundaimobil.bookingservice.data.NewsData;

import java.util.List;

/**
 * Created by User HMI on 12/12/2017.
 */

public class NewsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsData> newsItems;
    private ImageLoader imageLoader;

    public NewsAdapter(Activity activity, List<NewsData> newsItems) {
        this.activity   = activity;
        this.newsItems  = newsItems;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newsItems.get(location);
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
            convertView = inflater.inflate(R.layout.activity_main_news, null);
        }

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail  = convertView.findViewById(R.id.news_gambar);
        TextView judul              = convertView.findViewById(R.id.news_judul);
        TextView timestamp          = convertView.findViewById(R.id.news_timestamp);
        TextView isi                = convertView.findViewById(R.id.news_isi);

        NewsData news = newsItems.get(position);

        thumbNail.setImageUrl(news.getGambar(), imageLoader);
        judul.setText(news.getJudul());
        timestamp.setText(news.getDatetime());
        isi.setText(Html.fromHtml(news.getIsi()));

        return convertView;
    }

}
