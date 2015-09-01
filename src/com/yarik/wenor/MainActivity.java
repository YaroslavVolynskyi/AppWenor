package com.yarik.wenor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.JSONException;
import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements
		LoaderManager.LoaderCallbacks<List<Story>> {

	private ListView storiesListView;
	private StoriesListViewAdapter adapter;

	private ScrollView fullArticleScrollView;
	private TextView fullArticleTextView;

	private Handler handler;
	
	private boolean needWebArticles = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		storiesListView = (ListView) findViewById(R.id.storiesListView);
		storiesListView
				.setOnItemClickListener(storiesListViewItemClickListener);

		getLoaderManager().initLoader(0, null, this);

		fullArticleScrollView = (ScrollView) findViewById(R.id.articlesFulltextScrollView);
		fullArticleTextView = (TextView) findViewById(R.id.articlesFulltextTextView);

		fullArticleTextView.setOnClickListener(fullTextClickListener);
		
		if (needWebArticles) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					getLoaderManager().initLoader(1, null, MainActivity.this);
				}
			}, 4000);
			needWebArticles = false;
		}
	}

	private OnClickListener fullTextClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Animation slideDown = AnimationUtils.loadAnimation(
					MainActivity.this, R.anim.slide_down_animation);
			fullArticleScrollView.startAnimation(slideDown);
			fullArticleScrollView.setVisibility(View.GONE);
		}
	};

	private OnItemClickListener storiesListViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			fullArticleTextView.setText(((Story) storiesListView.getAdapter()
					.getItem(position)).getFullText());
			fullArticleScrollView.setBackgroundColor(Color
					.parseColor(((Story) storiesListView.getAdapter().getItem(
							position)).getBackgroundColor()));
			fullArticleScrollView.setVisibility(View.VISIBLE);
			Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.slide_up_animation);
			fullArticleScrollView.startAnimation(slideUp);
		}

	};

	public static interface Donwloader {
		void downloaded();
	}
	
	@Override
	public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
		if (id == 1) {
			return new WebArticlesLoader(this);
		}
		return new ArticlesLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<Story>> loader, List<Story> articlesList) {
		if (adapter == null) {
			adapter = new StoriesListViewAdapter(this, articlesList);
			storiesListView.setAdapter(adapter);
		} else {
			((StoriesListViewAdapter) storiesListView.getAdapter()).addStories(articlesList);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<Story>> loader) {
	}
}
