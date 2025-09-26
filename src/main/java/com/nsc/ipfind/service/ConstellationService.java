package com.nsc.ipfind.service;

import com.nsc.ipfind.xingzuo.ConstellationResponse;

// ConstellationService.java (接口)
public interface ConstellationService {

    /**
     * 获取星座完整信息
     */
    ConstellationResponse getConstellationInfo(String constellation);

    /**
     * 获取星座特定类型的运势信息
     */
    Object getSpecificConstellationInfo(String constellation, String type);
}
