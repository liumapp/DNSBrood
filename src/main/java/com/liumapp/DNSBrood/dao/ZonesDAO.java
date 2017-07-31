package com.liumapp.DNSBrood.dao;

import com.liumapp.DNSBrood.model.Zones;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public interface ZonesDAO {

    @Select("select * from zones where domain=#{domain} and userNumber=#{userNumber}")
    public Zones getVerified(@Param("domain") String domain , @Param("userNumber") String userNumber);

    @Select("select * from zones where domain=#{domain}")
    public Zones getByDomain(@Param("domain") String domain);

    @Select("select * from zones where userNumber=#{userNumber}")
    @Results({
        @Result(property = "id" , column = "id"),
        @Result(property = "userNumber" , column = "userNumber"),
        @Result(property = "domain" , column = "domain"),
        @Result(property = "value" , column = "value"),
        @Result(property = "type" , column = "type"),
        @Result(property = "createTime" , column = "createTime"),
        @Result(property = "updateTime" , column = "updateTime")
    })
    public List<Zones> getByUserNumber(@Param("userNumber") String userNumber);

    @Insert("insert into zones ( `userNumber`,`domain` , `value` , `type` , `createTime` , `updateTime`) values " +
            " (#{userNumber} , #{domain} , #{value} , #{type} , #{createTime} , #{updateTime})")
    public int insert(Zones zone);

    @Update("update zones set `userNumber`=#{userNumber} , `domain`=#{domain} , `value`=#{value} ," +
            " `type`=#{type} , `createTime`=#{createTime} , `updateTime`=#{updateTime} where id=#{id}")
    public int update(Zones zone);

    @Delete("delete from zones where id=#{id}")
    public int delete(Zones zone);

    @Delete("delete from zones where id=#{id} and userNumber=#{userNumber}")
    public int verifyDelete(Zones zone);

}
