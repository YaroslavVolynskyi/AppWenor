package com.yarik.wenor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class GalleryGridAdapter extends BaseAdapter {

    private final String[] imagesUrls;

    private final Context context;

    private final ImageLoadingListener imageLoadingListener;

    public GalleryGridAdapter(final String[] imagesUrls, final Context context,
        final ImageLoadingListener imageLoadingListener) {
        this.imagesUrls = imagesUrls;
        this.context = context;
        this.imageLoadingListener = imageLoadingListener;
    }

    @Override
    public int getCount() {
        if (this.imagesUrls != null) {
            return this.imagesUrls.length;
        }
        return 0;
    }

    @Override
    public Object getItem(final int position) {
        if ((this.imagesUrls != null) && (this.imagesUrls.length > 0)) {
            return this.imagesUrls[position];
        }
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        ImageHolder holder = new ImageHolder();
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.grid_item_layout, null);
            holder.imageView = (ImageView) view.findViewById(R.id.grid_item_imageview);
            view.setTag(holder);
        }
        holder = (ImageHolder) view.getTag();
        String imageUrl = this.imagesUrls[position];
        if ("http://animage.com".equals(imageUrl)) {
            imageUrl = "http://lorempixel.com/300/300/abstract/";
        }
        ImageLoader.getInstance().displayImage(imageUrl,
            holder.imageView,
            this.imageLoadingListener
            );
        return view;
    }

    private static class ImageHolder {
        public ImageView imageView;
    }

}
