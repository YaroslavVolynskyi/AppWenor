package com.yarik.wenor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class StoriesListViewAdapter extends ArrayAdapter<Story> {

	private final List<Story> storiesList;

	private final Activity activity;

	private static final int VIEWS_AMOUNT = 3;

	public StoriesListViewAdapter(Activity activity,
			final List<Story> storiesList) {
		super(activity, 0, storiesList);
		this.storiesList = storiesList;
		this.activity = activity;
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
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		int viewType = this.getItemViewType(position);
		Story story = this.storiesList.get(position);
		View view = convertView;
		switch (viewType) {
		case 0:
			ArticleViewHolder holder = new ArticleViewHolder();

			if (view == null) {
				view = LayoutInflater.from(this.activity).inflate(
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
			if ((story.getImageUrls() != null)
					&& (story.getImageUrls().length == 1)) {
				ImageLoader.getInstance().displayImage(story.getImageUrls()[0],
						holder.articleImageView, this.imageLoadingListener);
			}
			holder.titleTextView.setText(story.getTitle());
			holder.subtitleTextView.setText(story.getSubtitle());
			break;
		case 1:
			ArticleViewHolder newsflashHolder = new ArticleViewHolder();
			if (view == null) {
				view = LayoutInflater.from(this.activity).inflate(
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
				view = LayoutInflater.from(this.activity).inflate(
						R.layout.visual_layout, null);
				visualHolder.titleTextView = (TextView) view
						.findViewById(R.id.storyTitleTextView);
				visualHolder.galleryLinearLayout = (LinearLayout) view
						.findViewById(R.id.galleryLinearLayout);
				view.setTag(visualHolder);
			}
			visualHolder = (VisualViewHolder) view.getTag();
			visualHolder.galleryLinearLayout.setOnClickListener(new PositionedClickListener(position));
			view.setOnClickListener(new PositionedClickListener(position));
			visualHolder.titleTextView.setText(story.getTitle());
			if ((story.getImageUrls() != null)&& (story.getImageUrls().length > 1)) {
				visualHolder.galleryLinearLayout.removeAllViews();
				for (String imageUrl : story.getImageUrls()) {
					ImageView imageView = new ImageView(activity);
					ImageLoader.getInstance().displayImage(imageUrl, imageView,
							this.imageLoadingListener);
					visualHolder.galleryLinearLayout.addView(imageView);
				}
			}
			break;
		}
		if ((story.getBackgroundColor() != null)
				&& !story.getBackgroundColor().isEmpty()) {
			view.setBackgroundColor(Color.parseColor(story.getBackgroundColor()));
		}

		return view;
	}

	private class PositionedClickListener implements OnClickListener {

		private int position;

		public PositionedClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (activity instanceof MainActivity) {
				((MainActivity) activity).listViewItemClick(position);
			}
		}

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
		public LinearLayout galleryLinearLayout;
	}

	private final ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
		@Override
		public void onLoadingStarted(final String imageUri, final View view) {
		}

		@Override
		public void onLoadingFailed(final String imageUri, final View view,
				final FailReason failReason) {
			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(R.drawable.placeholder);
			}
		}

		@Override
		public void onLoadingComplete(final String imageUri, final View view,
				final Bitmap loadedImage) {

		}

		@Override
		public void onLoadingCancelled(final String imageUri, final View view) {
			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(R.drawable.placeholder);
			}
		}
	};
}
