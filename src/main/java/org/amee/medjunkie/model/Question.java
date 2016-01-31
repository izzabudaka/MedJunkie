package org.amee.medjunkie.model;

import java.util.List;

/**
 * Created by tuffle on 06/09/2015.
 */
public class Question extends Category{

    private final String question;
    private final List<MultipleChoiceOption> options;

    public Question(String question, List<MultipleChoiceOption> options) {
        super(0L, "", "", "");

        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public List<MultipleChoiceOption> getOptions() {
        return options;
    }

    public static Question newInstance(String question, List<MultipleChoiceOption> options) {
        return new Question(question, options);
    }
}
