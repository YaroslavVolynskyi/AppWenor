package com.yarik.wenor;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class StoriesListViewAdapter extends ArrayAdapter<Story> {

    private final List<Story> storiesList;

    private final Context context;

    private static final int VIEWS_AMOUNT = 3;

    public StoriesListViewAdapter(final Context context, final List<Story> storiesList) {
        super(context, 0, storiesList);
        this.storiesList = storiesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return this.storiesList.get(position).getStoryTypeValue();
    }

    @Override
    public int getViewTypeCount() {
        return StoriesListViewAdapter.VIEWS_AMOUNT;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        Story story = this.storiesList.get(position);
        View view = convertView;
        switch (viewType) {
            case 0:
                ArticleViewHolder holder = new ArticleViewHolder();

                if (view == null) {
                    view = LayoutInflater.from(this.context).inflate(
                        R.layout.article_layout, null);
                    holder.articleImageView = (ImageView) view
                        .findViewById(R.id.storyImageView);
                    holder.titleTextView = (TextView) view
                        .findViewById(R.id.storyTitleTextView);
                    holder.subtitleTextView = (TextView) view
                        .findViewById(R.id.storySubtitleTextView);
                    view.setTag(holder);
                }
                holder = (ArticleViewHolder) view.getTag();
                if ((story.getImageUrls() != null) && (story.getImageUrls().length == 1)) {
                    ImageLoader.getInstance().displayImage(story.getImageUrls()[0],
                        holder.articleImageView,
                        this.imageLoadingListener
                        );
                }
                holder.titleTextView.setText(story.getTitle());
                holder.subtitleTextView.setText(story.getSubtitle());
                break;
            case 1:
                ArticleViewHolder newsflashHolder = new ArticleViewHolder();
                if (view == null) {
                    view = LayoutInflater.from(this.context).inflate(
                        R.layout.newsflash_layout, null);
                    newsflashHolder.titleTextView = (TextView) view
                        .findViewById(R.id.storyTitleTextView);
                    view.setTag(newsflashHolder);
                }
                newsflashHolder = (ArticleViewHolder) view.getTag();
                newsflashHolder.titleTextView.setText(story.getTitle());
                break;
            case 2:
                VisualViewHolder visualHolder = new VisualViewHolder();
                if (view == null) {
                    view = LayoutInflater.from(this.context).inflate(
                        R.layout.visual_layout, null);
                    visualHolder.titleTextView = (TextView) view
                        .findViewById(R.id.storyTitleTextView);
                    visualHolder.galleryGridView = (GridView) view
                        .findViewById(R.id.storyGridView);
                    view.setTag(visualHolder);
                }
                visualHolder = (VisualViewHolder) view.getTag();
                visualHolder.titleTextView.setText(story.getTitle());
                if ((story.getImageUrls() != null) && (story.getImageUrls().length > 1)) {
                    GalleryGridAdapter gridAdapter = new GalleryGridAdapter(story.getImageUrls(), this.context, this.imageLoadingListener);
                    visualHolder.galleryGridView.setAdapter(gridAdapter);
                }
                break;
        }
        if ((story.getBackgroundColor() != null) && !story.getBackgroundColor().isEmpty()) {
            view.setBackgroundColor(Color.parseColor(story.getBackgroundColor()));
        }
        return view;
    }

    public void addStories(final Story story) {
        this.storiesList.add(story);
        notifyDataSetChanged();
    }

    public void addStories(final List<Story> stories) {
        for (Story story : stories) {
            if (!this.storiesList.contains(story)) {
                this.storiesList.add(story);
            }
        }
        notifyDataSetChanged();
    }

    private static class ArticleViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public ImageView articleImageView;
    }

    private static class VisualViewHolder {
        public TextView titleTextView;
        public GridView galleryGridView;
    }

    private final ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(final String imageUri, final View view) {
        }

        @Override
        public void onLoadingFailed(final String imageUri, final View view, final FailReason failReason) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(R.drawable.placeholder);
            }
        }

        @Override
        public void onLoadingComplete(final String imageUri, final View view, final Bitmap loadedImage) {

        }

        @Override
        public void onLoadingCancelled(final String imageUri, final View view) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(R.drawable.placeholder);
            }
        }
    };
}
