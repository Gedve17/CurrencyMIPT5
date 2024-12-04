package com.example.mipt5;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataReader {

    public static String getValuesFromApi(String apiUrl) throws IOException {
        InputStream apiContentStream = null;
        try {
            apiContentStream = downloadURLContent(apiUrl);
            return Parser.parseCurrencyRates(apiContentStream);
        } finally {
            if (apiContentStream != null) {
                apiContentStream.close();
            }
        }
    }

    private static InputStream downloadURLContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        Log.d("DataReader", "Connected to URL: " + urlString);
        return conn.getInputStream();
    }
}
