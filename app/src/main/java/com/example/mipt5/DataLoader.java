package com.example.mipt5;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public abstract class DataLoader extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0 || params[0] == null) {
            return "Error: No URL provided.";
        }

        try {
            Log.d("DataLoader", "Fetching data from URL: " + params[0]);
            return DataReader.getValuesFromApi(params[0]);
        } catch (IOException e) {
            Log.e("DataLoader", "Error fetching data", e);
            return String.format("Error occurred while fetching data: %s", e.getMessage());
        }
    }
}
