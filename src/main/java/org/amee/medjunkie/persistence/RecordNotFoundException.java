package org.amee.medjunkie.persistence;

/**
 * Created by tuffle on 05/09/2015.
 */
public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(Long identifier) {
        super("No record present with id #" + identifier);
    }
}