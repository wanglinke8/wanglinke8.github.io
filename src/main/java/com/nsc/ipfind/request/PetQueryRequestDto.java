package com.nsc.ipfind.request;

import lombok.Data;
import javax.validation.constraints.Min;

/**
 * 宠物查询请求参数DTO
 */
@Data
public class PetQueryRequestDto {

    /**
     * 名称关键词 - 支持模糊匹配宠物名称
     */
    private String q;

    /**
     * 属性筛选 - 如 "草"、"水"、"火" 等属性类型
     */
    private String type;

    /**
     * 蛋组筛选 - 根据蛋组类型进行筛选
     */
    private String eggGroup;

    /**
     * 技能名称筛选 - 根据技能名称进行筛选
     */
    private String skill;

    /**
     * 技能 ID - 根据技能ID进行精确筛选
     */
    private Integer skillId;

    /**
     * 页码 - 默认为第一页
     */
    @Min(value = 1, message = "页码必须大于等于1")
    private Integer pageNo = 1;

    /**
     * 每页数量 - 默认为20条
     */
    private Integer pageSize = 20;
}
