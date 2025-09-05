package com.nsc.ipfind.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 地理位置查询结果表
 * @TableName location_results
 */
@TableName(value ="location_results")
@Data
public class LocationResults implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
    private Integer success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 查询时间戳（精确到毫秒）
     */
    private Date timestamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LocationResults other = (LocationResults) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
            && (this.getPublicIp() == null ? other.getPublicIp() == null : this.getPublicIp().equals(other.getPublicIp()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getIsp() == null ? other.getIsp() == null : this.getIsp().equals(other.getIsp()))
            && (this.getRawLocation() == null ? other.getRawLocation() == null : this.getRawLocation().equals(other.getRawLocation()))
            && (this.getSuccess() == null ? other.getSuccess() == null : this.getSuccess().equals(other.getSuccess()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getPublicIp() == null) ? 0 : getPublicIp().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getIsp() == null) ? 0 : getIsp().hashCode());
        result = prime * result + ((getRawLocation() == null) ? 0 : getRawLocation().hashCode());
        result = prime * result + ((getSuccess() == null) ? 0 : getSuccess().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", publicIp=").append(publicIp);
        sb.append(", city=").append(city);
        sb.append(", province=").append(province);
        sb.append(", country=").append(country);
        sb.append(", isp=").append(isp);
        sb.append(", rawLocation=").append(rawLocation);
        sb.append(", success=").append(success);
        sb.append(", message=").append(message);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}