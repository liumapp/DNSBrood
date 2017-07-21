package com.liumapp.DNSBrood.config;

import com.liumapp.DNSBrood.answer.provider.CustomAnswerPatternProvider;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import com.liumapp.DNSBrood.service.impl.ZonesServiceImpl;
import com.liumapp.DNSBrood.utils.SpringLocator;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by liumapp on 7/21/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class ZonesFileRefresherTest extends TestCase {

    private ZonesService zonesService;

    private CustomAnswerPatternProvider customAnswerPatternProvider;

    @Test
    public void testDoWhatYouShouldDo () {

        String whatQueenSays = "add_zones_ip_127.0.0.1:6.7.4.5_gmail.liumapp.com";

        String line = StringUtils.removeStart(whatQueenSays, ZonesFileRefresher.getAddZonesIp());

        try {

            ZonesPattern zonesPattern = ZonesPattern.parse(line);

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


        } catch (UnknownHostException e) {

        }

    }

    @Override
    protected void setUp() throws Exception {
        SpringLocator.applicationContext = new ClassPathXmlApplicationContext("classpath*:/test/applicationContext*.xml");
        zonesService = (ZonesService) SpringLocator.getBean(ZonesService.class);
        customAnswerPatternProvider = (CustomAnswerPatternProvider)SpringLocator.getBean(CustomAnswerPatternProvider.class);
    }

}
