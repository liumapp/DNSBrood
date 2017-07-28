package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.DoubleKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/24/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 * del specify records : delete_zones_ip_LM0000000:4.5.6.7 gmail.liumapp.com
 */
@Service("DelZoneManager")
public class DelZoneManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Override
    public String handle(String whatQueenSays) {

        String line = StringUtils.removeStart(whatQueenSays,configure.getDeleteZonesIp());

        DoubleKeyMap<String , String , String> domainTexts = customAnswerPatternProvider.getDomainTexts();

        try {

            ZonesPattern zonesPattern = ZonesPattern.parse(line);

            /**
             * get data info from database by userNumber and domain
             */
            Zones zone = zonesService.getVerifiedZone(zonesPattern.getTexts().get(0) , zonesPattern.getUserNumber());

            /**
             * delete data from dnsJava
             */
            if (domainTexts.get(zone.getDomain() , "userNumber") == zone.getUserNumber()) {
                domainTexts.remove(zone.getDomain());
            } else {
                return "ERROR: " + zone.getDomain() + "'s userNumber is not the same with your database's userNumber";
            }

            /**
             * delete from database
             */
            zonesService.deleteZone(zone);


        } catch (UnknownHostException e) {

            e.printStackTrace();

        }

        return "success";

    }
}
