package com.liumapp.DNSBrood.service;

import com.liumapp.DNSBrood.model.Zones;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface ZonesService {

    public int addZones(Zones zone);

    public int updateZones(Zones zone);

    public boolean isExist(Zones zone);

    public Zones getZone (String domain);

    public Zones[] getZonesByUserNumber (String userNumber);

}
