# Ignite. Distributed counters with pessimistic locking

```java
    int key = random.nextInt(maxNamberOfCacheElements);
    
    Lock lock = counter.lock(key);
    lock.lock();
    try {
    
        Integer limit = counter.get(key);
        limit = (limit == null) ? 1 : limit + 1;
        counter.put(key, limit);
    
    } finally {
        lock.unlock();
    }
```