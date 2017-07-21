package com.liumapp.DNSBrood.config;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.RecordUtils;
import com.liumapp.DNSQueen.worker.ready.StandReadyWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class ZonesFileRefresher extends StandReadyWorker implements InitializingBean {

    @Autowired
    private Configure configure;

    @Autowired
    private ZonesFileLoader zonesFileLoader;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Autowired
    private ZonesService zonesService;

    private ScheduledExecutorService scheduledExecutorService = Executors
            .newScheduledThreadPool(1);

    private long lastFileModifiedTime;

    //delete_zones_ip_127.0.0.1
    private static final String DELETE_ZONES_IP = "delete_zones_ip_";

    //add_zones_ip_127.0.0.1:4.5.6.7 gmail.liumapp.com
    private static final String ADD_ZONES_IP = "add_zones_ip_";

    //update_zones_ip_127.0.0.1:4.5.6.8 gmail.liumapp.com
    private static final String UPDATE_ZONES_IP = "update_zones_ip_";

    /**
     * 正向解析：select_zones_ip_127.0.0.1 gmail.liumapp.com
     * return : 4.5.6.7
     * 反向解析：select_zones_ip_127.0.0.1 4.5.6.7
     * return gmail.liumapp.com
     */
    private static final String SELECT_ZONES_IP = "select_zones_ip_";

    public static String getDeleteZonesIp() {
        return DELETE_ZONES_IP;
    }

    public static String getAddZonesIp() {
        return ADD_ZONES_IP;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        File zonesFile = new File(Configure.getZonesFilename());
        lastFileModifiedTime = zonesFile.lastModified();

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                File zonesFile = new File(Configure.getZonesFilename());
                // When two files' last modify time not equal, we consider it is
                // changed.
                synchronized (this) {
                    if (zonesFile.lastModified() != lastFileModifiedTime) {
                        lastFileModifiedTime = zonesFile.lastModified();
                        zonesFileLoader.reload();
                    }
                }
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public String doWhatYouShouldDo(String whatQueenSays) {
        if (StringUtils.startsWithIgnoreCase(whatQueenSays, ADD_ZONES_IP)) {
            String line = StringUtils.removeStart(whatQueenSays, ADD_ZONES_IP);
            try {

                ZonesPattern zonesPattern = ZonesPattern.parse(line);

                if (zonesPattern == null) {
                    return "PARSE ERROR";
                }

                List<Pattern> patterns = zonesPattern.getPatterns();

                Zones zone = new Zones();
                zone.setType("A");
                zone.setDomain(zonesPattern.getTexts().toString());
                zone.setValue(zonesPattern.getTargetIp());
                zone.setCreateTime(new Date().getTime());
                zone.setUpdateTime(new Date().getTime());
                zonesService.addZones(zone);

                for (Pattern pattern : zonesPattern.getPatterns()) {
                    customAnswerPatternProvider.getDomainPatterns().put(zonesPattern.getUserIp(), pattern, zonesPattern.getTargetIp());
                }

                for (String text : zonesPattern.getTexts()) {
                    customAnswerPatternProvider.getDomainTexts().put(zonesPattern.getUserIp(), text, zonesPattern.getTargetIp());
                }

                return "SUCCESS, " + zonesPattern.getPatterns().size() + " patterns added.";

            } catch (UnknownHostException e) {
                return "ERROR " + e;
            }
        } else if (StringUtils.startsWithIgnoreCase(whatQueenSays, DELETE_ZONES_IP)) {
            String ip = StringUtils.removeStart(whatQueenSays, DELETE_ZONES_IP);
            if (RecordUtils.isValidIpv4Address(ip)) {
                customAnswerPatternProvider.getDomainPatterns().remove(ip);
                customAnswerPatternProvider.getDomainTexts().remove(ip);
                return "REMOVE SUCCESS";
            } else {
                return "ERROR, invalid ip " + ip;
            }
        }

        return null;
    }

}
