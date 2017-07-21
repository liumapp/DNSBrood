package com.liumapp.DNSBrood.service.impl;

import com.liumapp.DNSBrood.dao.ZonesDAO;
import com.liumapp.DNSBrood.model.Zones;
import com.liumapp.DNSBrood.service.ZonesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Service
public class ZonesServiceImpl implements ZonesService {

    @Resource
    private ZonesDAO zonesDAO;

    @Override
    public int addZones(Zones zone) {
        return zonesDAO.insert(zone);
    }

}
