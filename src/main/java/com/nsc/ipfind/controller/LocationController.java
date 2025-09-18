package com.nsc.ipfind.controller;

import com.nsc.ipfind.pojos.LocationResults;
import com.nsc.ipfind.service.LocationResultsService;
import com.nsc.ipfind.service.impl.LocationService;
import com.nsc.ipfind.dto.LocationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 位置定位REST控制器
 * 提供获取当前城市信息的接口服务
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationResultsService locationResultsService;
    /**
     * 获取当前访问者的城市信息
     * 一键调用即可获得详细的地理位置信息
     *
     * @param request HTTP请求对象，用于获取客户端IP地址
     * @return 包含城市信息的LocationResult对象
     */

    @GetMapping("/current")
    public LocationResults getCurrentCity(HttpServletRequest request) {
        try {
            // 获取客户端IP地址
            String clientIp = getClientIpAddress(request);

            // 调用服务层获取位置信息
            LocationResult result = locationService.getLocationByIp(clientIp);

            // 设置响应信息
            result.setMessage("成功获取当前城市信息");
            result.setSuccess(true);
            LocationResults locationResults = new LocationResults();
            locationResults.setClientIp(result.getClientIp());
            locationResults.setPublicIp(result.getPublicIp());
            locationResults.setCity(result.getCity());
            locationResults.setProvince(result.getProvince());
            locationResults.setCountry(result.getCountry());
            locationResults.setIsp(result.getIsp());
            locationResults.setRawLocation(result.getRawLocation());
            locationResults.setSuccess(result.isSuccess()?1:0);
            //把LocalDateTime类型转为Date类型
            locationResults.setTimestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            locationResultsService.save(locationResults);
            System.out.println(locationResults);
            if (locationResults.getCity().equals("获取失败")){
                return locationResults;
            }else {
                return locationResults;
            }
        } catch (Exception e) {
            // 异常处理，返回错误信息
            LocationResult errorResult = new LocationResult();
            errorResult.setSuccess(false);
            errorResult.setMessage("获取城市信息失败：" + e.getMessage());
            return null;
        }
    }

    /**
     * 从HTTP请求中提取客户端真实IP地址
     * 考虑代理服务器和负载均衡器的情况
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }


        return ipAddress;
    }

}

