package org.amee.medjunkie.model;

/**
 * Created by tuffle on 05/09/2015.
 */
public class Category {

    private final Long id;
    private final String chapter;
    private final String sectionTitle;
    private final String text;
    private String[] textTokens;

    public String getText() {
        return text;
    }

    public String[] getTextTokens() {
        return textTokens;
    }

    public void setTextTokens(String[] tokens) {
        textTokens = tokens;
    }

    public String[] getTokens(String text) {
        return text.split("\\. ");
    }

    public enum CategoryType {
        HINTS,
        //FIGURES,
        QUESTIONS,
        IMAGES,
        SYMPTOMS;

        public static String[] names() {
            CategoryType[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].name();
            }

            return names;
        }

    }

    public Category(Long id, String chapter, String sectionTitle, String text) {

        this.id = id;
        this.chapter = chapter;
        this.sectionTitle = sectionTitle;
        this.text = text;
        this.textTokens = getTokens(text);
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getChapter() {
        return chapter;
    }

    public Long getId() {
        return id;
    }
}
