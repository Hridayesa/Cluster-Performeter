# Hazlecast. Distributed counters with optimistic locking

```java
    int intKey = random.nextInt(maxNamberOfCacheElements);
    String key = Integer.toString(intKey);

    try {
        while (true) {
            Integer oldCount = (Integer) testMap.get(key);
            if (oldCount == null) {
                if (testMap.putIfAbsent(key, 1) == null) {
                    break;
                }
            } else {
                if (testMap.replace(key, oldCount, oldCount + 1)) {
                    break;
                }
            }
            Thread.sleep(10);
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
```