package org.amee.medjunkie.resource;

import org.amee.medjunkie.model.Book;
import org.amee.medjunkie.persistence.BookStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

/**
 * Created by tuffle on 05/09/2015.
 */

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BooksResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksResource.class);
    private final BookStore bookStore;

    public BooksResource(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    @POST
    public Response createBook(Book book) {
        LOGGER.info("Creating a new book...");

        try {
            // Create the account, and return so that main.xml have the id
            bookStore.add(book);
            LOGGER.info("New book #{} created {}", book.getBookId(), book);

            // Build the URI to return to the client
            URI accountResource = URI.create(String.format("/books/%d", book.getBookId()));

            // Build and return the response
            return Response.created(accountResource)
                    .status(Response.Status.CREATED)
                    .entity(book)
                    .build();
        } catch (Throwable e) {
            LOGGER.error("A book creation error has occurred", e);
            throw new RuntimeException(e);
        }
    }

    @GET
    public Collection<Book> getBooks() {
        Collection<Book> allBooks = bookStore.getAllRecords();
        LOGGER.debug("Returning all books, total = " + allBooks.size());
        return allBooks;
    }
}
