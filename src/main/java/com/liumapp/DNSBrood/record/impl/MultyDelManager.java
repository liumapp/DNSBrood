package com.liumapp.DNSBrood.record.impl;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.config.Configure;
import com.liumapp.DNSBrood.config.ZonesPattern;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.record.Manager;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.utils.RecordUtils;
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

        String line = StringUtils.removeStart(whatQueenSays,configure.getAddZonesIp());

//        try {

            /**
             * get data from database
             */


            /**
             * remove data from dnsjava
             */
//            customAnswerPatternProvider.getDomainTexts().remove("127.0.0.1" , )


            /**
             * save to db
             */
//            if (!zonesService.isExist(zone)) {
//                zonesService.addZones(zone);
//            }
//
//            /**
//             * put to dnsjava
//             */
//            for (Pattern pattern : zonesPattern.getPatterns()) {
//                customAnswerPatternProvider.getDomainPatterns().put(zonesPattern.getUserIp(), pattern, zonesPattern.getTargetIp());
//            }
//
//            for (String text : zonesPattern.getTexts()) {
//                customAnswerPatternProvider.getDomainTexts().put(zonesPattern.getUserIp(), text, zonesPattern.getTargetIp());
//            }
//
//
//        } catch (UnknownHostException e) {
//
//
//        }
//
//        String ip = StringUtils.removeStart(whatQueenSays, configure.getDeleteZonesIp());
//
//        if (RecordUtils.isValidIpv4Address(ip)) {
//            customAnswerPatternProvider.getDomainPatterns().remove(ip);
//            customAnswerPatternProvider.getDomainTexts().remove(ip);
//            return "REMOVE SUCCESS";
//        } else {
//            return "ERROR, invalid ip " + ip;
//        }

        return "success";
    }

}
