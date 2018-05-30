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
     * @param context       of the app
     * @param chocoArticles is the list of articles, which is the data source of the adapter
     */
    ChocolateAdapter(Context context, List <ChocoArticle> chocoArticles) {
        super(context, 0, chocoArticles);
    }

    /**
     * Return a list item view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chocolate_list_item, parent, false);
            holder = new ViewHolder();
            holder.titleView = listItemView.findViewById(R.id.title_text_view);
            holder.sectionView = listItemView.findViewById(R.id.section_text_view);
            holder.dateView = listItemView.findViewById(R.id.date_text_view);
            holder.authorView = listItemView.findViewById(R.id.author_text_view);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Find the article at the given position in the list
        ChocoArticle currentChocoArticle = getItem(position);

        // display appropriate data
        holder.titleView.setText(currentChocoArticle.getTitle());
        holder.sectionView.setText(currentChocoArticle.getSection());
        holder.dateView.setText(currentChocoArticle.getPublicationDate());

        if (currentChocoArticle.getAuthor() != null && currentChocoArticle.getAuthor().size() > 0) {
            String tag = currentChocoArticle.getAuthor().get(0).toString(); //Get the first Author
            holder.authorView.setText(tag);
        } else {
            holder.authorView.setVisibility(View.GONE);
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    private static class ViewHolder {
        private TextView titleView;
        private TextView sectionView;
        private TextView dateView;
        private TextView authorView;
    }
}
