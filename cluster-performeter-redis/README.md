# Redis. Distributed counters.

```java
    int intKey = rn.nextInt(maxNamberOfCacheElements);
    String key = Integer.toString(intKey);

    // atomic operation
    hashOperations.increment("QQQ",key,1);
```