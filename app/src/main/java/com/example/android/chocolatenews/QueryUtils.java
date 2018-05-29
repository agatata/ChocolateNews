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

import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor
     */
    private QueryUtils() {
    }

    // Query the USGS dataset and return a list of ChocoArticle objects.

    public static List <ChocoArticle> fetchChocolateData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract and return relevant fields from the JSON response and create a list of chocolate Articles
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of ChocoArticle objects that has been built up from
     * parsing the given JSON response.
     */
    private static List <ChocoArticle> extractFeatureFromJson(String chocolateJSON) {

        if (TextUtils.isEmpty(chocolateJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding chocolate articles to
        List <ChocoArticle> chocoArticles = new ArrayList <>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(chocolateJSON);

            // Extract a JSONObject associated with the key called "response"
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of articles about chocolate.
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            // For each earthquake in the earthquakeArray, create an ChocoArticle object
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentArticle = resultsArray.getJSONObject(i);

                // Extract the value for different keys
                String title = currentArticle.getString("webTitle");
                String sectionName = currentArticle.getString("sectionName");
                String date = currentArticle.getString("webPublicationDate");
                date = formatDate(date); // date convertion helper

                //An article can have multiple contributor
                List <String> tagList = new ArrayList <String>();

                JSONArray tags = currentArticle.optJSONArray("tags");
                if (tags != null) {
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tag = tags.getJSONObject(j);
                        String author = tag.getString("webTitle");
                        if (author != null) {
                            tagList.add(author);
                        }
                    }
                }
                String url = currentArticle.getString("webUrl");

                // Create a new ChocoArticle object with the title, section name, date of puclication
                // and url from the JSON response.
                ChocoArticle chocolate = new ChocoArticle(title, sectionName, date, tagList, url);

                // Add the new chocolate article to the list.
                chocoArticles.add(chocolate);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of articles
        return chocoArticles;
    }
    /**
     * Date Helper thanks to
     * https://github.com/angwandi/NewsAppABNSGU
     */
    private static String formatDate(String dateData) {
        String guardianJsonDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonDateFormatter = new SimpleDateFormat(guardianJsonDateFormat, Locale.ENGLISH);
        try {
            Date jsonDateToParse = jsonDateFormatter.parse(dateData);
            String resultDate = "MMM d, yyy";
            SimpleDateFormat resultDateFormatter = new SimpleDateFormat(resultDate, Locale.ENGLISH);
            return resultDateFormatter.format(jsonDateToParse);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error Formatting the Json Date", e);
            return "";
        }
    }

}
