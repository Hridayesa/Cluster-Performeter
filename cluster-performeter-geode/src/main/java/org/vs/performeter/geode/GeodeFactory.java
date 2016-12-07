//package org.vs.performeter.geode;
//
//
//import org.apache.geode.cache.Region;
//import org.apache.geode.cache.client.ClientCache;
//import org.apache.geode.cache.client.ClientCacheFactory;
//import org.apache.geode.cache.client.ClientRegionShortcut;
//
///**
// * Created by Denis Karpov on 09.11.2015.
// */
//public class GeodeFactory {
//    public static ClientCache createClientCache(String host, int port){
//        ClientCache cache = new ClientCacheFactory()
//                .addPoolLocator(host, port)
//                .create();
//        return cache;
//    }
//
//    public static Region<String, Integer> createRegion(ClientCache cache, ClientRegionShortcut regionType, String name){
//        Region<String, Integer> region = cache
//                .<String, Integer>createClientRegionFactory(regionType)
//                .create(name);
//        return region;
//    }
//}
