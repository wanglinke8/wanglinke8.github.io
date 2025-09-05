package com.nsc.ipfind.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpInfo {
    private String country;    // 国家
    private String region;     // 地区
    private String province;   // 省份
    private String city;       // 城市
    private String isp;        // 运营商
}
