package com.yarik.wenor;

import java.io.IOException;
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

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class WebArticlesLoader extends AsyncTaskLoader<List<Story>> {

    private final Context context;
    private List<Story> articlesList;

    private static final String URL = "https://nextto.com/homework.txt?";

    public WebArticlesLoader(final Context context) {
        super(context);
        this.context = context;
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){

                @Override
                public boolean verify(final String hostname, final SSLSession session) {

                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                    final String authType) throws CertificateException {}
                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                    final String authType) throws CertificateException {}
                @Override
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
        HttpClient defClient = new DefaultHttpClient();
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        SingleClientConnManager singleClientConnManager = new SingleClientConnManager(defClient.getParams(), registry);
        DefaultHttpClient httpclient = new DefaultHttpClient(singleClientConnManager, defClient.getParams());

        HttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(WebArticlesLoader.URL);
            response = httpclient.execute(httpGet);
        } catch (ClientProtocolException e) {
            Log.e("protocol exc", e.getMessage());
        } catch (IOException e) {
            Log.e("io exc", e.getMessage());
        }

        String json = null;
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            Log.e("parse exc", e.getMessage());
        } catch (IOException e) {
            Log.e("io exc", e.getMessage());
        }
        JSONArray articlesJSONArray = (JSONArray) JSONValue.parse(json);

        if (this.articlesList == null) {
            this.articlesList = new ArrayList<>();
        }
        for (int i = 0; i < articlesJSONArray.size(); i++) {
            JSONObject article = (JSONObject) articlesJSONArray.get(i);
            this.articlesList.add(new Story(article));
        }

        return this.articlesList;
    }

    @Override
    protected void onStartLoading() {
        if (this.articlesList != null) {
            deliverResult(this.articlesList);
        }

        if (takeContentChanged() || (this.articlesList == null)) {
            forceLoad();
        }
    }

}
