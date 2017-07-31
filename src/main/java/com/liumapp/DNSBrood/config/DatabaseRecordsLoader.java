package com.liumapp.DNSBrood.config;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import com.liumapp.DNSQueen.worker.job.ReloadAble;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liumapp on 7/31/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Component
public class DatabaseRecordsLoader implements InitializingBean , ReloadAble {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    public void readDatabase() {

        List<Zones> zones = zonesService.findAll();

        DoubleKeyMap<String , String , String> domainTexts = customAnswerPatternProvider.getDomainTexts();

        if (zones == null) return ;

        for (Zones zone : zones) {
            logger.info("read " + zone.getDomain() + "  " + zone.getValue() + " from database");
            if (domainTexts.get(zone.getDomain()) != null) {
                logger.info("read " + zone.getDomain() + " failed , because it was already exists in map");
            } else {
                domainTexts.put(zone.getDomain() , "ip" , zone.getValue());
                domainTexts.put(zone.getDomain() , "userNumber" , zone.getUserNumber());
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        reload();
    }

    @Override
    public void reload() {
        readDatabase();
    }
}
