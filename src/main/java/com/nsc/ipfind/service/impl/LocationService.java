package com.nsc.ipfind.service.impl;

import com.nsc.ipfind.dto.LocationResult;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 位置定位服务实现类
 * 负责通过IP地址获取地理位置信息的核心业务逻辑
 */
@Service
public class LocationService {

    private Searcher searcher;
    private WebClient webClient;

    @Value("${ip2region.xdb.path:ip2region.xdb}")
    private String xdbPath;

    @PostConstruct
    public void init() {
        try {
            // 初始化IP2Region搜索器
            initializeIp2RegionSearcher();

            // 初始化WebClient用于获取外部IP
            this.webClient = WebClient.builder().build();

        } catch (Exception e) {
            throw new RuntimeException("位置服务初始化失败", e);
        }
    }

    /**
     * 初始化IP2Region搜索器
     * 从类路径加载ip2region.xdb数据库文件
     */
    private void initializeIp2RegionSearcher() throws Exception {
        try {
            // 尝试从类路径加载数据库文件
            ClassPathResource resource = new ClassPathResource("ip2region.xdb");
            InputStream inputStream = resource.getInputStream();

            // Java 8兼容的方式读取字节数组
            byte[] dbBuffer = readInputStreamToByteArray(inputStream);
            inputStream.close();

            this.searcher = Searcher.newWithBuffer(dbBuffer);

        } catch (Exception e) {
            // 如果类路径文件不存在，尝试使用默认配置
            System.err.println("警告：未找到ip2region.xdb文件，将使用模拟数据");
            this.searcher = null;
        }
    }

    /**
     * Java 8兼容的方法：将InputStream读取为字节数组
     * 用于替代Java 9中的readAllBytes()方法
     *
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException IO异常
     */
    private byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

    /**
     * 根据IP地址获取位置信息
     *
     * @param ipAddress 客户端IP地址
     * @return 包含详细位置信息的LocationResult对象
     */
    public LocationResult getLocationByIp(String ipAddress) {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);

        try {
            // 处理本地IP地址
            if (isLocalIpAddress(ipAddress)) {
                result = handleLocalIpAddress(ipAddress);
            } else {
                result = searchLocationByIp(ipAddress);
            }

            return result;

        } catch (Exception e) {
            return createErrorResult(ipAddress, "IP地址解析失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否为本地IP地址
     */
    private boolean isLocalIpAddress(String ipAddress) {
        return ipAddress == null ||
                ipAddress.equals("127.0.0.1") ||
                ipAddress.equals("0:0:0:0:0:0:0:1") ||
                ipAddress.equals("::1") ||
                ipAddress.startsWith("192.168.") ||
                ipAddress.startsWith("10.") ||
                ipAddress.startsWith("172.");
    }

    /**
     * 处理本地IP地址的情况
     * 尝试获取外网IP地址进行定位
     */
    private LocationResult handleLocalIpAddress(String localIp) {
        try {
            // 尝试获取外网IP
            String publicIp = getPublicIpAddress();

            if (publicIp != null && !isLocalIpAddress(publicIp)) {
                LocationResult result = searchLocationByIp(publicIp);
                result.setClientIp(localIp);
                result.setPublicIp(publicIp);
                result.setMessage("检测到本地IP，已自动获取外网IP进行定位");
                return result;
            } else {
                return createLocalResult(localIp);
            }

        } catch (Exception e) {
            return createLocalResult(localIp);
        }
    }

    /**
     * 获取外网IP地址
     */
    private String getPublicIpAddress() {
        try {
            String response = webClient.get()
                    .uri("https://api.ipify.org?format=text")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response != null ? response.trim() : null;

        } catch (Exception e) {
            // 备用服务
            try {
                String response = webClient.get()
                        .uri("https://ifconfig.me/ip")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                return response != null ? response.trim() : null;

            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 使用IP2Region搜索IP位置信息
     */
    private LocationResult searchLocationByIp(String ipAddress) throws Exception {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);

        if (searcher == null) {
            // 模拟数据，用于演示
            return createMockResult(ipAddress);
        }

        try {
            String locationInfo = searcher.search(ipAddress);
            result = parseLocationInfo(locationInfo, ipAddress);

        } catch (Exception e) {
            return createErrorResult(ipAddress, "IP定位查询失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 解析IP2Region返回的位置信息
     * 格式通常为：国家|区域|省份|城市|ISP
     */
    private LocationResult parseLocationInfo(String locationInfo, String ipAddress) {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);

        if (locationInfo == null || locationInfo.isEmpty()) {
            result.setCity("未知城市");
            result.setProvince("未知省份");
            result.setCountry("未知国家");
            result.setMessage("IP地址定位信息不完整");
            return result;
        }

        try {
            String[] parts = locationInfo.split("\\|");

            if (parts.length >= 5) {
                result.setCountry(parts[0].equals("0") ? "中国" : parts[0]);
                result.setProvince(parts[2].equals("0") ? "未知省份" : parts[2]);
                result.setCity(parts[3].equals("0") ? "未知城市" : parts[3]);
                result.setIsp(parts[4].equals("0") ? "未知运营商" : parts[4]);
                result.setRawLocation(locationInfo);
                result.setSuccess(true);
            } else {
                result.setCity("解析失败");
                result.setMessage("位置信息格式异常");
            }

        } catch (Exception e) {
            result.setCity("解析错误");
            result.setMessage("位置信息解析失败：" + e.getMessage());
        }

        return result;
    }

    /**
     * 创建本地IP的结果对象
     */
    private LocationResult createLocalResult(String ipAddress) {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);
        result.setCity("本地环境");
        result.setProvince("本地环境");
        result.setCountry("中国");
        result.setIsp("本地网络");
        result.setMessage("检测到本地IP地址，无法进行精确定位");
        result.setSuccess(true);
        return result;
    }

    /**
     * 创建模拟结果（用于演示）
     */
    private LocationResult createMockResult(String ipAddress) {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);
        result.setCity("北京市");
        result.setProvince("北京");
        result.setCountry("中国");
        result.setIsp("电信");
        result.setMessage("使用模拟数据（请配置ip2region.xdb文件获取真实定位）");
        result.setSuccess(true);
        return result;
    }

    /**
     * 创建错误结果对象
     */
    private LocationResult createErrorResult(String ipAddress, String errorMessage) {
        LocationResult result = new LocationResult();
        result.setClientIp(ipAddress);
        result.setCity("获取失败");
        result.setMessage(errorMessage);
        result.setSuccess(false);
        return result;
    }
}
