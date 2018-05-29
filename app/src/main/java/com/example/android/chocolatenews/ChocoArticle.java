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

import java.util.ArrayList;
import java.util.List;

/**
 * A ChocoArticle object contains information related to a single chocolate article.
 */
public class ChocoArticle {

    /** Title of the article */
    private String artTitle;

    /** Section of the article */
    private String artSection;

    /** Date of publication */
    private String artPublicationDate;

    /**
     * Author of publication
     */
    private List artAuthor;

    /** Website URL of the earthquake */
    private String artUrl;

    /**
     * Constructs a new ChocoArticle object.
     *
     * @param title is the title of an article
     * @param section is the section of an article
     * @param publicationDate is the date of publication
     * @param author is the author of publication
     * @param url is the website URL to read the whole article
     */
    public ChocoArticle(String title, String section, String publicationDate, List author, String url) {
        artTitle = title;
        artSection = section;
        artPublicationDate = publicationDate;
        artAuthor = author;
        artUrl = url;
    }

    /**
     * Return the the proper data
     */
    public String getTitle() {
        return artTitle;
    }
    public String getSection() {
        return artSection;
    }

    public String getPublicationDate() {
        return artPublicationDate;
    }

    public List getAuthor() {
        return artAuthor;
    }
    public String getUrl() {
        return artUrl;
    }
}
