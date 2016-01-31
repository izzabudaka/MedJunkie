package org.amee.medjunkie.persistence;

/**
 * Created by tuffle on 05/09/2015.
 */
public class ObtainLockFailedException extends Exception {
    public ObtainLockFailedException(Long id) {
        super("Failed to obtain the lock #" + id);
    }
}
