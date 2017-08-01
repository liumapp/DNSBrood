package com.liumapp.DNSBrood.config;

import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by liumapp on 7/14/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class Configure {

    public final static long DEFAULT_TTL = 2000;

    public final static int DEFAULT_DNS_TIMEOUT = 2000;

    public static String FILE_PATH = "/usr/local/DNSBrood/";

    public final static int DEFAULT_MX_PRIORY = 10;

    private long ttl = DEFAULT_TTL;

    private int mxPriory = DEFAULT_MX_PRIORY;

    public final static int DNS_PORT = 53;

    private int dnsTimeOut = DEFAULT_DNS_TIMEOUT;

    private String loggerLevel;

    private boolean enableSafeBox = true;

    private SocketAddress fakeDnsServer;

    private boolean useCache = true;

    private int threadNum = 4;

    private static String configFilename;

    private int cacheExpire;

    public static String getConfigFilename() {
        if (configFilename == null) {
            return Configure.FILE_PATH + "config/DNSBrood.conf";
        } else {
            return configFilename;
        }
    }

    public static void setConfigFilename(String name) {
        configFilename = name;
    }

    /**
     * del all dns records within specify userNumber : delete_zones_ip_LM0000000
     * del specify records : delete_zones_ip_LM0000000:4.5.6.7_gmail.liumapp.com
     */
    private static final String DELETE_ZONES_IP = "delete_zones_ip_";

    //add_zones_ip_LM09000:4.5.6.7 gmail.liumapp.com
    private static final String ADD_ZONES_IP = "add_zones_ip_";

    //update_zones_ip_LM09000:4.5.6.8 gmail.liumapp.com
    private static final String UPDATE_ZONES_IP = "update_zones_ip_";

    /**
     * 正向解析：select_zones_ip_gmail.liumapp.com
     * return : 4.5.6.7
     * 反向解析：select_zones_ip_4.5.6.7
     * return gmail.liumapp.com
     */
    private static final String SELECT_ZONES_IP = "select_zones_ip_";

    public static String getUpdateZonesIp() {
        return UPDATE_ZONES_IP;
    }

    public static String getSelectZonesIp() {
        return SELECT_ZONES_IP;
    }

    public static String getDeleteZonesIp() {
        return DELETE_ZONES_IP;
    }

    public static String getAddZonesIp() {
        return ADD_ZONES_IP;
    }

    private static String zonesFilename;

    public static void setZonesFilename(String name) {
        zonesFilename = name;
    }

    public static String getZonesFilename() {
        if (zonesFilename == null) {
            return Configure.FILE_PATH + "/config/zones";
        } else {
            return zonesFilename;
        }
    }

    public boolean isEnableSafeBox() {
        return enableSafeBox;
    }

    public void setEnableSafeBox(boolean enableSafeBox) {
        this.enableSafeBox = enableSafeBox;
    }

    /**
     * @return the useCache
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     *
     * @return
     */
    public long getTTL() {
        return ttl;
    }

    /**
     * @return the ttl
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * @param ttl
     *            the ttl to set
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * @return the mxPriory
     */
    public int getMxPriory() {
        return mxPriory;
    }

    /**
     * @param mxPriory
     *            the mxPriory to set
     */
    public void setMxPriory(int mxPriory) {
        this.mxPriory = mxPriory;
    }

    /**
     * @return the dnsTimeOut
     */
    public int getDnsTimeOut() {
        return dnsTimeOut;
    }

    /**
     * @return the loggerLevel
     */
    public String getLoggerLevel() {
        return loggerLevel;
    }

    /**
     * @param loggerLevel
     *            the loggerLevel to set
     */
    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    /**
     * @param dnsTimeOut
     *            the dnsTimeOut to set
     */
    public void setDnsTimeOut(int dnsTimeOut) {
        this.dnsTimeOut = dnsTimeOut;
    }

    /**
     * @param useCache
     *            the useCache to set
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public SocketAddress getFakeDnsServer() {
        return fakeDnsServer;
    }

    public void setFakeDnsServer(String fakeDnsServer) {
        this.fakeDnsServer = new InetSocketAddress(fakeDnsServer, DNS_PORT);
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getCacheExpire() {
        return cacheExpire;
    }

    public void setCacheExpire(int cacheExpire) {
        this.cacheExpire = cacheExpire;
    }

}
