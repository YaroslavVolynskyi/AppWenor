package com.yarik.wenor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class WebArticlesLoader extends AsyncTaskLoader<List<Story>> {

	private Context context;
	private List<Story> articlesList;

	private static final String URL = "https://nextto.com/homework.txt?";

	public WebArticlesLoader(Context context) {
		super(context);
		this.context = context;
	}
	
	private void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
	    			
					@Override
					public boolean verify(String hostname, SSLSession session) {
						
						return true;
					}});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[]{new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}}}, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(
					context.getSocketFactory());
		} catch (Exception e) { // should never happen
			e.printStackTrace();
		}
	}

	@Override
	public List<Story> loadInBackground() {
		/*
		 * BufferedReader reader = null; StringBuffer lines = new
		 * StringBuffer(); try { reader = new BufferedReader(new
		 * InputStreamReader(context .getAssets().open("articles2.txt"),
		 * "UTF-8")); String line; while ((line = reader.readLine()) != null) {
		 * lines.append(line); } } catch (IOException e) { Log.e("readArticles",
		 * e.getMessage()); } finally { if (reader != null) { try {
		 * reader.close(); } catch (IOException e) { Log.e("readArticles",
		 * e.getMessage()); } } } JSONArray articlesJSONArray = (JSONArray)
		 * JSONValue.parse(lines .toString()); if (articlesList == null) {
		 * articlesList = new ArrayList<>(); } for (int i = 0; i <
		 * articlesJSONArray.size(); i++) { JSONObject article = (JSONObject)
		 * articlesJSONArray.get(i); Story story = new Story(article);
		 * articlesList.add(story); }
		 * 
		 * return articlesList;
		 */

		trustEveryone();
		HttpUriRequest request = new HttpGet(URL.toString());
		request.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String responseString = null;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
		} catch (ClientProtocolException e) {
			Log.e("protocol exc", e.getMessage());
		} catch (IOException e) {
			Log.e("io exc", e.getMessage());
		}

		if (responseString != null) {
			JSONArray articlesJSONArray = (JSONArray) JSONValue.parse(responseString);
			if (articlesList == null) {
				articlesList = new ArrayList<>();
			}
			for (int i = 0; i < articlesJSONArray.size(); i++) {
				JSONObject article = (JSONObject) articlesJSONArray.get(i);
				Story story = new Story(article);
				articlesList.add(story);
			}
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
