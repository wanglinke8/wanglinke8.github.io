package com.nsc.ipfind.service.impl;

import com.nsc.ipfind.service.ConstellationService;
import com.nsc.ipfind.xingzuo.ConstellationData;
import com.nsc.ipfind.xingzuo.ConstellationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// ConstellationServiceImpl.java (实现类)
@Service
@Slf4j
public class ConstellationServiceImpl implements ConstellationService {

    private static final String API_URL = "https://www.52api.cn/api/constellation";
    private static final String API_KEY = "ViiJow0hebIX8vtai4hZqoNwDa"; // 你的API key

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ConstellationResponse getConstellationInfo(String constellation) {
        try {
            String url = API_URL + "?key=" + API_KEY;
            ResponseEntity<ConstellationResponse> response = restTemplate.getForEntity(url, ConstellationResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ConstellationResponse apiResponse = response.getBody();
                if (apiResponse.getCode() == 200) {
                    return apiResponse;
                } else {
                    throw new RuntimeException("API调用失败: " + apiResponse.getMsg());
                }
            } else {
                throw new RuntimeException("HTTP请求失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("查询星座运势失败", e);
            throw new RuntimeException("查询星座运势失败: " + e.getMessage());
        }
    }

    @Override
    public Object getSpecificConstellationInfo(String constellation, String type) {
        ConstellationResponse response = getConstellationInfo(constellation);

        if (response.getData() != null && response.getData().containsKey(constellation)) {
            ConstellationData constellationData = response.getData().get(constellation);

            switch (type.toLowerCase()) {
                case "today":
                    return constellationData.getDetail().getToday();
                case "tomorrow":
                    return constellationData.getDetail().getTomorrow();
                case "weeks":
                    return constellationData.getDetail().getWeeks();
                case "month":
                    return constellationData.getDetail().getMonth();
                case "years":
                    return constellationData.getDetail().getYears();
                default:
                    return constellationData;
            }
        }
        return null;
    }
}
