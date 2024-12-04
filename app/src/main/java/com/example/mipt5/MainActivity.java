package com.example.mipt5;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView currencyListView;
    private TextView statusTextView;
    private Switch asyncSwitch;
    private EditText currencyFilterInput;
    private ArrayAdapter<String> adapter;
    private String[] loadedCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        currencyListView = findViewById(R.id.lv_items);
        statusTextView = findViewById(R.id.tv_status);
        asyncSwitch = findViewById(R.id.sw_use_async_task);
        currencyFilterInput = findViewById(R.id.filterEditText);

        // Set up the ListView adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        currencyListView.setAdapter(adapter);
    }

    public void onBtnGetDataClick(View view) {
        statusTextView.setText("Data loading...");
        if (asyncSwitch.isChecked()) {
            fetchCurrenciesWithAsyncTask();
            Toast.makeText(this, "Using AsyncTask", Toast.LENGTH_SHORT).show();
        } else {
            fetchCurrenciesWithThread();
            Toast.makeText(this, "Using Thread", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBtnFilterData(View view) {
        String filterText = currencyFilterInput.getText().toString().trim();

        if (TextUtils.isEmpty(filterText)) {
            Toast.makeText(this, "Please enter a currency code", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> filteredList = new ArrayList<>();
        if (loadedCurrencies != null) {
            for (String currency : loadedCurrencies) {
                if (currency.toLowerCase().contains(filterText.toLowerCase())) {
                    filteredList.add(currency);
                }
            }
        }

        if (filteredList.isEmpty()) {
            statusTextView.setText("No results found for: " + filterText);
        } else {
            adapter.clear();
            adapter.addAll(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchCurrenciesWithAsyncTask() {
        new DataLoader() {
            @Override
            public void onPostExecute(String result) {
                if (result != null && !result.isEmpty()) {
                    loadedCurrencies = result.split("\n");
                    statusTextView.setText("Data loaded successfully!");
                    adapter.clear();
                    adapter.addAll(loadedCurrencies);
                    adapter.notifyDataSetChanged();
                } else {
                    statusTextView.setText("Failed to fetch data.");
                }
            }
        }.execute("https://www.floatrates.com/daily/eur.xml");
    }

    private void fetchCurrenciesWithThread() {
        new Thread(() -> {
            try {
                String result = DataReader.getValuesFromApi("https://www.floatrates.com/daily/eur.xml");
                loadedCurrencies = result.split("\n");

                runOnUiThread(() -> {
                    statusTextView.setText("Data loaded successfully!");
                    adapter.clear();
                    adapter.addAll(loadedCurrencies);
                    adapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                Log.e("MainActivity", "Error fetching data", e);
                runOnUiThread(() -> statusTextView.setText("Error loading data."));
            }
        }).start();
    }
}
