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
 */
@Service("AddZoneManager")
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

        DoubleKeyMap <String , String , String > domainTexts = customAnswerPatternProvider.getDomainTexts();

        try {

            ZonesPattern zonesPattern = ZonesPattern.parse(line);

            if (zonesPattern == null) {
                return "PARSE ERROR";
            }

            Zones zone = new Zones(zonesPattern.getUserNumber(),zonesPattern.getTexts().get(0) , zonesPattern.getTargetIp() , "A" , new Date().getTime() , new Date().getTime());

            /**
             * save to db
             */
            if (!zonesService.isExist(zone)) {
                //不存在则添加到db
                zonesService.addZones(zone);
            } else {
                /**
                 * 已经存在的则执行更新操作
                 * 不过要先检查userNumber是否相等
                 */
                Zones tmp = zonesService.getZone(zonesPattern.getTexts().get(0));
                if (tmp.getUserNumber().equals(zonesPattern.getUserNumber())) {
                    zonesService.updateZones(zone);
                } else {
                    return "ERROR: you have no access to control " + zonesPattern.getTexts().get(0);
                }
            }

            /**
             * put to dnsjava
             */
            for (String text : zonesPattern.getTexts()) {

                if (domainTexts.get(text) != null) {
                    /**
                     * 如果域名已经存在，则判断userNumber
                     */
                    String userNumber = domainTexts.get(text , "userNumber");
                    if (userNumber.equals(zonesPattern.getUserNumber())) {
                        domainTexts.put(text , "ip" , zonesPattern.getTargetIp());
                    } else {
                        return "ERROR : you have no access to control " + text;
                    }
                } else {
                    customAnswerPatternProvider.getDomainTexts().put(text , "ip" , zonesPattern.getTargetIp());
                    customAnswerPatternProvider.getDomainTexts().put(text ,  "userNumber" , zonesPattern.getUserNumber());
                }

            }

            return "SUCCESS, " + zonesPattern.getTexts().size() + " patterns added.";

        } catch (UnknownHostException e) {

            return "ERROR " + e;

        }

    }

}

