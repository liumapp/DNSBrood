package com.liumapp.DNSBrood.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component("MapCacheClient")
public class MapCacheClient implements CacheClient{

    private Map<String,Object> map = new ConcurrentHashMap<String, Object>();

    @Override
    public <T> boolean set(String key, T value, int expireTime) {
        map.put(key,value);
        return true;
    }

    @Override
    public <T> T get(String key) {
        return (T)map.get(key);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void clearCache() {
        map = new ConcurrentHashMap<String, Object>();
    }
}
