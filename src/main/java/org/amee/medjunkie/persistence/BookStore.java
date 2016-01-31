package org.amee.medjunkie.persistence;

import org.amee.medjunkie.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuffle on 05/09/2015.
 */
public class BookStore extends GenericStore<Book> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookStore.class);
    private AtomicLong uniqueId;
    private static BookStore instance = null;

    protected BookStore() {
        super();

        //TODO: Crude implementation for prototype purposes only
        uniqueId = new AtomicLong();
    }

    public static BookStore getInstance() {
        if(instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    public Book add(Book book) {
        return super.add(book.getBookId(), book);
    }

    public Book remove(Long bookId) {
        return super.remove(bookId);
    }

}
