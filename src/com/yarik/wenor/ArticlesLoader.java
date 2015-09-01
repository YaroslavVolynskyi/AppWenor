package com.yarik.wenor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class ArticlesLoader extends AsyncTaskLoader<List<Story>> {

	private Context context;
	private List<Story> articlesList;

	public ArticlesLoader(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public List<Story> loadInBackground() {
		BufferedReader reader = null;
		StringBuffer lines = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(context
					.getAssets().open("articles.txt"), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				lines.append(line);
			}
		} catch (IOException e) {
			Log.e("readArticles", e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("readArticles", e.getMessage());
				}
			}
		}
		JSONArray articlesJSONArray = (JSONArray) JSONValue.parse(lines
				.toString());
		if (articlesList == null) {
			articlesList = new ArrayList<>();
		}
		for (int i = 0; i < articlesJSONArray.size(); i++) {
			JSONObject article = (JSONObject) articlesJSONArray.get(i);
			Story story = new Story(article);
			articlesList.add(story);
		}
		
		return articlesList;
	}

	@Override
	protected void onStartLoading() {
		if (articlesList != null) {
			deliverResult(articlesList);
		}

		if (takeContentChanged() || articlesList == null) {
			forceLoad();
		}
	}

}
