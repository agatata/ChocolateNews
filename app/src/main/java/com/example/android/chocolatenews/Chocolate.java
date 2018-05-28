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

/**
 * A Chocolate object contains information related to a single chocolate article.
 */
public class Chocolate {

    /** Title of the article */
    private String mTitle;

    /** Section of the article */
    private String mSection;

    /** Date of publication */
    private String mPublicationDate;

    /** Website URL of the earthquake */
    private String mUrl;

    /**
     * Constructs a new Chocolate object.
     *
     * @param title is the title of an article
     * @param section is the section of an article
     * @param publicationDate is the date of publication
     * @param url is the website URL to read the whole article
     */
    public Chocolate (String title, String section, String publicationDate, String url) {
        mTitle = title;
        mSection = section;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    /**
     * Return the the proper data
     */
    public String getTitle() {
        return mTitle;
    }
    public String getSection() {
        return mSection;
    }
    public String getPublicationDate() {
        return mPublicationDate;
    }
    public String getUrl() {
        return mUrl;
    }
}
