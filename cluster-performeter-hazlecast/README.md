# Hazlecast. Distributed counters with pessimistic locking

```java
    int intKey = rn.nextInt(maxNamberOfCacheElements);
    String key = Integer.toString(intKey);
    testMap.lock(key);
    try {

        Integer limit = (Integer) testMap.get(key);
        limit = (limit == null) ? 1 : limit + 1;
        testMap.set(key, limit);

    } finally {
        testMap.unlock(key);
    }
```
