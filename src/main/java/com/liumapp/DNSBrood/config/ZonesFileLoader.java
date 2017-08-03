package com.liumapp.DNSBrood.config;

import com.liumapp.DNSBrood.answer.container.DomainPatternsContainer;
import com.liumapp.DNSBrood.answer.provider.AnswerPatternProvider;
import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.cache.CacheManager;
import com.liumapp.DNSBrood.forward.DNSHostsContainer;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import com.liumapp.DNSQueen.worker.job.ReloadAble;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/15/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * 写在 zones file的记录，其userNumber将被标识为LM
 * zones file 一行只允许出现一条解析记录
 */
@Component
public class ZonesFileLoader implements InitializingBean, ReloadAble {

    @Autowired
    private Configure configure;

    @Autowired
    private AnswerPatternProvider answerPatternContainer;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Autowired
    private DNSHostsContainer dnsHostsContainer;

    @Autowired
    private ZonesService zonesService;

    private Logger logger = Logger.getLogger(getClass());

    public void readConfig(String filename) {

        try {
            DomainPatternsContainer domainPatternsContainer = new DomainPatternsContainer();
            DomainPatternsContainer nsDomainPatternContainer = new DomainPatternsContainer();
            DoubleKeyMap<String, Pattern, String> customAnswerPatternsTemp = new DoubleKeyMap<String, Pattern, String>(
                    new ConcurrentHashMap<String, Map<Pattern, String>>(), LinkedHashMap.class);
            DoubleKeyMap<String, String, String> customAnswerTextsTemp = customAnswerPatternProvider.getDomainTexts();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                ZonesPattern zonesPattern = ZonesPattern.parse(line);
                if (zonesPattern == null) {
                    continue;
                }
                try {

                    for (String text : zonesPattern.getTexts()) {
                        customAnswerTextsTemp.put(text, "ip", zonesPattern.getTargetIp());
                        customAnswerTextsTemp.put(text, "userNumber" , "LM");
                    }

                    logger.info("read config success:\t" + line);

                } catch (Exception e) {

                    logger.warn("parse config line error:\t" + line + "\t" , e);

                }

                //save to db
                Zones zone = new Zones(zonesPattern.getUserNumber() , zonesPattern.getTexts().get(0) , zonesPattern.getTargetIp() , "A" , new Date().getTime() , new Date().getTime());

                if (!zonesService.isExist(zone)) {
                    zonesService.addZones(zone);
                }

                // For NS
                if (line.startsWith("NS")) {
                    line = StringUtils.removeStartIgnoreCase(line, "NS").trim();
                    zonesPattern = ZonesPattern.parse(line);
                    if (zonesPattern == null) {
                        continue;
                    }
                    try {
                        for (Pattern pattern : zonesPattern.getPatterns()) {
                            nsDomainPatternContainer.getDomainPatterns().put(pattern,zonesPattern.getTargetIp());
                        }
                        for (String text : zonesPattern.getTexts()) {
                            nsDomainPatternContainer.getDomainTexts().put(text,zonesPattern.getTargetIp());
                        }
                        logger.info("read config success:\t" + line);
                    } catch (Exception e) {
                        logger.warn("parse config line error:\t" + line + "\t" + e);
                    }
                }
            }
            answerPatternContainer.setDomainPatternsContainer(domainPatternsContainer);
            customAnswerPatternProvider.setDomainPatterns(customAnswerPatternsTemp);
            customAnswerPatternProvider.setDomainTexts(customAnswerTextsTemp);
            dnsHostsContainer.setDomainPatternsContainer(nsDomainPatternContainer);
            bufferedReader.close();
        } catch (Throwable e) {
            logger.warn("read config file failed:" + filename, e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.liumapp.DNSQueen.worker.job.ReloadAble#reload()
     */
    @Override
    public void reload() {
        readConfig(Configure.getZonesFilename());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        reload();
    }

}
