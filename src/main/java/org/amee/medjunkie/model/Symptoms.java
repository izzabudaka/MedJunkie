package org.amee.medjunkie.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Home on 06/09/15.
 */
public class Symptoms extends Category{

    private static AtomicLong uniqueId = new AtomicLong();

    public Symptoms(String chapter, String sectionTitle, String text) {
        this(uniqueId.getAndIncrement(), chapter, sectionTitle, text);
    }

    public Symptoms(Long id, String chapter, String sectionTitle, String text) {
        super(id, chapter, sectionTitle, text);

    }

    public static Symptoms newInstance(String chapter, String sectionTitle, String text) {
        return new Symptoms(chapter, sectionTitle, text);
    }
}
