package com.liumapp.DNSBrood.config;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.DelZoneManager;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.RecordUtils;
import com.liumapp.DNSQueen.worker.ready.StandReadyWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource(name = "AddZoneManager")
    private Manager addZoneManager;

    @Resource(name = "DelZoneManager")
    private Manager delZoneManager;

    private ScheduledExecutorService scheduledExecutorService = Executors
            .newScheduledThreadPool(1);

    private long lastFileModifiedTime;

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

        if (StringUtils.startsWithIgnoreCase(whatQueenSays, configure.getAddZonesIp())) {

            return addZoneManager.handle(whatQueenSays);

        } else if (StringUtils.startsWithIgnoreCase(whatQueenSays, configure.getDeleteZonesIp())) {

            return delZoneManager.handle(whatQueenSays);

        } else if (StringUtils.startsWithIgnoreCase(whatQueenSays , configure.getUpdateZonesIp())) {

        } else if (StringUtils.startsWithIgnoreCase(whatQueenSays , configure.getSelectZonesIp())) {

        }

        return null;
    }


}
