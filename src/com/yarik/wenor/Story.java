package com.yarik.wenor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    public Story(final JSONObject story) {
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
            this.imageUrls = new String[urls.size()];
            for (int i = 0; i < urls.size(); i++) {
                this.imageUrls[i] = (String) urls.get(i);
            }
        }
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(final String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getArticleId() {
        return this.articleId;
    }

    public void setArticleId(final int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public String getFullText() {
        return this.fullText;
    }

    public void setFullText(final String fullText) {
        this.fullText = fullText;
    }

    public String[] getImageUrls() {
        return this.imageUrls;
    }

    public void setImageUrls(final String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getArticleType() {
        return this.articleType;
    }

    public void setArticleType(final String articleType) {
        this.articleType = articleType;
    }

    public int getStoryTypeValue() {
        if (this.articleType != null) {
            switch (this.articleType) {
                case "article":
                    return 0;
                case "newsflash":
                    return 1;
                case "visual":
                    return 2;
                default:
                    return 1;
            }
        }
        return 1;
    }

}
