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

public class MainActivity extends Activity implements
LoaderManager.LoaderCallbacks<List<Story>> {

    private ListView storiesListView;
    private StoriesListViewAdapter adapter;

    private ScrollView fullArticleScrollView;
    private TextView fullArticleTextView;

    private Handler handler;

    private boolean needWebArticles = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.storiesListView = (ListView) findViewById(R.id.storiesListView);
        this.storiesListView
        .setOnItemClickListener(this.storiesListViewItemClickListener);



        this.fullArticleScrollView = (ScrollView) findViewById(R.id.articlesFulltextScrollView);
        this.fullArticleTextView = (TextView) findViewById(R.id.articlesFulltextTextView);

        this.fullArticleTextView.setOnClickListener(this.fullTextClickListener);

        getLoaderManager().initLoader(0, null, MainActivity.this);
        // if (this.needWebArticles) {
        //Handler handler = new Handler();
        getLoaderManager().initLoader(1, null, new WebArticlesLoaderCallback());
        /*handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getLoaderManager().initLoader(1, null, MainActivity.this);
            }
        }, 4000);*/
        this.needWebArticles = false;
        //}
    }

    private final OnClickListener fullTextClickListener = new OnClickListener() {

        @Override
        public void onClick(final View v) {
            Animation slideDown = AnimationUtils.loadAnimation(
                MainActivity.this, R.anim.slide_down_animation);
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



    @Override
    public Loader<List<Story>> onCreateLoader(final int id, final Bundle args) {
        //if (id == 1) {
        //  return new WebArticlesLoader(this);
        //}
        return new ArticlesLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<List<Story>> loader, final List<Story> articlesList) {
        if (this.adapter == null) {
            this.adapter = new StoriesListViewAdapter(this, articlesList);
            this.storiesListView.setAdapter(this.adapter);
        } else {
            ((StoriesListViewAdapter) this.storiesListView.getAdapter()).addStories(articlesList);
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(final Loader<List<Story>> loader) {
    }

    public class WebArticlesLoaderCallback implements LoaderManager.LoaderCallbacks<List<Story>> {

        @Override
        public Loader<List<Story>> onCreateLoader(final int id, final Bundle args) {
            return new WebArticlesLoader(this);
        }

        @Override
        public void onLoadFinished(final Loader<List<Story>> arg0, final List<Story> articlesList) {
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
            // TODO Auto-generated method stub

        }

    }
}
