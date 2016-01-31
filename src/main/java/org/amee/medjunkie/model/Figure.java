package org.amee.medjunkie.model;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuffle on 05/09/2015.
 */
public class Figure extends Category {

    private final String link;
    private static AtomicLong uniqueId = new AtomicLong();


    public Figure(String chapter, String sectionTitle, String text) {
        this(uniqueId.getAndIncrement(), chapter, sectionTitle, text);
    }

    public Figure(Long id, String chapter, String sectionTitle, String link) {
        super(id, chapter, sectionTitle, link);

        this.link = link;
    }


    public String getLink() {
        return link;
    }

    public static Figure newInstance(String chapter, String sectionTitle, String text) {
        return new Figure(chapter, sectionTitle, text);
    }
}
