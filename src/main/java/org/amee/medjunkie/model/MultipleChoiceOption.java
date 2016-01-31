package org.amee.medjunkie.model;

/**
 * Created by tuffle on 06/09/2015.
 */
public class MultipleChoiceOption {

    private final String option;
    private final boolean isAnswer;

    public MultipleChoiceOption(String option, boolean isAnswer) {

        this.option = option;
        this.isAnswer = isAnswer;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public String getOption() {
        return option;
    }
}
