# Redis. Distributed counters.

```java
    int intKey = rn.nextInt(maxNumberOfCacheElements);
    String key = Integer.toString(intKey);

    // atomic operation
    hashOperations.increment("QQQ",key,1);
```