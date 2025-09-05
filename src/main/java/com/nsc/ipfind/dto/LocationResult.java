package com.nsc.ipfind.dto;

import java.time.LocalDateTime;

/**
 * 位置信息数据传输对象
 * 用于封装IP地址定位查询的结果信息
 */

public class LocationResult {

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 外网IP地址（当客户端为内网IP时使用）
     */
    private String publicIp;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 省份/地区名称
     */
    private String province;

    /**
     * 国家名称
     */
    private String country;

    /**
     * 网络服务提供商
     */
    private String isp;

    /**
     * 原始位置信息字符串
     */
    private String rawLocation;

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 查询时间戳
     */
    private LocalDateTime timestamp;

    public LocationResult() {
        this.timestamp = LocalDateTime.now();
    }

    // Getter和Setter方法

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getRawLocation() {
        return rawLocation;
    }

    public void setRawLocation(String rawLocation) {
        this.rawLocation = rawLocation;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "LocationResult{clientIp='%s', city='%s', province='%s', country='%s', success=%s, message='%s'}",
                clientIp, city, province, country, success, message
        );
    }
}
