/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.chocolatenews;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks <List <ChocoArticle>> {

    public static final String JSON = "json";
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search";
    // API student key
    private static final String API_STUDENT_KEY = "f27a96ea-e41b-454d-a4e6-0cc9f2846a8a";
    private static final int CHOCOLATE_LOADER_ID = 1;
    private ChocolateAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the ListView in the layout
        ListView listView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input and set it on the ListView
        mAdapter = new ChocolateAdapter(this, new ArrayList <ChocoArticle>());
        listView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {

                ChocoArticle currentChocoArticle = mAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentChocoArticle.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(CHOCOLATE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader <List <ChocoArticle>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String displaySection = sharedPrefs.getString(
                getString(R.string.settings_display_section_key),
                getString(R.string.settings_display_section_default));


        String orderByTime = sharedPrefs.getString(
                getString(R.string.settings_order_time_key),
                getString(R.string.settings_order_time_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value
        uriBuilder.appendQueryParameter(getString(R.string.query_parameter_format), JSON);
        uriBuilder.appendQueryParameter(getString(R.string.query_parameter_q), getString(R.string.query_entry_chocolate));
        uriBuilder.appendQueryParameter(getString(R.string.query_parameter_show_tags), getString(R.string.query_entry_contributor));
        uriBuilder.appendQueryParameter(getString(R.string.query_parameter_orderby), orderByTime);

        // display selected section if its other than "All"
        if (!displaySection.equals(getString(R.string.settings_section_all_value))) {
            uriBuilder.appendQueryParameter(getString(R.string.query_parameter_section), displaySection);
        }
        // access key from the Guardian API website
        uriBuilder.appendQueryParameter(getString(R.string.api_key), API_STUDENT_KEY);
        // Return the completed uri
        return new ChocolateLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader <List <ChocoArticle>> loader, List <ChocoArticle> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous data
        mAdapter.clear();

        // If there is a valid list of articles, then add them to the adapter's data set.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader <List <ChocoArticle>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    // Initialize the contents of the Activity's options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // This method is called whenever an item in the options menu is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
