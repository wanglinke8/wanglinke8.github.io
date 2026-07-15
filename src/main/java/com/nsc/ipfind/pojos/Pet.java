package com.nsc.ipfind.pojos;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 宠物主表
 * @TableName pet
 */
@TableName(value ="pet")
@Data
public class Pet {
    /**
     * 宠物ID (接口主键)
     */
    @TableId
    private Integer petId;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 图鉴编号，如 "002"
     */
    private String handbookNo;

    /**
     * 品质/稀有度: 1=普通 2=稀有 3=史诗 4=传说
     */
    private Integer quality;

    /**
     * 进化阶段: 1=一阶 2=二阶 3=最终形态
     */
    private Integer stage;

    /**
     * 赛季ID, 0=常驻精灵
     */
    private Integer seasonId;

    /**
     * 是否有骑乘天赋: 0=否 1=是
     */
    private Integer hasRideTalent;

    /**
     * 是否有闪光/异色形态: 0=否 1=是
     */
    private Integer hasShiny;

    /**
     * 最小身高(cm)
     */
    private BigDecimal heightMinCm;

    /**
     * 最大身高(cm)
     */
    private BigDecimal heightMaxCm;

    /**
     * 最小体重(g)
     */
    private Integer weightMinG;

    /**
     * 最大体重(g)
     */
    private Integer weightMaxG;

    /**
     * 图标URL
     */
    private String icon;

    /**
     * 小图标URL
     */
    private String smallIcon;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

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
        Pet other = (Pet) that;
        return (this.getPetId() == null ? other.getPetId() == null : this.getPetId().equals(other.getPetId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getHandbookNo() == null ? other.getHandbookNo() == null : this.getHandbookNo().equals(other.getHandbookNo()))
            && (this.getQuality() == null ? other.getQuality() == null : this.getQuality().equals(other.getQuality()))
            && (this.getStage() == null ? other.getStage() == null : this.getStage().equals(other.getStage()))
            && (this.getSeasonId() == null ? other.getSeasonId() == null : this.getSeasonId().equals(other.getSeasonId()))
            && (this.getHasRideTalent() == null ? other.getHasRideTalent() == null : this.getHasRideTalent().equals(other.getHasRideTalent()))
            && (this.getHasShiny() == null ? other.getHasShiny() == null : this.getHasShiny().equals(other.getHasShiny()))
            && (this.getHeightMinCm() == null ? other.getHeightMinCm() == null : this.getHeightMinCm().equals(other.getHeightMinCm()))
            && (this.getHeightMaxCm() == null ? other.getHeightMaxCm() == null : this.getHeightMaxCm().equals(other.getHeightMaxCm()))
            && (this.getWeightMinG() == null ? other.getWeightMinG() == null : this.getWeightMinG().equals(other.getWeightMinG()))
            && (this.getWeightMaxG() == null ? other.getWeightMaxG() == null : this.getWeightMaxG().equals(other.getWeightMaxG()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getSmallIcon() == null ? other.getSmallIcon() == null : this.getSmallIcon().equals(other.getSmallIcon()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPetId() == null) ? 0 : getPetId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getHandbookNo() == null) ? 0 : getHandbookNo().hashCode());
        result = prime * result + ((getQuality() == null) ? 0 : getQuality().hashCode());
        result = prime * result + ((getStage() == null) ? 0 : getStage().hashCode());
        result = prime * result + ((getSeasonId() == null) ? 0 : getSeasonId().hashCode());
        result = prime * result + ((getHasRideTalent() == null) ? 0 : getHasRideTalent().hashCode());
        result = prime * result + ((getHasShiny() == null) ? 0 : getHasShiny().hashCode());
        result = prime * result + ((getHeightMinCm() == null) ? 0 : getHeightMinCm().hashCode());
        result = prime * result + ((getHeightMaxCm() == null) ? 0 : getHeightMaxCm().hashCode());
        result = prime * result + ((getWeightMinG() == null) ? 0 : getWeightMinG().hashCode());
        result = prime * result + ((getWeightMaxG() == null) ? 0 : getWeightMaxG().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getSmallIcon() == null) ? 0 : getSmallIcon().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", petId=").append(petId);
        sb.append(", name=").append(name);
        sb.append(", handbookNo=").append(handbookNo);
        sb.append(", quality=").append(quality);
        sb.append(", stage=").append(stage);
        sb.append(", seasonId=").append(seasonId);
        sb.append(", hasRideTalent=").append(hasRideTalent);
        sb.append(", hasShiny=").append(hasShiny);
        sb.append(", heightMinCm=").append(heightMinCm);
        sb.append(", heightMaxCm=").append(heightMaxCm);
        sb.append(", weightMinG=").append(weightMinG);
        sb.append(", weightMaxG=").append(weightMaxG);
        sb.append(", icon=").append(icon);
        sb.append(", smallIcon=").append(smallIcon);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}
