package com.liumapp.DNSBrood.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * 当修改过dnsbrood.conf之后，将新的内容同步到程序中。
 */
@Component
public class ConfigFileRefresher implements InitializingBean {

    @Autowired
    private ConfigFileLoader configFileLoader;

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
        File configFile = new File(Configure.getConfigFilename());
        lastFileModifiedTime = configFile.lastModified();

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                File configFile = new File(Configure.getConfigFilename());
                // When two files' last modify time not equal, we consider it is
                // changed.
                synchronized (this) {
                    if (configFile.lastModified() != lastFileModifiedTime) {
                        lastFileModifiedTime = configFile.lastModified();
                        configFileLoader.reload();
                    }
                }
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

}
