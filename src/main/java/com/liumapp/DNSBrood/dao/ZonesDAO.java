package com.liumapp.DNSBrood.dao;

import com.liumapp.DNSBrood.model.Zones;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface ZonesDAO {

    @Select("select * from zones where domain=#{domain}")
    public Zones getByDomain(@Param("domain") String domain);

    @Insert("insert into zones (`domain` , `value` , `type` , `createTime` , `updateTime`) values " +
            " (#{domain} , #{value} , #{type} , #{createTime} , #{updateTime})")
    public int insert(Zones zone);

    @Update("update zones set `domain`=#{domain} , `value`=#{value} ," +
            " `type`=#{type} , `createTime`=#{createTime} , `updateTime`=#{updateTime}")
    public int update(Zones zone);

    @Delete("delete from zones where id=#{id}")
    public int delete(Zones zone);

}
