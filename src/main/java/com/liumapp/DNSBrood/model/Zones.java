package com.liumapp.DNSBrood.model;

/**
 * Created by liumapp on 7/19/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class Zones {

    private int id;

    private String domain;

    private String value;

    private String type;

    private int createTime;

    private int updateTime;

    public int getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Zones{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
