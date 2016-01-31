package org.amee.medjunkie.persistence;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tuffle on 05/09/2015.
 */
public abstract class GenericStore<T> {

    private static final int LOCK_TIMEOUT_SECONDS = 5;

    private Map<Long, T> genericStoreMap;
    private Map<Long, ReentrantLock> genericLockMap;

    public GenericStore() {
        genericStoreMap = new ConcurrentHashMap<>();
        genericLockMap = new ConcurrentHashMap<>();
    }

    public T get(Long genericId) throws RecordNotFoundException {
        T record = genericStoreMap.get(genericId);

        if(record == null)
            throw new RecordNotFoundException(genericId);

        return record;
    }

    public T add(Long genericId, T record) {
        genericStoreMap.put(genericId, record);
        genericLockMap.put(genericId, new ReentrantLock(true));

        return record;
    }

    public void lock(Long id) throws RecordNotFoundException, ObtainLockFailedException {
        Lock lock = genericLockMap.get(id);

        if(lock == null)
            throw new RecordNotFoundException(id);

        try {
            if(!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                throw new ObtainLockFailedException(id);
        } catch (InterruptedException e) {
            throw new ObtainLockFailedException(id);
        }
    }

    public void unlock(Long id) {
        ReentrantLock lock = genericLockMap.get(id);

        if(lock != null && lock.isHeldByCurrentThread())
            lock.unlock();
    }

    public T remove(Long genericId) {
        return genericStoreMap.remove(genericId);
    }

    public Collection<T> getAllRecords() {
        return genericStoreMap.values();
    }
}
