package org.amee.medjunkie.model;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuffle on 05/09/2015.
 */
public class Book {
    private ArrayList<Hint> hints = new ArrayList<>();
    private ArrayList<Figure> images = new ArrayList<>();
    private String title = "";
    private ArrayList<Symptoms> syms = new ArrayList<>();
    private String searchTerm;
    private Long bookId;

    private static AtomicLong uniqueId = new AtomicLong();

    public Book(String title, ArrayList<Hint> hints,
                ArrayList<Symptoms> syms, ArrayList<Figure> images) {

        this.title = title;
        this.hints = hints;
        this.syms = syms;
        this.images = images;
    }
    public Book() {
        this(uniqueId.getAndIncrement(), "SearchTerm");
    }

    public Book(Long bookId, String searchTerm) {
        Validate.notNull(bookId, "bookId cannot be null");
        Validate.notNull(searchTerm, "searchTerm cannot be null");

        this.bookId = bookId;
        this.searchTerm = searchTerm;
    }

    public Book(String testBook) {
        this(uniqueId.getAndIncrement(), testBook);
    }

    public Long getBookId() {
        return bookId;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 31) // arbitrary prime numbers
                .append(bookId)
                .append(searchTerm)
                .toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;

        if (!(object instanceof Book))
            return false;

        Book compare = (Book) object;
        return new EqualsBuilder()
                .append(bookId, compare.getBookId())
                .append(searchTerm, compare.getSearchTerm())
                .isEquals();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bookId", bookId)
                .add("searchTerm", searchTerm)
                .toString();
    }

    public ArrayList<Figure> getImages() {
        return images;
    }

    public ArrayList<Hint> getHints() {
        return hints;
    }

    public ArrayList<Symptoms> getSyms() {
        return syms;
    }

    public String getTitle() {
        return title;
    }
}
