package com.yarik.wenor;

import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private ListView storiesListView;

    private StoriesListViewAdapter adapter;

    private ScrollView fullArticleScrollView;

    private TextView fullArticleTextView;

    private Handler handler;

    private static final int LOAD_DELAY = 4000;

    private static final int LOCAL_LOADER_ID = 0;

    private static final int WEB_LOADER_ID = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader();

        this.storiesListView = (ListView) findViewById(R.id.storiesListView);
        this.storiesListView.setOnItemClickListener(this.storiesListViewItemClickListener);

        this.fullArticleScrollView = (ScrollView) findViewById(R.id.articlesFulltextScrollView);
        this.fullArticleTextView = (TextView) findViewById(R.id.articlesFulltextTextView);
        this.fullArticleTextView.setOnClickListener(this.fullTextClickListener);

        getLoaderManager().initLoader(MainActivity.LOCAL_LOADER_ID, null, this);
        this.handler = new Handler();
        this.handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getLoaderManager().initLoader(MainActivity.WEB_LOADER_ID, null, MainActivity.this);
            }
        }, MainActivity.LOAD_DELAY);
    }

    @Override
    public Loader<List<Story>> onCreateLoader(final int id, final Bundle args) {
        if (id == MainActivity.LOCAL_LOADER_ID) {
            return new ArticlesLoader(MainActivity.this);
        }
        return new WebArticlesLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(final Loader<List<Story>> loader, final List<Story> articlesList) {
        if (MainActivity.this.adapter == null) {
            MainActivity.this.adapter = new StoriesListViewAdapter(MainActivity.this, articlesList);
            MainActivity.this.storiesListView.setAdapter(MainActivity.this.adapter);
        } else {
            ((StoriesListViewAdapter) MainActivity.this.storiesListView.getAdapter()).addStories(articlesList);
        }
        MainActivity.this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(final Loader<List<Story>> arg0) {
    }

    private final OnClickListener fullTextClickListener = new OnClickListener() {

        @Override
        public void onClick(final View v) {
            Animation slideDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down_animation);
            MainActivity.this.fullArticleScrollView.startAnimation(slideDown);
            MainActivity.this.fullArticleScrollView.setVisibility(View.GONE);
        }
    };

    private final OnItemClickListener storiesListViewItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position,
            final long id) {
            MainActivity.this.fullArticleTextView.setText(((Story) MainActivity.this.storiesListView.getAdapter()
                .getItem(position)).getFullText());
            MainActivity.this.fullArticleScrollView.setBackgroundColor(Color
                .parseColor(((Story) MainActivity.this.storiesListView.getAdapter().getItem(
                    position)).getBackgroundColor()));
            MainActivity.this.fullArticleScrollView.setVisibility(View.VISIBLE);
            Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.slide_up_animation);
            MainActivity.this.fullArticleScrollView.startAnimation(slideUp);
        }
    };

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();

        ImageLoader.getInstance().init(config);
    }

}
