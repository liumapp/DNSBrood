package com.liumapp.DNSBrood.cache;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface CacheClient {

    /**
     * If "cache" configure is turned off, it can get the elemets already in
     * cache.
     *
     * @param key
     * @param value
     * @param expireTime
     *            in s
     * @return
     */
    <T> boolean set(String key, T value, int expireTime);

    /**
     * If "cache" configure is turned off, it can't be setted and return false.
     *
     * @param key
     * @return
     */
    <T> T get(String key);

    /**
     * init cache
     *
     * @throws Exception
     */
    void init() throws Exception;

    void clearCache();

}
