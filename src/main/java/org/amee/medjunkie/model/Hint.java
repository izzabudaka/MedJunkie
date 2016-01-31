package org.amee.medjunkie.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuffle on 05/09/2015.
 */
public class Hint extends Category {

    private static AtomicLong uniqueId = new AtomicLong();

    public Hint(String chapter, String sectionTitle, String text) {
        this(uniqueId.getAndIncrement(), chapter, sectionTitle, text);
    }

    public Hint(Long id, String chapter, String sectionTitle, String text) {
        super(id, chapter, sectionTitle, text);
    }

    public static Hint newInstance(String chapter, String sectionTitle, String text) {
        return new Hint(chapter, sectionTitle, text);
    }
}
