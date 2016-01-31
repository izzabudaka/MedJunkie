package org.amee.medjunkie.resource;

/**
 * Created by tuffle on 31/08/2015.
 */
public class TransactionFailedException extends Exception {
    public TransactionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
