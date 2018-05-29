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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ChocolateAdapter extends ArrayAdapter <ChocoArticle> {

    /**
     * Construct a new ChocolateAdapter.
     *
     * @param context of the app
     * @param chocoArticles is the list of articles, which is the data source of the adapter
     */
    public ChocolateAdapter(Context context, List <ChocoArticle> chocoArticles) {
        super(context, 0, chocoArticles);
    }

    /**
     * Return a list item view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chocolate_list_item, parent, false);
        }
        // Find the article at the given position in the list
        ChocoArticle currentChocoArticle = getItem(position);

        // Find all the TextViews and display appropriate data
        TextView titleView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleView.setText(currentChocoArticle.getTitle());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_text_view);
        sectionView.setText(currentChocoArticle.getSection());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateView.setText(currentChocoArticle.getPublicationDate());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
