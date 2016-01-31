package org.amee.medjunkie.resource;

import io.dropwizard.jersey.params.LongParam;
import org.amee.medjunkie.model.Book;
import org.amee.medjunkie.persistence.BookStore;
import org.amee.medjunkie.persistence.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by tuffle on 05/09/2015.
 */
@Path("/books/{bookId}")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookResource.class);
    private final BookStore bookStore;

    public BookResource(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    @GET
    public Book getDetails(@PathParam("bookId") LongParam bookId) throws RecordNotFoundException {
        LOGGER.info("Getting book details for bookId = " + bookId.get());
        return bookStore.get(bookId.get());
    }
}
