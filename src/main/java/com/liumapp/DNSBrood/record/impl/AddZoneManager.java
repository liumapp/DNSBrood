package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
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
 */
@Service
public class AddZoneManager implements Manager {

    @Autowired
    private Configure configure;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Override
    public String handle(String whatQueenSays) {

        /**
         * add zone record
         */
        String line = StringUtils.removeStart(whatQueenSays,configure.getAddZonesIp());

        try {

            ZonesPattern zonesPattern = ZonesPattern.parse(line);

            if (zonesPattern == null) {
                return "PARSE ERROR";
            }

            Zones zone = new Zones(zonesPattern.getUserIp(),zonesPattern.getTexts().toString() , zonesPattern.getTargetIp() , "A" , new Date().getTime() , new Date().getTime());

            /**
             * save to db
             */
            if (!zonesService.isExist(zone)) {
                zonesService.addZones(zone);
            }

            /**
             * put to dnsjava
             */
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

    }

}

