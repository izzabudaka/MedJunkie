package org.amee.medjunkie.model;

import java.util.ArrayList;

/**
 * Created by tuffle on 06/09/2015.
 */
public class FinalResult {

    public Category   category;
    public ArrayList<Category> questions;


    public FinalResult(Category category, ArrayList<Category> questions) {

        this.category = category;
        this.questions = questions;
    }

    public FinalResult() {}


}
