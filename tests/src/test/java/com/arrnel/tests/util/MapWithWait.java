package com.arrnel.tests.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class MapWithWait<K, V> {

    private final Map<K, SyncObject> store = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        store.computeIfAbsent(key, SyncObject::new)
                .put(value);
    }

    @Nullable
    public Set<Map.Entry<K, MapWithWait<K,V>.SyncObject>> entrySet(){
        return store.entrySet();
    }

    @Nonnull
    public Collection<SyncObject> values(){
        return store.values();
    }

    @Nullable
    public V get(K key, Duration maxWaitTime) throws InterruptedException {
        SyncObject syncObject = store.computeIfAbsent(key, SyncObject::new);
        return syncObject.latch.await(maxWaitTime.getSeconds(), TimeUnit.SECONDS)
                ? syncObject.value
                : null;
    }

    public void remove(K key) {
        store.remove(key);
    }

    public final class SyncObject {

        private final CountDownLatch latch;
        private final K key;
        private V value;

        public SyncObject(K key) {
            this.key = key;
            this.latch = new CountDownLatch(1);
        }

        public synchronized void put(V value) {
            if (latch.getCount() != 0L) {
                this.value = value;
                this.latch.countDown();
            }
        }
        
    }

}