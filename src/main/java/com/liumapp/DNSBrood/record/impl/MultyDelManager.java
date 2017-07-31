package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import com.liumapp.DNSBrood.utils.RecordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * 暂时不考虑通过正则表达式添加的记录
 * del all dns records within specify userNumber : delete_zones_ip_LM0000000
 */
@Service("MultyDelManager")
public class MultyDelManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    /**
     * remove all records according userNumber
     * @param whatQueenSays
     * @return
     */
    @Override
    public String handle(String whatQueenSays) {

        String userNumber = StringUtils.removeStart(whatQueenSays,configure.getDeleteZonesIp());

        DoubleKeyMap<String , String , String> domainTexts = customAnswerPatternProvider.getDomainTexts();

        /**
         * get data info from database by userNumber
         */
        List<Zones> zones = zonesService.getZonesByUserNumber(userNumber);

        for (Zones zone : zones) {

            if (domainTexts.get(zone.getDomain()) == null) continue;

            /**
             * delete data from dnsJava
             */
            if (domainTexts.get(zone.getDomain() , "userNumber").equals(zone.getUserNumber())) {
                domainTexts.remove(zone.getDomain());
            } else {
                return "ERROR: " + zone.getDomain() + "'s userNumber is not the same with your database's userNumber";
            }

            /**
             * delete data from database
             */
            zonesService.deleteZone(zone);
        }

        return "success";
    }

}
