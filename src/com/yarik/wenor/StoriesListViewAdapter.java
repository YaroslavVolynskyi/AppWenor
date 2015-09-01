package com.yarik.wenor;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class StoriesListViewAdapter extends ArrayAdapter<Story> {

	private List<Story> storiesList;

	private Context context;

	private static final int VIEWS_AMOUNT = 3;

	public StoriesListViewAdapter(Context context, List<Story> storiesList) {
		super(context, 0, storiesList);
		this.storiesList = storiesList;
		this.context = context;
	}

	@Override
	public int getItemViewType(int position) {
		return storiesList.get(position).getStoryTypeValue();
	}

	@Override
	public int getViewTypeCount() {
		return VIEWS_AMOUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = this.getItemViewType(position);
		Story story = storiesList.get(position);
		View view = convertView;
		switch (viewType) {
		case 0:
			ArticleViewHolder holder = new ArticleViewHolder();
			
			if (view == null) {
				view = LayoutInflater.from(context).inflate(
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

			holder.titleTextView.setText(story.getTitle());
			holder.subtitleTextView.setText(story.getSubtitle());
			break;
		case 1:
			ArticleViewHolder newsflashHolder = new ArticleViewHolder();
			if (view == null) {
				view = LayoutInflater.from(context).inflate(
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
				view = LayoutInflater.from(context).inflate(
						R.layout.visual_layout, null);
				visualHolder.titleTextView = (TextView) view
						.findViewById(R.id.storyTitleTextView);
				visualHolder.galleryGridView = (GridView) view
						.findViewById(R.id.storyGridView);
				view.setTag(visualHolder);
			}
			visualHolder = (VisualViewHolder) view.getTag();
			visualHolder.titleTextView.setText(story.getTitle());
			break;
		}
		if (story.getBackgroundColor() != null && !story.getBackgroundColor().isEmpty()) {
			view.setBackgroundColor(Color.parseColor(story.getBackgroundColor()));
		}
		return view;

	}

	public void addStories(Story story) {
		storiesList.add(story);
		notifyDataSetChanged();
	}
	
	public void addStories(List<Story> stories) {
		for (Story story : stories) {
			storiesList.add(story);
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
}
