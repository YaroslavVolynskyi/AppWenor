package com.yarik.wenor;

import javax.crypto.AEADBadTagException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.view.View;

public class Story {

	private String backgroundColor;

	private int articleId;

	private String title;

	private String subtitle;

	private String fullText;

	private String[] imageUrls;

	private String articleType;

	public Story() {

	}

	public Story(JSONObject story) {
		if (story.get("title") != null) {
			setTitle((String) story.get("title"));
		}
		if (story.get("subtitle") != null) {
			setSubtitle((String) story.get("subtitle"));
		}
		if (story.get("fulltext") != null) {
			setFullText((String) story.get("fulltext"));
		}
		if (story.get("articleType") != null) {
			setArticleType((String) story.get("articleType"));
		}
		if (story.get("imageUrl") != null) {
			setImageUrls(new String[] { ((String) story.get("imageUrl")) });
		}
		if (story.get("backgroundColor") != null) {
			setBackgroundColor((String) story.get("backgroundColor"));
		}
		if (story.get("articleId") != null) {
			setArticleId(Integer.parseInt(story.get("articleId").toString()));
		}
		if (story.get("imageUrls") != null) {
			JSONArray urls = (JSONArray) story.get("imageUrls");
			imageUrls = new String[urls.size()];
			for (int i = 0; i < urls.size(); i++) {
				imageUrls[0] = (String) urls.get(0);
			}
		}
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public String[] getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public int getStoryTypeValue() {
		if (articleType != null) {
			switch (articleType) {
			case "article":
				return 0;
			case "newsflash":
				return 1;
			case "visual":
				return 2;
			default:
				return 2;
			}
		}
		return 1;
	}

}
