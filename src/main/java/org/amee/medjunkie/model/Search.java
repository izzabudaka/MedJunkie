package org.amee.medjunkie.model;

import org.amee.medjunkie.model.Category.CategoryType;

/**
 * Created by tuffle on 05/09/2015.
 */
public class Search {

    private String searchTerms;
    private CategoryType categoryType;
    private int durationInMins;

    public Search() {
        // Jackson
    }

    public Search(String searchTerms, int durationInMins) {
        this(searchTerms, CategoryType.HINTS, durationInMins);
    }

    public Search(String searchTerms, CategoryType categoryType, int durationInMins) {

        this.searchTerms = searchTerms;
        this.categoryType = categoryType;
        this.durationInMins = durationInMins;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public int getDurationInMins() {
        return durationInMins;
    }
}
